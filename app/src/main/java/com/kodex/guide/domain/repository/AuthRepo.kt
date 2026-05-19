package com.kodex.guide.domain.repository

import com.kodex.guide.domain.model.User

interface AuthRepo {
    suspend fun signUp(email: String, password: String): Result<User>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun resetPassword(email: String, ): Result<Unit>
    suspend fun deleteAccount(email: String, password: String ): Result<Unit>
    fun getCurrentUser(): User?
    fun signOut()
}