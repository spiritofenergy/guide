package com.kodex.guide.data.mapper

import com.kodex.guide.domain.model.Book
import com.kodex.guide.presentation.navigation.NavRoutes

fun NavRoutes.AddScreenObject.toDomain(): Book {
    return Book(
        id = id, // <-- ДОБАВЬТЕ (иначе создается новая запись)
        key = key,
        title = title,
        searchTitle = title.lowercase(),
        description = description,
        price = price,
        telephone = telephone, // <-- ДОБАВЬТЕ
        categoryIndex = categoryIndex,
        imageUrl = imageUrl,
        isFavorite = isFavorite, // <-- ДОБАВЬТЕ
        isAuthor = isAuthor, // <-- ДОБАВЬТЕ
        authorId = authorId, // <-- ДОБАВЬТЕ
        publishPeriod = publishPeriod, // <-- ДОБАВЬТЕ
        timeStamp = timeStamp, // <-- ДОБАВЬТЕ
        deleteDate = deleteDate,// <-- ДОБАВЬТЕ
        village = village, // <-- ДОБАВЬТЕ
        delivery = delivery, // <-- ДОБАВЬТЕ
        ratingsList = ratingsList.map { it.toInt() }
        //ratingsList = ratingsList // <-- ДОБАВЬТЕ
    )
}