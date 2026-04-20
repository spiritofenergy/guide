package com.kodex.guide.presentation.navigation

import com.kodex.guide.ui.utils.Categories
import kotlinx.serialization.Serializable

@Serializable
class NavRoutes {
    @Serializable
    data class HomeDataObject(
        val uid: String = "",
        val email: String = ""
    )

    @Serializable
    object AdminPanelNavObject {
    }

    @Serializable
    object ModerationNavObject

    @Serializable
    object LoginNavObject

    @Serializable
    object SingUpNavObject
    @Serializable
    object SettingsNavObject
    @Serializable
    data class ParallaxNavObject(
        val bookId: String = "",
        val title: String = "",
        val description: String = "",
        val price: Int = 0,
        val isOpenNow: Boolean = true,
        val openingHours: String = "09:00 - 20:00",
        val address: String = "Москва, ул. Тверская, 15",
        val telephone: String = "+7(495)123-45-67",
        val website: String = "coffeehouse.ru",
        val latitude: String = "9",
        val longitude:String = "22",
        val categoryIndex: Int = Categories.ALL,
        val imageUrl: String = "",
        val author: String = "",
        val timestamp: Long = System.currentTimeMillis(),
        val isFaves: Boolean = false,
        val ratingsList: List<Int> = emptyList()
    )
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
    @Serializable
    data class PlaceNavObject(
        val bookId: String = "",
        val title: String = "Уютный уголок",
        val description: String = "Уютная кофейня в центре города. Мы предлагаем свежую выпечку, авторский кофе и приятную атмосферу для работы и встреч с друзьями. У нас есть бесплатный Wi-Fi, розетки и вежливый персонал.",
        val price: Int = 0,
        val categoryIndex: Int = Categories.ALL,
        val imageUrl: String = "",
        val author: String = "",
        val address: String = "ул. Центральная, 15, Москва",
        val isOpen: Boolean = true,
        val workTime: String = "09:00 - 22:00",
        val contact: String = "info@cozyplace.ru",
        val telephone: String = "+7 (999) 123-45-67",
        val site: String = "https://cozyplace.ru",
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