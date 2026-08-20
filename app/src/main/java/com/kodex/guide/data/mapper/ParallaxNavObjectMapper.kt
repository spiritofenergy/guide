package com.kodex.guide.data.mapper

import com.kodex.guide.domain.model.Book
import com.kodex.guide.presentation.navigation.NavRoutes

fun Book.toParallaxNavObject(): NavRoutes.ParallaxNavObject {
    return NavRoutes.ParallaxNavObject(
        bookId = key,
        title = title,
        description = description,
        price = price,
        village = village,
        street = street,
        house = house,
        flat = flat,
        categoryIndex = categoryIndex,
        imageUrl = imageUrl,
        telephone = telephone,
        payment = payment,
        delivery = delivery,
        ratingsList = ratingsList,
        latitude = latitude ?: "55.7558",   // ← добавьте в Book
        longitude = longitude ?: "37.6173"  // ← добавьте в Book

        // все остальные поля возьмутся из дефолтных значений
    )
}