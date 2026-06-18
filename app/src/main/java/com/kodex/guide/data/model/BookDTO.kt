package com.kodex.guide.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kodex.guide.domain.model.BookCategories

@Entity(tableName = "books")
data class BookDTO(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val key: String = "",
    val title: String = "",
    val searchTitle: String = title.lowercase(),
    val description: String = "",
    val price: Int = 0,
    val telephone: String = "",
    val categoryIndex: Int = BookCategories.ALL.id,
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
    val isAuthor: Boolean = false,
    val authorId: Int = 0,
    val publishPeriod: Int = 1,
    val timeStamp: Long = System.currentTimeMillis(),
    val deleteDate: Int = 0,
    val village: String = "",
    val delivery: Boolean = false,
    val ratingsList: List<Int> = emptyList(),

    )