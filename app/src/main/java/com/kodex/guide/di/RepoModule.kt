package com.kodex.guide.di

import com.kodex.guide.data.repository.BooksRepo_Impl
import com.kodex.guide.data.repository.FavoritesFirebaseRepoImpl
import com.kodex.guide.data.repository.FirebaseAuthRepoImpl
import com.kodex.guide.data.repository.ModerationRepoImpl
import com.kodex.guide.data.repository.UserSettingsRepoImpl
import com.kodex.guide.data.repository.UserAccessRepoImpl
import com.kodex.guide.data.repository.UserRoleRepoImpl
import com.kodex.guide.domain.repository.AuthRepo
import com.kodex.guide.domain.repository.BooksRepo
import com.kodex.guide.domain.repository.FavoritesRepo
import com.kodex.guide.domain.repository.ModerationRepo
import com.kodex.guide.domain.repository.UserAccessRepo
import com.kodex.guide.domain.repository.UserRoleRepo
import com.kodex.guide.domain.repository.UserSettingsRepo
import com.kodex.guide.domain.role.DefaultRolePermissionChecker
import com.kodex.guide.domain.role.RolePermissionChecker
import com.kodex.guide.domain.tarif.AuthStateProvider
import com.kodex.guide.domain.tarif.DefaultTariffPolicy
import com.kodex.guide.domain.tarif.FirebaseAuthStateProvider
import com.kodex.guide.domain.tarif.TariffPolicy
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindUserAccessRepository(
        impl: UserAccessRepoImpl
    ): UserAccessRepo

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: FirebaseAuthRepoImpl
    ): AuthRepo

    @Binds
    abstract fun bindTariffPolicy(
        impl: DefaultTariffPolicy
    ): TariffPolicy

    @Binds
    abstract fun bindAuthStateProvider(
        impl: FirebaseAuthStateProvider
    ): AuthStateProvider

    @Binds
    abstract fun bindUserRoleRepository(
        impl: UserRoleRepoImpl
    ): UserRoleRepo

    @Binds
    abstract fun bindRolePermissionChecker(
        impl: DefaultRolePermissionChecker
    ): RolePermissionChecker

    @Binds
    @Singleton
    abstract fun bindBooksPepo(
        booksRepoImpl: BooksRepo_Impl
    ): BooksRepo

    @Binds
    @Singleton
    abstract fun bindModerationPepo(
        moderationRepoInfo: ModerationRepoImpl
    ): ModerationRepo

    @Binds
    @Singleton
    abstract fun bindFavoritesRepo(
        favoritesRepoImpl: FavoritesFirebaseRepoImpl
    ): FavoritesRepo

    @Binds
    @Singleton
    abstract fun bindUserSettingsRepo(
        userSettingsRepo: UserSettingsRepoImpl
    ): UserSettingsRepo
}