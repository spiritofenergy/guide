package com.kodex.guide.data.mapper

import com.kodex.guide.domain.model.Favorite

fun Favorite.toFavorite(): Favorite{
    return Favorite(
            key = key
    )
}