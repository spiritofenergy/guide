package com.kodex.guide.presentation.add_book

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.domain.model.Book
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.presentation.home.HomeViewModel
import com.kodex.guide.ui.settingsScreen.GlobalSettings
import com.kodex.guide.utils.Categories
import com.kodex.guide.utils.FirebaseConst.POSTS
import com.kodex.guide.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging,
    val globalSettings: GlobalSettings,
) : ViewModel() {

    val title = mutableStateOf("")
    val village = mutableStateOf("")
    val description = mutableStateOf("")
    val price = mutableStateOf("")
    val telephone = mutableStateOf("")
    val selectedCategory = mutableIntStateOf(Categories.ALL)
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
        price.value = navData.price.toString()
        telephone.value = navData.telephone
        selectedCategory.intValue = navData.categoryIndex

    }

    /*fun uploadBook(
        navData: NavRoutes.AddScreenObject
    ) {
        saveBookToFirestore(
            firestore = FirebaseFirestore.getInstance(),
            Book(
                key = navData.key,
                title = title.value,
                description = description.value,
                price = price.value.toInt(),
                categoryIndex = selectedCategory.intValue,
                village = village.value,

                imageUrl = if (selectedImageUri.value != null) {
                    imageToBase64(
                        selectedImageUri.value!!,
                        cv,
                        globalSettings.userSettingsData
                    )
                } else {
                    navData.imageUrl
                }
            ),
            onSaved = {
               // onSaved(

              //  )
                Log.d(
                    "MyLog",
                    "Add image64 size: , ${navData.imageUrl.toByteArray(Charsets.UTF_8).size}"
                )

            },
            onError = { error ->
                Log.d("MyLog4", "Error: ${error}")

            }
        )
    }*/

    fun saveBookToFirestore(
        firestore: FirebaseFirestore,
        book: Book,
        onSaved: () -> Unit,
        onError: (String) -> Unit
    ) {
        val db = firestore.collection(POSTS)
        val key = book.key.ifEmpty { db.document().id }
        db.document(key)
            .set(book.copy(key = key))
            .addOnSuccessListener { onSaved() }
            .addOnFailureListener { onError(it.message ?: "Error") }
        // Log.d("MyLog", "saveBookToFirestore: $book")
    }

    /*fun uploadBook(
        navData: NavRoutes.AddScreenObject
    ) {
        sendUiState(HomeViewModel.MainUiState.Loading)
        val book = Book(
            key = navData.key,
            title = title.value,
            description = description.value,
            price = price.value.toInt(),
            telephone = telephone.value,
            categoryIndex = selectedCategory.intValue,
            imageUrl = navData.imageUrl,
            village = navData.village
        )

        fireStoreManager.saveBookImage(
            oldImageUrl = navData.imageUrl,
            uri = selectedImageUri.value,
            book = book,
            onSaved = {
                sendUiState(HomeViewModel.MainUiState.Success)
            },
            onError = { message ->
                sendUiState(HomeViewModel.MainUiState.Error(message))
            }
        )
    }*/
}