package com.kodex.guide.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ratingData")
data class RatingDataDTO (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String  = "",
    val uid: String  = "",
    val rating: Int? = null,

    val lastRating: Int = 0,
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val bookId: String = "",
   // val likes: Int,
)