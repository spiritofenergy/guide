package com.kodex.guide.ui.detailScreen

import kotlinx.serialization.Serializable

@Serializable
data class DetailNavObject(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val telephone: String = "",
    val categoryIndex: Int = 0,
    val imageUrl: String = ""
)
