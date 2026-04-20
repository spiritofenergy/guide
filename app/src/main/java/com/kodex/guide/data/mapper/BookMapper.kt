package com.kodex.guide.data.mapper

import com.kodex.guide.data.model.BookDTO
import com.kodex.guide.domain.model.Book

fun BookDTO.toBook(): Book {
    return Book(
     id = id,
     key = key,
     title = title,
     searchTitle = title.lowercase(),
     description = description,
     price = price,
     telephone  = telephone,
     categoryIndex = categoryIndex,
     imageUrl  = imageUrl,
     isFavorite = isFavorite,
     isAuthor  = isAuthor,
     authorId  = authorId,
     publishPeriod = publishPeriod,
     timeStamp = timeStamp,
     deleteDate = deleteDate,
     village = village,
     ratingsList = ratingsList,
    )
}