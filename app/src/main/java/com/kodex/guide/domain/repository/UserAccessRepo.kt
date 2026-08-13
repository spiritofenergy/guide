package com.kodex.guide.domain.repository

interface UserAccessRepo {
    suspend fun isAdmin(uid: String): Boolean
    suspend fun isRegistered(uid: String): Boolean
}