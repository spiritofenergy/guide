package com.kodex.guide.ui.detailScreen

import com.kodex.guide.ui.utils.Categories
import kotlinx.serialization.Serializable

@Serializable
data class DetailNavObject(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val telephone: String = "",
    val category: Int = Categories.ALL,
    val imageUrl: String = ""
)
