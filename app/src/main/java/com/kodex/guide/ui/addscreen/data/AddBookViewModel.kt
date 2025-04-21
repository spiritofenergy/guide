package com.kodex.guide.ui.addscreen.data

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.kodex.guide.ui.utils.firebase.FireStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBookViewModal @Inject constructor(
    private val fireStoreManager: FireStoreManager,
) : ViewModel() {
}
val title = mutableStateOf("")
val description = mutableStateOf("")
val prise = mutableStateOf("")
val selectedImageUri = mutableStateOf<Uri?>(null)

fun setDefaultData(navData: AddScreenObject){
    title.value = navData.title
    description.value = navData.description
    prise.value = navData.price
}