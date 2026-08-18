package com.kodex.guide.presentation.home

sealed interface SavedUiEvent {
    data class ShowToast(val message: String) : SavedUiEvent

}