package com.kodex.guide.ui.addscreen.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kodex.guide.ui.utils.Categories
import com.kodex.guide.ui.utils.Village

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
    val categoryIndex: Int = Categories.ANIMALS,
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
    val isAuthor: Boolean = false,
    val authorId: String = "",
    val publishDate: String = "",
    val timeStamp: Long = System.currentTimeMillis(),
    val deleteDate: String = "",
    val villageIndex: Int = Village.TAMAN,
    val delivery: Boolean = false,
    val ratingsList: List<Double> = emptyList(),

    )


