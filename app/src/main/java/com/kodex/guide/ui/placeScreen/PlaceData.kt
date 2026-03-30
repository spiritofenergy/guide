package com.kodex.guide.ui.placeScreen

data class PlaceData(
    val id: String,
    val title: String,
    val description: String,
    val categoryIndex: Int,
    val price: String,
    val rating: Float,
    val address: String,
    val isOpen: Boolean,
    val workTime: String,
    val contact: String,
    val telephone: String,
    val site: String,
    val images: List<String>,
    val isFavorite: Boolean = false
)
