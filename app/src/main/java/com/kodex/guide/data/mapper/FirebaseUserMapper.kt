package com.kodex.guide.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.kodex.guide.domain.model.User

fun FirebaseUser.toUser(): User {
    return User(
        uid = this.uid,
        email = this.email ?: "",
    )
}