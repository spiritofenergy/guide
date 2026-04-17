package com.kodex.guide.ui.db

import androidx.room.TypeConverter


class BookConverter {

    @TypeConverter
    fun fromRatingsList(ratings: List<Int>): String {
        return ratings.joinToString(",")
    }

    @TypeConverter
    fun toRatingsList(data: String): List<Int> {
        if (data.isEmpty()) return emptyList()
        return data.split(",").mapNotNull { it.toIntOrNull() }
    }
}