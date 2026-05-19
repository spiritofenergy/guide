package com.kodex.guide.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.kodex.guide.data.source.remote.FirebaseAuthDataSource
import com.kodex.guide.domain.model.User
import com.kodex.guide.domain.repository.AuthRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepo_Impl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
): AuthRepo {
    override suspend fun signUp(
        email: String,
        password: String
    ): Result<User> {
        return firebaseAuthDataSource.signUp(email, password)
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<User> {
        return firebaseAuthDataSource.signIn(email, password)
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return  firebaseAuthDataSource.resetPassword(email)
    }

    override suspend fun deleteAccount(
        email: String,
        password: String
    ): Result<Unit> {
        return firebaseAuthDataSource.deleteAccount(email, password)
    }

    override fun getCurrentUser(): User? {
        return firebaseAuthDataSource.getCurrentUser()
    }
    override fun signOut(){
        firebaseAuthDataSource.signOut()
    }
}