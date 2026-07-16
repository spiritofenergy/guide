package com.kodex.guide.presentation.add_book

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.domain.model.Book
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.presentation.home.HomeViewModel
import com.kodex.guide.domain.model.BookCategories
import com.kodex.guide.data.source.remote.FirebaseConst.POSTS
import com.kodex.guide.domain.repository.BooksRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
private val booksRepo: BooksRepo
) : ViewModel() {

    val title = mutableStateOf("")
    val village = mutableStateOf("")
    val description = mutableStateOf("")
    val price = mutableIntStateOf(0)
    val telephone = mutableStateOf("")
    val selectedCategory = mutableStateOf(BookCategories.ALL)
    val selectedImageUri = mutableStateOf<Uri?>(null)
    val showLoadingIndicator = mutableStateOf(false)

    private val _uiState = MutableSharedFlow<HomeViewModel.MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: HomeViewModel.MainUiState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    fun setDefaultData(navData: NavRoutes.AddScreenObject) {
        title.value = navData.title
        village.value = navData.village
        description.value = navData.description
        price.intValue = navData.price
        telephone.value = navData.telephone
        selectedCategory.value = navData.categoryIndex

    }

    fun uploadBook(
        book: Book,
    ) {
        sendUiState(HomeViewModel.MainUiState.Loading)
        viewModelScope.launch {
            val result = booksRepo.saveBook(
                book.copy(
                    title = title.value,
                    description = description.value,
                    price = price.value,
                    village = village.value,
                    categoryIndex = selectedCategory.value),
                selectedImageUri.value)
            result.fold(
                onSuccess = {
                    sendUiState(HomeViewModel.MainUiState.Success) },
                onFailure = { error->
                    sendUiState(HomeViewModel.MainUiState.Error(error.message ?: "Unknow error"))
                }
            )
        }
    }
}