package com.kodex.guide.ui.mainScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.bottomMenu.BottomMenuItem
import com.kodex.guide.ui.utils.firebase.FireStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MsViewModel @Inject constructor(
            private val firebaseManager: FireStoreManager
): ViewModel() {
    val bookListState = mutableStateOf(emptyList<Book>())
    val isFavesListEmptyState = mutableStateOf(false)
    val selectedBottomItemState = mutableStateOf(BottomMenuItem.Home.title)

    fun getAllBooks(){
        firebaseManager.getAllBooks { books ->
            bookListState.value = books
            isFavesListEmptyState.value = books.isEmpty()
        }
    }
    fun getAllFavesBook(){
        firebaseManager.getAllFavesBooks { books->
            bookListState.value = books
            isFavesListEmptyState.value = books.isEmpty()
        }
    }
    fun getBooksFromCategory(category: String){
        if (category == "All"){
            getAllBooks()
        }else{
            firebaseManager.getAllBooksFromCategory(category) {books->
                bookListState.value = books
                isFavesListEmptyState.value = books.isEmpty()
            }
        }
    }
    fun onFavesClick(book: Book, isFavesState: String){
        val bookList = firebaseManager.changeFavesState(bookListState.value, book)
        bookListState.value = if (isFavesState == BottomMenuItem.Faves.title){
            bookList.filter { it.isFaves }
        }else {
            bookList
        }
        isFavesListEmptyState.value = bookListState.value.isEmpty()
    }
    fun onDeleteBook(book: Book){
        firebaseManager.deleteBook(book)
    }
}