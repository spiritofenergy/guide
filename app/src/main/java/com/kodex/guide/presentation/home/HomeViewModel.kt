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
import com.kodex.guide.presentation.castom.FilterData
import com.kodex.guide.ui.bottomMenu.BottomMenuItem
import com.kodex.guide.ui.db.MainDb
import com.kodex.guide.domain.model.BookCategories
import com.kodex.guide.utils.FirebaseConst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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
    var isRegisterState = mutableStateOf(false)
    val categoryState = mutableStateOf(BookCategories.ALL)
    var bookToDelete: Book? = null
    private val bookListUpdate = MutableStateFlow<List<Book>>(emptyList())
    private val bookFilterStateFlow = MutableStateFlow<BookFilter>(BookFilter())

    private val favoritesKeysFlow = flow{
        val result = favoritesRepo.getIdsFavesList()
        result.fold(
            onSuccess = {keysList->
                emit(keysList)
            },
            onFailure ={
                emit(emptyList())
                sendUiState(MainUiState.Error(it.message?:"Unknown error"))
            }
        )
    }
    val postList = mainDb.trackDao.getAllPosts()

    @OptIn(ExperimentalCoroutinesApi::class)
    val books: Flow<PagingData<Book>> = combine(favoritesKeysFlow, bookFilterStateFlow) { keysList, bookFilter ->
        keysList to bookFilter
    }.flatMapLatest {(keysList, filter) ->
        booksRepo.getBooks( keysList, filter)
    }.cachedIn(viewModelScope)
        .combine(bookListUpdate) { pagingData, booksList ->
            val pgData = pagingData.map { book ->
                val updateBook = booksList.find {
                    it.key == book.key
                }
                updateBook ?: book
            }
            if (bookListUpdate.value.isNotEmpty()) {
                pgData.filter { pgData ->
                    booksList.find {
                        it.key == pgData.key
                    } != null
                }
            } else {
                pgData
            }
        }

    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()
    private fun sendUiState(state: MainUiState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    fun getSettings() = viewModelScope.launch {
        favoritesKeysFlow.collect{keysList ->

        }
    /*    booksRepo.getSettings(
            onSettingsLoaded = { pData, aData, sData ->
                globalSettings.personalData = pData
                globalSettings.addressData = aData
                globalSettings.userSettingsData = sData
            }
        )*/
    }

    fun clearTempBookList() {
        bookListUpdate.value = emptyList()
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
                FirebaseConst.TITLE
            } else
                FirebaseConst.PRICE
        )
        // fireStoreManagerPaging.filterData = filterData
    }


    fun searchBook(searchText: String) {
       // booksRepo.searchText = searchText
    }

    fun getAllBooksFromCategory(category: BookCategories) {
        categoryState.value = category
        bookFilterStateFlow.update { filter ->
            filter.copy(category = category)
        }
        Log.d("MyLog", "getAllBooksFromCategory: $category")

    }
    fun onFavesClick(book: Book, isFavesState: Int,
                     bookList: List<Book>)  = viewModelScope.launch(Dispatchers.IO){
                         val favsResult = favoritesRepo.onFavorites(Favorite(book.key),!book.isFavorite)
        favsResult.fold(
            onSuccess = {
                val newUpdateList = changeFavesState(bookList, book)
                bookListUpdate.value = if (isFavesState == BottomMenuItem.Faves.titleId) {
                    bookList.filter { it.isFavorite }
                } else {
                    bookList
                }
                sendUiState(MainUiState.Success)
            },
            onFailure = {
                sendUiState(MainUiState.Error(it.message ?: "Unknown error"))
            }
        )
    }

   private fun changeFavesState(books: List<Book>, book: Book): List<Book> {
        return books.map { bk ->
            if (bk.key == book.key) {
               /* onFaves(
                    Favorite(bk.key),
                    !bk.isFavorite
                )*/
                bk.copy(isFavorite = !bk.isFavorite)
            } else {
                bk
            }
        }
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
        data class Error(val massage: String) : MainUiState()
    }

   /* private fun deleteBook(bookId: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = booksRepo.getBookComments(bookId)
        result.fold(
            onSuccess = { commentsList ->
                _uiState.value = uiState.value.copy(
                    comments = commentsList
                )
            },
            onFailure = { error ->

            }
        )
    }*/
    fun isAdmin(onAdmin: (Boolean) -> Unit) {
        val uid = Firebase.auth.currentUser!!.uid
        Firebase.firestore.collection("admin")
            .document(uid)
            .get()
            .addOnSuccessListener {
                onAdmin(it.get("isAdmin") as Boolean)
            }
    }

    fun isUserRegistered(onRegister: (Boolean) -> Unit) {
        val uid = Firebase.auth.currentUser!!.uid
        Firebase.firestore.collection("guide_users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                onRegister(it.get("isRegistered") as Boolean)
            }

    }


    /*    fun isUserRegistered(onRegister: (Boolean) -> Unit) {
            val uid = Firebase.auth.currentUser!!.uid
            Firebase.firestore.collection("guide_users")
                .document(uid)
         isRegisterState = true
        }*/
}