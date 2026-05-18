package com.kodex.guide.presentation.home

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.domain.model.Book
import com.kodex.guide.ui.db.MainDb
import com.kodex.guide.utils.Categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val mainDb: MainDb
) : ViewModel() {
    val trackList = mainDb.trackDao.getAllTracks()
    var trackToDelete: Book? = null

    val categoryState = mutableIntStateOf(Categories.ALL)
    val isAdminState = mutableStateOf(false)


    fun deleteTrack() = viewModelScope.launch(Dispatchers.IO) {
        trackToDelete?.let{ mainDb.trackDao.deleteTracks(it) }
    }

}