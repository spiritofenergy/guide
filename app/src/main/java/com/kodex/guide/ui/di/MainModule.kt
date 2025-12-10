package com.kodex.guide.ui.di

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.kodex.guide.ui.utils.StoreManager
import com.kodex.guide.ui.utils.firebase.AuthManager
import com.kodex.guide.ui.utils.firebase.FireStoreManagerPaging
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

    fun provideFirebasePagingManager(
        db: FirebaseFirestore,
        auth: FirebaseAuth,
        app: Application
    ): FireStoreManagerPaging{
        return FireStoreManagerPaging(
            db, auth

        )
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
    ): AuthManager{
        return AuthManager(auth)
    }

    @Provides
    @Singleton
    fun provideStoreManager(
        app: Application
    ):StoreManager{
        return StoreManager(app)
    }
}