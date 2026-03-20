package com.kodex.guide.ui.detailScreen

import com.kodex.guide.ui.utils.Categories
 import kotlinx.serialization.Serializable

@Serializable
data class DetailNavObject(
    val bookId: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val telephone: String = "",
    val categoryIndex: Int = Categories.ALL,
    val imageUrl: String = "",
    val author: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFaves: Boolean = false,
    val ratingsList: List<Int> = emptyList()

)
