package com.kodex.guide.domain.tarif

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import javax.inject.Inject

data class AuthUser(
    val uid: String,
    val isAnonymous: Boolean,
    val email: String?
)

interface AuthStateProvider {
    fun currentUser(): AuthUser?
}

class FirebaseAuthStateProvider @Inject constructor() : AuthStateProvider {

    override fun currentUser(): AuthUser? {
        return Firebase.auth.currentUser?.let {
            AuthUser(
                uid = it.uid,
                isAnonymous = it.isAnonymous,
                email = it.email
            )
        }
    }
}