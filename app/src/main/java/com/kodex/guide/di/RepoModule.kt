package com.kodex.guide.di

import com.kodex.guide.data.repository.BooksRepo_Impl
import com.kodex.guide.data.repository.FavoritesFirebaseRepo_Impl
import com.kodex.guide.domain.repository.BooksRepo
import com.kodex.guide.domain.repository.FavoritesRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun bindBooksPepo(
        booksRepoImpl: BooksRepo_Impl
    ): BooksRepo

@Binds
    @Singleton
    abstract fun bindFavoritesRepo(
    favoritesRepoIpl: FavoritesFirebaseRepo_Impl
    ): FavoritesRepo
}