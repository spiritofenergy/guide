package com.kodex.guide.ui.detailScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.ui.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging
) : ViewModel() {
  //  val ratingState = mutableStateOf("0")
    val commentState = mutableStateOf(emptyList<RatingData>())
    val ratingDataState = mutableStateOf<RatingData?>(RatingData())

    fun insertRating(ratingData: RatingData, bookId: String) {
        fireStoreManager.insertUserRating(ratingData, bookId)
    }
    fun getBookComments(bookId: String) = viewModelScope.launch{
        commentState.value = fireStoreManager.getBookComments(bookId)
    }
    fun getUserRating(bookId: String) = viewModelScope.launch {
        ratingDataState.value = fireStoreManager.getUserRating(bookId)

    }

}