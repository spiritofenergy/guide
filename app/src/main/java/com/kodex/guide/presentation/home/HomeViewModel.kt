package com.kodex.guide.presentation.home

import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.kodex.guide.data.mapper.toDTO
import com.kodex.guide.data.mapper.toUser
import com.kodex.guide.data.model.BookFilter
import com.kodex.guide.data.source.local.PreferenceDataSource
import com.kodex.guide.data.source.remote.FirebaseAuthDataSource
import com.kodex.guide.domain.repository.BooksRepo
import com.kodex.guide.domain.repository.FavoritesRepo
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.model.Favorite
import com.kodex.guide.domain.model.FilterData
import com.kodex.guide.ui.db.MainDb
import com.kodex.guide.domain.model.BookCategories
import com.kodex.guide.domain.model.FilterType
import com.kodex.guide.domain.model.Permission
import com.kodex.guide.domain.model.User
import com.kodex.guide.domain.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ✅ События для UI (диалоги, навигация)
sealed class AuthEvent {
    data object ShowLoginDialog : AuthEvent()
    data object ShowLogoutDialog : AuthEvent()
    data class NavigateToRegistration(val message: String) : AuthEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val booksRepo: BooksRepo,
    private val favoritesRepo: FavoritesRepo,
    private val authDataSource: FirebaseAuthDataSource,
    private val mainDb: MainDb,
    private val preferenceDataSource: PreferenceDataSource   // ✅ добавили

) : ViewModel() {

    val isEdit = mutableStateOf(false)
    val minPriceValue = mutableFloatStateOf(0f)
    val maxPriceValue = mutableFloatStateOf(0f)
    val isFilterByTitle = mutableStateOf(true)
    var showTabOneOrTo = mutableStateOf(false)
    val selectedBottomItemState = mutableIntStateOf(BottomMenuItem.Home.titleId)
    val isAdminState = mutableStateOf(false)

    // ✅ профиль для DrawerHeader
    val headerUser = mutableStateOf<User?>(null)


    val isAuthorized = mutableStateOf(authDataSource.getCurrentUser() != null)
    val userRole = mutableStateOf(UserRole.ANONYMOUS)

    // Состояние для диалога
    val showAuthDialog = mutableStateOf(false)
    val isLogoutDialog = mutableStateOf(false)

    // val isAuthorized = mutableStateOf(Firebase.auth.currentUser != null)
    var isRegisterState = mutableStateOf(false)
    val categoryState = mutableStateOf(BookCategories.ALL)
    var bookToDelete: Book? = null

    private val bookListUpdate = MutableStateFlow<List<ChangedTempBook>>(emptyList())


    private val bookFilterStateFlow = MutableStateFlow<BookFilter>(BookFilter())
    private val searchStateFlow = MutableStateFlow("")
    private val debounceSearchFlow = searchStateFlow
        .debounce(500)
        .distinctUntilChanged()
    private val favoritesKeysFlow = MutableStateFlow<List<String>>(emptyList())


    val postList = mainDb.trackDao.getAllPosts()

    @OptIn(ExperimentalCoroutinesApi::class)
    val books: Flow<PagingData<Book>> = combine(
        favoritesKeysFlow,
        bookFilterStateFlow,
        debounceSearchFlow
    ) { keysList, bookFilter, searchText ->
        Triple(keysList, bookFilter, searchText)
    }.flatMapLatest { (keysList, filter, searchText) ->
        booksRepo.getBooks(
            keysList, filter.copy(
                searchText = searchText
            )
        )
    }.cachedIn(viewModelScope)
        .combine(bookListUpdate) { pagingData, changedBookList ->
            pagingData.filter { pData ->
                val book = changedBookList.find { it.key == pData.key }
                if (book != null) {
                    !book.isDeleted
                } else {
                    true
                }
            }.map { pData ->
                val book = changedBookList.find { it.key == pData.key }
                if (book != null) {
                    pData.copy(
                        isFavorite = book.isFavorite
                    )
                } else {
                    pData
                }
            }
        }
    private val _authEvents = MutableSharedFlow<AuthEvent>()
    val authEvents = _authEvents.asSharedFlow()

    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

    // ✅ НОВОЕ: Роль текущего пользователя
    private val _userRole = MutableStateFlow(UserRole.ANONYMOUS)
    // val userRole: StateFlow<UserRole> = _userRole.asStateFlow()

  /*  // ✅ Профиль для шапки: мгновенно из кеша, обновляется реактивно
    val cachedUser: StateFlow<User?> = preferenceDataSource.headerUser
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
*/
    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Firebase.firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { doc ->
                    if (!doc.exists()) {
                        // ✅ Используем маппер вместо хардкода
                        val user = currentUser.toUser(role = UserRole.USER)
                        createUserProfile(user)
                    }
                    loadUserRole(currentUser.uid)
                }
            // ...
        }
    }

    init {
        // миграция: кеш пуст, но пользователь залогинен — заполняем кеш
        if (preferenceDataSource.getUser() == null) {
            authDataSource.getCurrentUser()?.let { preferenceDataSource.saveUser(it) }
        }
        refreshHeader()
        // Загружаем роль при старте
        authDataSource.getCurrentUser()?.let { user ->
            userRole.value = user.role
            isAuthorized.value = true
        }

        // ✅ ДОБАВЛЕНО: Регистрируем слушатель состояния авторизации
        Firebase.auth.addAuthStateListener(authStateListener)
        // Если уже авторизован - загружаем роль
        Firebase.auth.currentUser?.uid?.let { loadUserRole(it) }

        /*    loadFromRoom()
        viewModelScope.launch {
            delay(100)
            checkNetworkAndSync()
        }
*/

    }

    fun refreshHeader() {
        headerUser.value = preferenceDataSource.getUser()
    }

    private var roleListener: ListenerRegistration? = null

    private fun loadUserRole(uid: String) {
        roleListener?.remove()
        roleListener = Firebase.firestore.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("MyLog", "Ошибка загрузки роли: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val roleString = snapshot.getString("role") ?: "USER"
                    val role = try {
                        UserRole.valueOf(roleString)
                    } catch (e: Exception) {
                        UserRole.USER
                    }
                    _userRole.value = role
                    userRole.value = role      // ← синхронизируем с Compose-состоянием
                    isAuthorized.value = true
                    Log.d("MyLog", "Роль пользователя: $role")
                    // ✅ роль из Firestore синхронизируем в кеш
                    viewModelScope.launch { preferenceDataSource.saveRole(role) }
                }
            }
    }

    fun createUserProfile(user: User) {
        Firebase.firestore
            .collection("users")
            .document(user.uid)
            .set(user.toDTO())
            .addOnSuccessListener {
                Log.d("MyLog", "Профиль создан для ${user.uid}")
            }
            .addOnFailureListener { e ->
                Log.e("MyLog", "Ошибка создания профиля: ${e.message}")
            }
    }

    // Повышение до BUSINESS
    fun upgradeToBusiness(uid: String) {
        Firebase.firestore.collection("users")
            .document(uid)
            .update("role", UserRole.BUSINESS.name)
    }

    // ✅ Проверка прав
    fun hasPermission(permission: Permission): Boolean {
        return permission.isGrantedBy(_userRole.value)
    }

    fun canAccess(requiredRole: UserRole): Boolean {
        return _userRole.value.hasAccessTo(requiredRole)
    }

    // ✅ ДОБАВЛЕНО: Очищаем слушатель при уничтожении ViewModel
    override fun onCleared() {
        super.onCleared()
        roleListener?.remove()
        Firebase.auth.removeAuthStateListener(authStateListener)
    }

    fun getSettings() = viewModelScope.launch {
        favoritesKeysFlow.collect { keysList ->

        }
        /*    booksRepo.getSettings(
                onSettingsLoaded = { pData, aData, sData ->
                    globalSettings.personalData = pData
                    globalSettings.addressData = aData
                    globalSettings.userSettingsData = sData
                }
            )*/
    }

    init {
        refreshFavoritesKeys()
    }

    private fun refreshFavoritesKeys() = viewModelScope.launch {
        val result = favoritesRepo.getIdsFavesList()
        result.fold(
            onSuccess = { keysList ->
                favoritesKeysFlow.value = keysList
            },
            onFailure = {
                favoritesKeysFlow.value = emptyList()
                sendUiState(MainUiState.Error(it.message ?: "Unknown error"))
            }
        )
    }

    private fun sendUiState(state: MainUiState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    fun clearTempBookList() {
        bookListUpdate.value = emptyList()
    }

    private fun updateChangedBook(book: Book, isDeleted: Boolean) {
        bookListUpdate.update { list ->
            val changedBook = list.find { book.key == it.key }
            if (changedBook == null) {
                list + ChangedTempBook(
                    key = book.key,
                    isFavorite = book.isFavorite,
                    isDeleted = isDeleted
                )
            } else {
                list.map { tempBook ->
                    if (tempBook.key == book.key) {
                        tempBook.copy(
                            isFavorite = book.isFavorite,
                            isDeleted = isDeleted
                        )
                    } else {
                        tempBook
                    }
                }
            }
        }
    }

    fun setFilter() {
        val filterData = FilterData(
            minPrise = minPriceValue.floatValue.toInt(),
            maxPrise = maxPriceValue.floatValue.toInt(),
            filterType = if (isFilterByTitle.value) {
                FilterType.TITLE
            } else
                FilterType.PRICE
        )
        bookFilterStateFlow.update { filter ->
            filter.copy(
                filterData = filterData
            )
        }
    }

    fun deleteBook() {
        sendUiState(MainUiState.Loading)
        if (bookToDelete == null) return
        viewModelScope.launch {
            val result = booksRepo.deleteBook(bookToDelete!!)
            result.fold(
                onSuccess = {
                    updateChangedBook(bookToDelete!!, true)
                    sendUiState(MainUiState.Success)
                },
                onFailure = { error ->
                    sendUiState(MainUiState.Error(error.message ?: "Unknow error"))
                }
            )
        }
    }

    fun searchBook(searchText: String) {
        searchStateFlow.update {
            searchText
        }
    }

    fun getAllBooksFromCategory(category: BookCategories) {
        categoryState.value = category
        clearTempBookList()
        refreshFavoritesKeys()
        bookFilterStateFlow.update { filter ->
            filter.copy(category = category)
        }
        Log.d("MyLog", "getAllBooksFromCategory: $category")
    }

    fun onFavesClick(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        val needDelete = selectedBottomItemState.intValue == BottomMenuItem.Saved.titleId
        val favsResult = favoritesRepo.onFavorites(Favorite(book.key), !book.isFavorite)
        favsResult.fold(
            onSuccess = {
                updateChangedBook(book.copy(isFavorite = book.isFavorite), needDelete)
                sendUiState(MainUiState.Success)
            },
            onFailure = {
                sendUiState(MainUiState.Error(it.message ?: "Unknown error"))
            }
        )
    }

    fun insertPost(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        mainDb.trackDao.insertPost(book)
    }

    // Получить все сохраненные книги
    fun getAllSavedBooks(): Flow<List<Book>> {
        return mainDb.trackDao.getAllPosts()
    }


    sealed class MainUiState {
        data object Loading : MainUiState()
        data object Success : MainUiState()
        data class Error(val message: String) : MainUiState()
    }

    // ✅ Обработка клика по кнопке входа/выхода
    fun onAuthButtonClick() = viewModelScope.launch {
        if (isAuthorized.value) {
            // Пользователь авторизован — предлагаем выйти
            _authEvents.emit(AuthEvent.ShowLogoutDialog)
        } else {
            // Пользователь не авторизован — предлагаем войти
            _authEvents.emit(AuthEvent.ShowLoginDialog)
        }
    }

    // ✅ Анонимный вход
    fun loginAnonymously() = viewModelScope.launch {
        try {
            authDataSource.signInAnonymously()
            Log.d("MyLog", "Анонимный вход успешен")
            // authStateListener автоматически загрузит роль
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка анонимного входа: ${e.message}")
            sendUiState(MainUiState.Error("Ошибка входа: ${e.message}"))
        }
    }

    // ✅ Переход к регистрации
    fun navigateToRegistration() = viewModelScope.launch {
        _authEvents.emit(AuthEvent.NavigateToRegistration("Для доступа требуется регистрация"))
    }

    // 3. НОВАЯ ФУНКЦИЯ: Выход из аккаунта
    fun logout() {
        Firebase.auth.signOut()
        authDataSource.signOut()
        userRole.value = UserRole.ANONYMOUS
        _userRole.value = UserRole.ANONYMOUS
        isAuthorized.value = false
        isAdminState.value = false

        preferenceDataSource.clearUserSession()   // ✅ шапка сразу покажет Anonymous
        refreshHeader()
        Log.d("MyLog", " logout isAdminState.value = false")
       // viewModelScope.launch { preferenceDataSource.clearUser() }

    }

    // ✅ Закрытие диалога
    fun dismissAuthDialog() {
        showAuthDialog.value = false
    }

    // ✅ Проверка прав
    fun canCreatePost(): Boolean =
        hasPermission(Permission.CREATE_POST)

    fun canModerate(): Boolean =
        hasPermission(Permission.MODERATE_CONTENT)


    // 2. ИСПРАВЛЕНИЕ: Убираем !! чтобы избежать краша у неавторизованных пользователей
    fun isAdmin(onAdmin: (Boolean) -> Unit) {
        val uid = Firebase.auth.currentUser?.uid ?: run {
            onAdmin(false) // Если нет пользователя, он точно не админ
            return
        }

        Firebase.firestore.collection("admin")
            .document(uid)
            .get()
            .addOnSuccessListener {
                onAdmin(it.get("isAdmin") as? Boolean ?: false)
            }
            .addOnFailureListener {
                onAdmin(false)
            }
    }

    // 3. ИСПРАВЛЕНИЕ: Аналогично для проверки регистрации
    fun isUserRegistered(onRegister: (Boolean) -> Unit) {
        val uid = Firebase.auth.currentUser?.uid ?: run {
            onRegister(false)
            return
        }

        Firebase.firestore.collection("guide_users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                // Анонимный пользователь не пройдет эту проверку, вернется false
                onRegister(it.get("isRegistered") as? Boolean ?: false)
            }
            .addOnFailureListener {
                onRegister(false)
            }
    }
}
/*  fun isAdmin(onAdmin: (Boolean) -> Unit) {
      val uid = Firebase.auth.currentUser!!.uid
      Firebase.firestore.collection("admin")
          .document(uid)
          .get()
          .addOnSuccessListener {
              onAdmin(it.get("isAdmin") as Boolean)
          }
  }*/
/*

    fun isUserRegistered(onRegister: (Boolean) -> Unit) {
        val uid = Firebase.auth.currentUser!!.uid
        Firebase.firestore.collection("guide_users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                onRegister(it.get("isRegistered") as Boolean)
            }

    }
*/


/*    fun isUserRegistered(onRegister: (Boolean) -> Unit) {
        val uid = Firebase.auth.currentUser!!.uid
        Firebase.firestore.collection("guide_users")
            .document(uid)
     isRegisterState = true
    }*/
