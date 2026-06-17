package com.kodex.guide.presentation.admin_panel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.data.source.remote.BooksFirebaseRemoteDataSource
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.domain.repository.ModerationRepo

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModerationScreenViewModel @Inject constructor(
    private val moderationRepo: ModerationRepo
) : ViewModel(){

    val  commentState  = mutableStateOf(emptyList<RatingData>())

    fun acceptComment(ratingData: RatingData) = viewModelScope.launch(){
       val result = moderationRepo.acceptComment(ratingData)
        result.fold(
            onSuccess = {
                commentState.value = commentState.value.filter { it.uid != ratingData.uid }
            },
            onFailure = {

                Log.d("MyLog", "Accept error: ${it.message}")
            }
        )
    }
    fun deleteComment(uid: String) = viewModelScope.launch{
        val result = moderationRepo.deleteComment(uid)
        result.fold(
            onSuccess = {
                commentState.value = commentState.value.filter { it.uid != uid }
            },
            onFailure = {

            }
        )

    }
    fun getAllComments () = viewModelScope.launch{
        val result = moderationRepo.getCommentsToModerate()
        result.fold(
            onSuccess = { commentLost ->
                commentState.value = commentLost
            },
            onFailure = {

            }
        )
    }
}