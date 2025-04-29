package com.kodex.guide.ui.addscreen.data

import com.kodex.guide.ui.utils.Categories
import kotlinx.serialization.Serializable

@Serializable
data class AddScreenObject(
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val telephone: String = "",
    val categoryIndex: Int = Categories.FANTASY,
    val imageUrl: String = ""
)

