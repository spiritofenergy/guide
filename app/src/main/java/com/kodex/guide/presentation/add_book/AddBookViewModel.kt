package com.kodex.guide.presentation.add_book

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.bookmarketcompose.R
import com.kodex.guide.data.images.BitmapEncoder
import com.kodex.guide.data.source.remote.FirebaseAuthDataSource
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
    private val bitmapEncoder: BitmapEncoder,
    private val authRepository: FirebaseAuthDataSource, // или UserSession

) : ViewModel() {

    val title = mutableStateOf("")
    val village = mutableStateOf("")
    val description = mutableStateOf("")
    val price = mutableIntStateOf(50)
    val telephone = mutableStateOf("")
    val location = mutableStateOf(false)
    val street = mutableStateOf("")
    val flat = mutableStateOf("")
    val house = mutableStateOf("")
    val delivery = mutableStateOf(false)
    val payment = mutableStateOf(false)
    var imageBase64 = mutableStateOf("")

    val validationError = mutableStateOf<String?>(null)

    val selectedCategory = mutableStateOf(BookCategories.ALL)
    val selectedImageUri = mutableStateOf<Uri?>(null)
    val showLoadingIndicator = mutableStateOf(false)

    private val _uiState = MutableSharedFlow<HomeViewModel.MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: HomeViewModel.MainUiState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    // Добавьте метод для конвертации URI в Base64
    fun convertImageToBase64(uri: Uri): String {
        return bitmapEncoder.imageToBase64(uri)
    }
    // ✅ НОВЫЙ МЕТОД: Асинхронная конвертация в Base64 (не блокирует UI)
  /*  fun convertImageToBase64Async(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val base64String = bitmapEncoder.imageToBase64(uri)
            withContext(Dispatchers.Main) {
                imageBase64.value = base64String
            }
        }
    }
*/
    // Функция валидации
    fun validateBook(context: Context): Boolean {
        val errors = mutableListOf<String>()
        if (title.value.isBlank()) { errors.add(context.getString(R.string.title_is_required)) }
        if (description.value.isBlank()) { errors.add(context.getString(R.string.description_is_required)) }
        if (village.value.isBlank()) { errors.add(context.getString(R.string.please_specify_the_village)) }
        if (price.intValue <= 0) { errors.add(context.getString(R.string.enter_a_valid_price)) }
        // 🔍 Проверка фото
        if (selectedImageUri.value == null && imageBase64.value.isBlank()) {
            errors.add(context.getString(R.string.please_add_a_photo))
        }
        if (errors.isNotEmpty()) {
            validationError.value = errors.joinToString("\n")
            return false
        }
        validationError.value = null
        return true
    }

    // Сброс ошибки (вызывать при закрытии диалога)
    fun clearValidationError() {
        validationError.value = null
    }
    fun setDefaultData(navData: NavRoutes.AddScreenObject) {
        Log.d("EditDebug", "Пришло на экран: village=${navData.village}, delivery=${navData.delivery}, payment=${navData.payment}")

        title.value = navData.title
        description.value = navData.description
        price.intValue = navData.price
        telephone.value = navData.telephone
        selectedCategory.value = navData.categoryIndex
        village.value = navData.village
        street.value = navData.village
        house.value = navData.house
        flat.value = navData.flat
        location.value = navData.location
        delivery.value = navData.delivery
        payment.value = navData.payment

        // ✅ Если есть фото в navData, сохраняем его
        if (navData.imageUrl.isNotEmpty()) {
            imageBase64.value = navData.imageUrl
        }
    }

    fun uploadBook(book: Book, ) {
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
                    payment = payment.value,
                    imageUrl = imageBase64.value // ✅ ДОБАВЛЕНО
                ),

                selectedImageUri.value)
            result.fold(
                onSuccess = { sendUiState(HomeViewModel.MainUiState.Success) },
                onFailure = { error-> sendUiState(HomeViewModel.MainUiState.Error(error.message ?: "Unknow error"))
                }
            )
        }
    }
}