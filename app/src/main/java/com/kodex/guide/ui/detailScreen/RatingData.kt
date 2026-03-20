package com.kodex.guide.ui.detailScreen

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ratingData")
data class RatingData (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String  = "",
    val uid: String  = "",
    val rating: Int = 1,
    val lastRating: Int = 0,
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val bookId: String = ""
)