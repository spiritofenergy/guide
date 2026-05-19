package com.kodex.guide.di

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kodex.guide.data.source.remote.BooksFirebaseRemoteDataSource
import com.kodex.guide.data.source.remote.FavoritesDataSource
import com.kodex.guide.utils.StoreManager
import com.kodex.guide.data.source.remote.FirebaseAuthDataSource
import com.kodex.guide.data.source.remote.UserSettingsDataSource
import com.kodex.guide.utils.firebase.FireStoreManagerPaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton

    fun provideFirebaseDataSource (
        db: FirebaseFirestore,
        auth: FirebaseAuth,
    ): BooksFirebaseRemoteDataSource{
        return BooksFirebaseRemoteDataSource(db, auth)
    }

    @Provides
    @Singleton
    fun provideFavoritesDataSource (
        db: FirebaseFirestore,
        auth: FirebaseAuth,
    ): FavoritesDataSource{
        return FavoritesDataSource(db, auth)
    }

    @Provides
    @Singleton
    fun provideUserSettingsDataSource (
        db: FirebaseFirestore,
        auth: FirebaseAuth,
    ): UserSettingsDataSource {
        return UserSettingsDataSource(db, auth)
    }
    @Provides
    @Singleton
    fun provideFirebaseManager (
        db: FirebaseFirestore,
        auth: FirebaseAuth,
    ): FireStoreManagerPaging{
        return FireStoreManagerPaging(db, auth)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth{
        return Firebase.auth
    }
    @Provides
    @Singleton
    fun provideFirebaseStore(): FirebaseFirestore{
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideAuthManager(
        auth: FirebaseAuth
    ): FirebaseAuthDataSource{
        return FirebaseAuthDataSource(auth)
    }

    @Provides
    @Singleton
    fun provideStoreManager(
        app: Application
    ):StoreManager{
        return StoreManager(app)
    }
}