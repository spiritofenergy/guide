package com.kodex.guide.ui.addscreen.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kodex.guide.ui.utils.Categories

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val key: String = "",
    val title: String = "",
    val searchTitle: String = title.lowercase(),
    val description: String = "",
    val price: Int = 0,
    val telephone: String = "",
    val category: Int = Categories.ALL,
    val imageUrl: String = "",
    val isFaves: Boolean = false,
   // val isAuthor: Boolean = false,
   // val publishDate: String = "",
  //  val location: String = ""

){

}
