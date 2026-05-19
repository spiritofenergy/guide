package com.kodex.guide.data.repository

import com.google.firebase.ktx.Firebase
import com.kodex.guide.data.source.remote.FavoritesDataSource
import com.kodex.guide.domain.model.Favorite
import com.kodex.guide.domain.repository.FavoritesRepo
import javax.inject.Inject

class FavoritesFirebaseRepo_Impl @Inject constructor(
    private val dataSource: FavoritesDataSource
): FavoritesRepo {
    override suspend fun getIdsFavesList(): Result<List<String>> {
       return dataSource.getIdsFavesList()
    }

    override suspend fun onFavorites(
        favorite: Favorite,
        isFav: Boolean
    ): Result<Unit> {
        return dataSource.onFavorites(favorite, isFav)
    }
}