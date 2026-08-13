package com.kodex.guide.data.model

import com.google.firebase.firestore.FieldValue
import com.kodex.guide.domain.model.UserRole

data class UserDTO(
    val uid: String = "",
    val email: String? = null,
    val role: UserRole = UserRole.USER,
    val isAnonymous: Boolean = false,
    val isRegistered: Boolean,
    val userName: String? = null,
    val createdAt: FieldValue = FieldValue.serverTimestamp(),
    val updatedAt: FieldValue = FieldValue.serverTimestamp()
)
