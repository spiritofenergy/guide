package com.kodex.guide.domain.repository

import com.kodex.guide.domain.model.Favorite

interface FavoritesRepo {
    suspend fun getIdsFavesList(): Result<List<String>>
    suspend fun onFavorites(favorite: Favorite, isFav: Boolean) : Result<Unit>
}