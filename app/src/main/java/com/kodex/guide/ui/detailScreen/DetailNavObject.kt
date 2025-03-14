package com.kodex.guide.ui.detailScreen

import kotlinx.serialization.Serializable

@Serializable
data class DetailNavObject(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val imageUrl: String = ""
)
