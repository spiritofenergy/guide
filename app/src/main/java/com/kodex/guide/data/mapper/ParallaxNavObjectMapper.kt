package com.kodex.guide.data.mapper

import com.kodex.guide.domain.model.Book
import com.kodex.guide.presentation.navigation.NavRoutes

fun Book.toParallaxNavObject(): NavRoutes.ParallaxNavObject {
    return NavRoutes.ParallaxNavObject(
        bookId = this.key,
        title = this.title,
        description = this.description,
        price = this.price,
        categoryIndex = this.categoryIndex,
        imageUrl = this.imageUrl,
        telephone = this.telephone,
        ratingsList = this.ratingsList,
        latitude = this.latitude ?: "55.7558",   // ← добавьте в Book
        longitude = this.longitude ?: "37.6173"  // ← добавьте в Book

        // все остальные поля возьмутся из дефолтных значений
    )
}