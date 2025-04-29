package com.kodex.guide.ui.addscreen.data

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.ui.mainScreen.MainScreenViewModel.MainUiState
import com.kodex.guide.ui.utils.Categories
import com.kodex.guide.ui.utils.firebase.FireStoreManagerPaging
import com.kodex.guide.ui.utils.toBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val fireStoreManagerPaging: FireStoreManagerPaging,
) : ViewModel() {

    val title = mutableStateOf("")
    val description = mutableStateOf("")
    val prise = mutableStateOf("")
    val telephone = mutableStateOf("")
    val selectedCategory = mutableIntStateOf(Categories.FANTASY)
    val selectedImageUri = mutableStateOf<Uri?>(null)
    val showLoadingIndicator = mutableStateOf(false)
    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: MainUiState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    fun setDefaultData(navData: AddScreenObject) {
        title.value = navData.title
        description.value = navData.description
        prise.value = navData.price
        telephone.value = navData.telephone
        selectedCategory.intValue = navData.categoryIndex
        //selectedImageUri.value = navData.imageUrl.toUri()

    }

    fun uploadBook(
        navData: AddScreenObject
    ) {
        sendUiState(MainUiState.Loading)
        val book = Book(
            title = title.value,
            description = description.value,
            key = navData.key,
            price = prise.value.toInt(),
            telephone = telephone.value,
            categoryIndex = selectedCategory.intValue,
            imageUrl = navData.imageUrl
        )

        fireStoreManagerPaging.saveBookImage(
            oldImageUrl = navData.imageUrl,
            uri = selectedImageUri.value,
            book = book,
            onSaved = {
                sendUiState(MainUiState.Success)
            },
            onError = { message ->
                sendUiState(MainUiState.Error(message))
            }
        )
    }
}



