package com.kodex.guide.presentation.home

data class ChangedTempBook(
    val key: String,
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
)
