package com.kodex.guide.data.mapper

import com.kodex.guide.data.model.RatingDataDTO
import com.kodex.guide.domain.model.RatingData
import kotlin.Int

fun RatingDataDTO.toRatingData(): RatingData {
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
fun RatingData.toDTO  (): RatingDataDTO {
    return RatingDataDTO(
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