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

    val validationError = mutableStateOf<String?>(null)
    val isEditMode = mutableStateOf(false) // Флаг: редактирование или создание
    var imageBase64 = mutableStateOf("")

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

    // Функция валидации
    fun validateBook(): Boolean {
        val errors = mutableListOf<String>()

        if (title.value.isBlank()) {
            errors.add("• Название обязательно для заполнения")
        }

        if (description.value.isBlank()) {
            errors.add("• Описание обязательно для заполнения")
        }

        if (village.value.isBlank()) {
            errors.add("• Укажите станицу")
        }

        if (price.intValue <= 0) {
            errors.add("• Укажите корректную цену")
        }
        // 🔍 Проверка фото
        if (selectedImageUri.value == null && imageBase64.value.isBlank() && !isEditMode.value) {
            errors.add("• Добавьте фото книги")
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
        village.value = navData.village
        description.value = navData.description
        price.intValue = navData.price
        telephone.value = navData.telephone
        selectedCategory.value = navData.categoryIndex
        delivery.value = navData.delivery
        payment.value = navData.payment
        // ✅ Устанавливаем флаг режима редактирования
        isEditMode.value = navData.id != 0 && navData.key.isNotEmpty()

        // ✅ Если есть фото в navData, сохраняем его
        if (navData.imageUrl.isNotEmpty()) {
            imageBase64.value = navData.imageUrl
        }
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
                    sendUiState(HomeViewModel.MainUiState.Success) },
                onFailure = { error->
                    sendUiState(HomeViewModel.MainUiState.Error(error.message ?: "Unknow error"))
                }
            )
        }
    }
}