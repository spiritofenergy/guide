package com.kodex.guide.di

import  android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.kodex.guide.data.source.remote.BooksFirebaseRemoteDataSource
import com.kodex.guide.data.source.remote.FavoritesDataSource
import com.kodex.guide.data.source.local.PreferenceDataSource
import com.kodex.guide.data.source.remote.FirebaseAuthDataSource
import com.kodex.guide.data.source.remote.ModerationDataSource
import com.kodex.guide.data.source.remote.UserSettingsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object   MainModule {


    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return Firebase.storage
    }
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
    fun provideModerationDataSource (
        db: FirebaseFirestore,
        auth: FirebaseAuth,
    ): ModerationDataSource {
        return ModerationDataSource(db, auth)
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
    ):PreferenceDataSource{
        return PreferenceDataSource(app)
    }
}