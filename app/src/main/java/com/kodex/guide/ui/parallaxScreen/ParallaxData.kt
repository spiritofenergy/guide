package com.kodex.guide.ui.parallaxScreen

data class ParallaxData (
    val id: String,
    val name: String,
    val address: String,
    val rating: Double,
    val reviewsCount: Int,
    val priceLevel: String,
    val photos: List<String>,
    val isOpenNow: Boolean,
    val openingHours: String,
    val phone: String,
    val website: String,
    val amenities: List<String>,
    val latitude: Double,
    val longitude: Double
)