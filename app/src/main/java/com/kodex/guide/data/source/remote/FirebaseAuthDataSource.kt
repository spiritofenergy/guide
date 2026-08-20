package com.kodex.guide.data.source.remote

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kodex.guide.data.mapper.toUser
import com.kodex.guide.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataSource(
    private val auth: FirebaseAuth,
) {
    /** Текущий пользователь (может быть null) */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /** Поток изменений состояния авторизации */
    //  fun observeAuthState() = auth.authStateChanges()

    /** ✅ Анонимный вход  Нужно переделать функцию по Солид*/
    suspend fun signInAnonymously(): FirebaseUser {
        // await() — из kotlinx-coroutines-play-services
        val result = auth.signInAnonymously().await()
        return result.user ?: error("User is null after anonymous sign-in")
    }

    suspend fun signUp(email: String, password: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password)
                .await()
            Result.success(result.user?.toUser() ?: throw Exception("User not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password)
                .await()
            Result.success(result.user?.toUser() ?: throw Exception("User not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAccount(email: String, password: String): Result<Unit> {
        return try {
            val credential = EmailAuthProvider.getCredential(email, password)
            auth.currentUser?.reauthenticate(credential)?.await()
            auth.currentUser?.delete()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): User? {
        return auth.currentUser?.toUser()
    }

    fun signOut() {
        auth.signOut()
    }
}