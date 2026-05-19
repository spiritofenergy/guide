package com.kodex.guide.data.mapper

import com.kodex.guide.domain.model.RatingData
import kotlin.Int

fun RatingData.toRatingData(): RatingData {
    return RatingData(
        id = id,
        name = name,
        uid = uid,
        rating = rating,
        lastRating = lastRating,
        message = message,
        timestamp = timestamp,
        bookId = bookId,
    )
}