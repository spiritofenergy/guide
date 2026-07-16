package com.kodex.guide.data.mapper

import com.kodex.guide.domain.model.Book
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.presentation.navigation.NavRoutes.AddScreenObject

fun NavRoutes.AddScreenObject.toDomain(): Book {
    return Book(
        key = key,
        title = title,
        description = description,
        price = price,
        categoryIndex = categoryIndex,
        imageUrl = imageUrl


    )
}