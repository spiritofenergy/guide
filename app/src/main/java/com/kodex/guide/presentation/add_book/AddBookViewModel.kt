package com.kodex.guide.presentation.add_book

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.data.images.BitmapEncoder
import com.kodex.guide.domain.model.Book
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.presentation.home.HomeViewModel
import com.kodex.guide.domain.model.BookCategories
import com.kodex.guide.domain.repository.BooksRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
private val booksRepo: BooksRepo,
private val bitmapEncoder: BitmapEncoder
) : ViewModel() {

    val title = mutableStateOf("")
    val village = mutableStateOf("")
    val description = mutableStateOf("")
    val price = mutableIntStateOf(0)
    val telephone = mutableStateOf("")
    val selectedCategory = mutableStateOf(BookCategories.ALL)
    val selectedImageUri = mutableStateOf<Uri?>(null)
    val showLoadingIndicator = mutableStateOf(false)
    val delivery = mutableStateOf(false)
    val payment = mutableStateOf(false)


    private val _uiState = MutableSharedFlow<HomeViewModel.MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: HomeViewModel.MainUiState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    // Добавьте метод для конвертации URI в Base64
    fun convertImageToBase64(uri: Uri): String {
        return bitmapEncoder.imageToBase64(uri)
    }

    fun setDefaultData(navData: NavRoutes.AddScreenObject) {
        Log.d("MyLogVM", "navData: title=${navData.title}," +
                "\n  key=${navData.key}, " +
              " \n  key=${navData.key}, " +
                "\n imageUrl=${navData.imageUrl}")

        title.value = navData.title
        village.value = navData.village
        description.value = navData.description
        price.intValue = navData.price
        telephone.value = navData.telephone
        selectedCategory.value = navData.categoryIndex
        delivery.value = navData.delivery
        payment.value = navData.payment

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
                    price = price.intValue,
                    village = village.value,
                    categoryIndex = selectedCategory.value,
                    delivery = delivery.value,
                    payment = payment.value
                ),
                selectedImageUri.value)
            result.fold(
                onSuccess = {
                      Log.d("MyLogV", "delivery.value:  ${delivery.value}")
                      Log.d("MyLog", "payment.value:  ${payment.value}")
                    sendUiState(HomeViewModel.MainUiState.Success) },



                onFailure = { error->
                    sendUiState(HomeViewModel.MainUiState.Error(error.message ?: "Unknow error"))
                }
            )
        }
    }
}