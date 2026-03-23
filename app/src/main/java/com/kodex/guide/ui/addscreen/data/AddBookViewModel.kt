package com.kodex.guide.ui.addscreen.data

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.ui.data.NavRoutes
import com.kodex.guide.ui.mainScreen.MainScreenViewModel.MainUiState
import com.kodex.guide.ui.utils.Categories
 import com.kodex.guide.ui.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging,
) : ViewModel() {

    val title = mutableStateOf("")
    val village = mutableStateOf("")
    val description = mutableStateOf("")
    val price = mutableStateOf("")
    val telephone = mutableStateOf("")
    val selectedCategory = mutableIntStateOf(Categories.ALL)
     val selectedImageUri = mutableStateOf<Uri?>(null)
    val showLoadingIndicator = mutableStateOf(false)

    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: MainUiState) = viewModelScope.launch {
        _uiState.emit(state)
    }

    fun setDefaultData(navData: NavRoutes.AddScreenObject) {
        title.value = navData.title
        village.value = navData.village
        description.value = navData.description
        price.value = navData.price.toString()
        telephone.value = navData.telephone
        selectedCategory.intValue = navData.categoryIndex

    }

    fun uploadBook(
        navData: NavRoutes.AddScreenObject
    ) {
        sendUiState(MainUiState.Loading)
        val book = Book(
            key = navData.key,
            title = title.value,
            description = description.value,
            price = price.value.toInt(),
            telephone = telephone.value,
            categoryIndex = selectedCategory.intValue,
            imageUrl = navData.imageUrl
        )

        fireStoreManager.saveBookImage(
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



