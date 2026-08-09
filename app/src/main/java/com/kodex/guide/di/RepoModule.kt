package com.kodex.guide.di

import com.google.firebase.database.core.RepoInfo
import com.kodex.guide.data.repository.BooksRepo_Impl
import com.kodex.guide.data.repository.FavoritesFirebaseRepo_Impl
import com.kodex.guide.data.repository.FirebaseAuthRepo_Impl
import com.kodex.guide.data.repository.ModerationRepo_Impl
import com.kodex.guide.data.repository.UserSettingsRepo_Impl
import com.kodex.guide.domain.repository.AuthRepo
import com.kodex.guide.domain.repository.BooksRepo
import com.kodex.guide.domain.repository.FavoritesRepo
import com.kodex.guide.domain.repository.ModerationRepo
import com.kodex.guide.domain.repository.UserSettingsRepo
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
    abstract fun bindAuthRepository(impl: FirebaseAuthRepo_Impl): AuthRepo

    @Binds
    @Singleton
    abstract fun bindBooksPepo(
        booksRepoImpl: BooksRepo_Impl
    ): BooksRepo

    @Binds
    @Singleton
    abstract fun bindModerationPepo(
        moderationRepoInfo: ModerationRepo_Impl
    ): ModerationRepo

    @Binds
    @Singleton
    abstract fun bindFavoritesRepo(
        favoritesRepoImpl: FavoritesFirebaseRepo_Impl
    ): FavoritesRepo

    @Binds
    @Singleton
    abstract fun bindUserSettingsRepo(
        userSettingsRepo: UserSettingsRepo_Impl
    ): UserSettingsRepo
}