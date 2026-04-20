package com.kodex.guide.presentation.admin_panel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.ui.utils.firebase.FireStoreManagerPaging

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModerationScreenViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging
) : ViewModel(){

    val  commentState  = mutableStateOf(emptyList<RatingData>())

    fun insertModerationRating(ratingData: RatingData) = viewModelScope.launch(){
        fireStoreManager.deleteComment(ratingData.uid)
        commentState.value = commentState.value.filter { it.uid != ratingData.uid }
        fireStoreManager.insertModerationRating(ratingData)
    }
    fun deleteComment(uid: String) = viewModelScope.launch{
        fireStoreManager.deleteComment(uid)
        commentState.value = commentState.value.filter { it.uid != uid }
    }
    fun getAllComments () = viewModelScope.launch{
        commentState.value = fireStoreManager.getCommentsToModerate()
    }

}