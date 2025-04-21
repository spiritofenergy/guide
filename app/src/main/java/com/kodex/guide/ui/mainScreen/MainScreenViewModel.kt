package com.kodex.guide.ui.mainScreen

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.bottomMenu.BottomMenuItem
import com.kodex.guide.ui.utils.Categories
import com.kodex.guide.ui.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MsViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging,
    private val pager: Flow<PagingData<Book>>
) : ViewModel() {
    val bookListState = mutableStateOf(emptyList<Book>())
    val selectedBottomItemState = mutableIntStateOf(BottomMenuItem.Home.titleId)
    val isFavesListEmptyState = mutableStateOf(false)
    val categoryState = mutableIntStateOf(Categories.ALL)
    var bookToDelete: Book? = null
    val showLoadingIndicator = mutableStateOf(false)


    val books: Flow<PagingData<Book>> = pager.cachedIn(viewModelScope)

    // val books = pager.cachedIn(viewModelScope)
    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: MainUiState) = viewModelScope.launch {
        _uiState.emit(state)
    }

fun getBooksFromCategory(categoryIndex: Int) {
  categoryState.intValue = categoryIndex
    fireStoreManager.categoryIndex = categoryIndex

}


/*
fun deleteBook() {
  if (bookToDelete == null) return
  fireStoreManager.deleteBook(
      bookToDelete!!,
      onDeleted = {
          bookListState.value = bookListState.value.filter {
              it.key != bookToDelete!!.key
          }
      }
  )
}*/
/*

fun onFavesClick(book: Book, isFavesState: Int) {
  val bookList = fireStoreManager.changeFavesState(bookListState.value, book)
  bookListState.value = if (isFavesState == BottomMenuItem.Faves.titleId) {
      bookList.filter { it.isFaves }
  } else {
      bookList
  }
  isFavesListEmptyState.value = bookListState.value.isEmpty()
}
*/

sealed class MainUiState {
  data object Loading : MainUiState()
  data object Success : MainUiState()
  data class Error(val massage: String) : MainUiState()
}
}