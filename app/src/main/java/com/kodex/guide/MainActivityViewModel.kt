package com.kodex.guide

import androidx.lifecycle.ViewModel
import com.kodex.guide.domain.repository.UserSettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
        private val userSettingsRepo: UserSettingsRepo
): ViewModel() {
    suspend fun updateLastVisit(){
        userSettingsRepo.updateLastVisit()
    }
}