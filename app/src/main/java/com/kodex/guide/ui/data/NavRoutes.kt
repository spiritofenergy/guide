package com.kodex.guide.ui.data

import com.kodex.guide.ui.utils.Categories
import kotlinx.serialization.Serializable

@Serializable
class NavRoutes {
    @Serializable
    data class MainScreenDataObject(
        val uid: String = "",
        val email: String = ""
    )

    @Serializable
    object AdminPanelNavObject {
    }

    @Serializable
    object ModerationNavObject

    @Serializable
    object LoginScreenObject
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
    fun DetailNavObject.toCommentsNavData(): CommentsNavData {
        return CommentsNavData(
            bookId = bookId,
            title = title,
            ratingsList = ratingsList
        )
    }


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
        val authorId: Int = 0,
        val publishPeriod: Int = 1,
        val timeStamp: Long = System.currentTimeMillis(),
        val deleteDate: String = "",
        val village: String = "",
        val delivery: Boolean = false,
        val ratingsList: List<Double> = emptyList(),
    )

    @Serializable
    data class CommentsNavData (
        val bookId: String = "",
        val title: String = "",
        val ratingsList: List<Int> = emptyList(),

        )

}