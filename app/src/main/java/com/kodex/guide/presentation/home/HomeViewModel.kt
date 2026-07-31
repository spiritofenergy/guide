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
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.kodex.guide.data.model.BookFilter
import com.kodex.guide.domain.repository.BooksRepo
import com.kodex.guide.domain.repository.FavoritesRepo
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.model.Favorite
import com.kodex.guide.domain.model.FilterData
import com.kodex.guide.ui.db.MainDb
import com.kodex.guide.domain.model.BookCategories
import com.kodex.guide.domain.model.FilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val booksRepo: BooksRepo,
    private val favoritesRepo: FavoritesRepo,
    private val mainDb: MainDb,

) : ViewModel() {

    val isEdit = mutableStateOf(false)

    val minPriceValue = mutableFloatStateOf(0f)
    val maxPriceValue = mutableFloatStateOf(0f)
    val isFilterByTitle = mutableStateOf(true)
    var showTabOneOrTo = mutableStateOf(false)
    val selectedBottomItemState = mutableIntStateOf(BottomMenuItem.Home.titleId)
    val isAdminState = mutableStateOf(false)
    // 1. НОВОЕ СОСТОЯНИЕ: Факт авторизации пользователя
    val isAuthorized = mutableStateOf(Firebase.auth.currentUser != null)

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

    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

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
                list +  ChangedTempBook(
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
    /*
        fun setPriceFilter(minPrice: Float, maxPrice: Float) {
            booksRepo.minPrice = minPrice.toInt()
            booksRepo.maxPrice = maxPrice.toInt()
        }*/

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
                    updateChangedBook(bookToDelete!!, true ,)
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

    // 1. НОВАЯ ФУНКЦИЯ: Анонимный вход без регистрации
    fun loginAnonymously(onResult: (Boolean) -> Unit) {
        // Если уже авторизован, просто возвращаем true
        if (Firebase.auth.currentUser != null) {
            onResult(true)
            return
        }

        Firebase.auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isAuthorized.value = true // Обновляем состояние
                    // При успешном входе обновляем ключи избранного
                    refreshFavoritesKeys()
                    onResult(true)
                } else {
                    sendUiState(MainUiState.Error(task.exception?.message ?: "Ошибка анонимного входа"))
                    onResult(false)
                }
            }
    }
    // 3. НОВАЯ ФУНКЦИЯ: Выход из аккаунта
    fun logout() {
        Firebase.auth.signOut()
        isAuthorized.value = false
        isAdminState.value = false
        refreshFavoritesKeys() // Сбрасываем избранное при выходе
    }
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
}