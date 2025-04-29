package com.kodex.guide.ui.mainScreen

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.bottomMenu.BottomMenuItem
import com.kodex.guide.ui.castom.FilterData
import com.kodex.guide.ui.utils.Categories
import com.kodex.guide.ui.utils.firebase.FireStoreManagerPaging
import com.kodex.guide.ui.utils.firebase.FirebaseConst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val fireStoreManagerPaging: FireStoreManagerPaging,
    private val pager: Flow<PagingData<Book>>
) : ViewModel() {
    val isEdit = mutableStateOf(false)
    val minPriceValue = mutableFloatStateOf(0f)
    val maxPriceValue = mutableFloatStateOf(0f)
    val isFilterByTitle = mutableStateOf(true)
    val selectedBottomItemState = mutableIntStateOf(BottomMenuItem.Home.titleId)
    val categoryState = mutableIntStateOf(Categories.ALL)
    var bookToDelete: Book? = null
    private var deleteBook = false
    private val bookListUpdate = MutableStateFlow<List<Book>>(emptyList())
    val books: Flow<PagingData<Book>> = pager.cachedIn(viewModelScope)
        .combine(bookListUpdate) { pagingData, booksList ->
            val pgData = pagingData.map { book ->
                book
                val updateBook = booksList.find {
                    it.key == book.key
                }
                updateBook ?: book
            }
            if (deleteBook) {
                deleteBook = false
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

    fun setFilter() {
        val filterData = FilterData(
            minPrise = minPriceValue.floatValue.toInt(),
            maxPrise = maxPriceValue.floatValue.toInt(),
            filterType = if (isFilterByTitle.value) {
                FirebaseConst.TITLE
            } else 
                FirebaseConst.PRICE
        )
        fireStoreManagerPaging.filterData = filterData
    }

    fun deleteBook(uiList: List<Book>) {
        if (bookToDelete == null) return
        fireStoreManagerPaging.deleteBook(
            bookToDelete!!,
            onDeleted = {
                bookListUpdate.value = uiList.filter {
                    it.key != bookToDelete!!.key
                }
            },
            onFailure = {
               sendUiState(MainUiState.Error(it))
               }
        )
    }

    fun searchBook(searchText: String){
        fireStoreManagerPaging.searchText = searchText
    }

    fun getBooksFromCategory(categoryIndex: Int) {
        categoryState.intValue = categoryIndex
        fireStoreManagerPaging.categoryIndex = categoryIndex

    }
    fun onFavesClick(book: Book, isFavesState: Int, bookList: List<Book>) {
        val bookList = fireStoreManagerPaging.changeFavesState(bookList, book)
        bookListUpdate.value = if (isFavesState == BottomMenuItem.Faves.titleId) {
            deleteBook = true
            bookList.filter { it.isFaves }
        } else {
            bookList
        }
        // isFavesListEmptyState.value = bookListUpdate.value.isEmpty()
    }


    sealed class MainUiState {
        data object Loading : MainUiState()
        data object Success : MainUiState()
        data class  Error(val massage: String) : MainUiState()
    }
}