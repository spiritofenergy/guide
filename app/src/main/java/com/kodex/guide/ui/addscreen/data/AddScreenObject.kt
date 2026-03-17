package com.kodex.guide.ui.addscreen.data

import com.kodex.guide.ui.utils.Categories
import kotlinx.serialization.Serializable

@Serializable
data class AddScreenObject(
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val price: Int = 0,
    val telephone: String = "",
    val categoryIndex: Int = Categories.MISCELLANEOUS,
    val imageUrl: String = "",

    val isFavorite: Boolean = false,
    val isAuthor: Boolean = false,
    val authorId: String = "",
    val timeStamp: Long = System.currentTimeMillis(),
    val deleteDate: String = "",
    val location: String = "",
    val delivery: Boolean = false,
    val ratingsList: List<Double> = emptyList(),
)

