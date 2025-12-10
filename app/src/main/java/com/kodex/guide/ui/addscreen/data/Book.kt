package com.kodex.guide.ui.addscreen.data

import com.kodex.guide.ui.utils.Categories

data class Book(
    val key: String = "",
    val title: String = "",
    val searchTitle: String = title.lowercase(),
    val description: String = "",
    val price: Int = 0,
    val telephone: String = "",
    val categoryIndex: Int = Categories.FANTASY,
    val imageUrl: String = "",
    val isFaves: Boolean = false,
   // val isAuthor: Boolean = false,
   // val publishDate: String = "",
  //  val location: String = ""

){

}
