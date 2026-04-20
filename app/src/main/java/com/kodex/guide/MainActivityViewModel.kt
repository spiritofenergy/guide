package com.kodex.guide

import androidx.lifecycle.ViewModel
import com.kodex.guide.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
        private val fireStoreManager: FireStoreManagerPaging
): ViewModel() {
    fun updateLastVisit(){
        fireStoreManager.updateLastVisit()
    }
}