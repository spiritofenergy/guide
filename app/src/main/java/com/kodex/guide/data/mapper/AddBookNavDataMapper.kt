package com.kodex.guide.data.mapper

import com.kodex.guide.domain.model.Book
import com.kodex.guide.presentation.navigation.NavRoutes
import kotlin.String

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
        village = village,
        street = street,
        house = house,
        flat = flat,
        location = location,
        delivery = delivery, // <-- ДОБАВЬТЕ
        payment = payment,
        ratingsList = ratingsList.map { it.toInt() }
    )
}

fun Book.toAddScreenObject(): NavRoutes.AddScreenObject {
    return NavRoutes.AddScreenObject(
        id = id,
        key = this.key,
        title = this.title,
        searchTitle = searchTitle,
        description = this.description,
        price = this.price,
        telephone = this.telephone,
        categoryIndex = this.categoryIndex,
        imageUrl = this.imageUrl,
        isFavorite = this.isFavorite,
        isAuthor = this.isAuthor,
        authorId = this.authorId,
        publishPeriod = this.publishPeriod,
        timeStamp = this.timeStamp,
        deleteDate = this.deleteDate,
        village = this.village,
        street = this.street,
        house = this.house,
        flat = this.flat,
        location = this.location,// ← убедитесь, что это есть
        delivery = this.delivery,
        payment = this.payment,// ← убедитесь, что это есть
        ratingsList = this.ratingsList.map { it.toString() }
    )
}
