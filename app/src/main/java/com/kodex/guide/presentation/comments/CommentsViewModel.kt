package com.kodex.guide.presentation.comments

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.domain.repository.BooksRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val booksRepo: BooksRepo
): ViewModel() {

    val commentsState = mutableStateOf(emptyList<RatingData>())

     fun getBookComments(bookId: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = booksRepo.getBookComments(bookId)
        result.fold(
            onSuccess = { commentsList ->
                commentsState.value = commentsList
            },
            onFailure = { error ->

            }
        )
    }
}