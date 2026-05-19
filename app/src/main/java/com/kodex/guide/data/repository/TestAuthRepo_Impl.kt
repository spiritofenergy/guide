package com.kodex.guide.data.repository

import com.kodex.guide.domain.model.User
import com.kodex.guide.domain.repository.AuthRepo

class TestAuthRepo_Impl: AuthRepo {
    override suspend fun signUp(
        email: String,
        password: String
    ): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(
        email: String,
        password: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): User? {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}