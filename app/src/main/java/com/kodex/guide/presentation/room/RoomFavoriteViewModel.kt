package com.kodex.guide.presentation.room

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.domain.model.Book
import com.kodex.guide.ui.db.MainDb
import com.kodex.guide.domain.model.BookCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomFavoriteViewModel @Inject constructor(
    private val mainDb: MainDb
) : ViewModel() {
    val postList = mainDb.roomDao.getAllPosts()
    var postToDelete: Book? = null

    val categoryState = mutableIntStateOf(BookCategories.ALL.id)
    val isAdminState = mutableStateOf(false)


    fun deletePost() = viewModelScope.launch(Dispatchers.IO) {
        postToDelete?.let{ mainDb.roomDao.deletePost(it) }
    }

}