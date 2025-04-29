package com.kodex.guide.ui.utils.firebase

import coil.compose.AsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kodex.guide.ui.data.MainScreenDataObject
import javax.inject.Singleton

@Singleton
class AuthManager(
    private val auth: FirebaseAuth,
) {
    fun signUp(
        email: String,
        password: String,
        onSignUpSuccess: (MainScreenDataObject) -> Unit,
        onSignUpFailure: (String) -> Unit,
    ) {
        if (email.isBlank() || password.isBlank()) {
            onSignUpFailure("Email snd Password be empty")
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSignUpSuccess
            }
            .addOnFailureListener() {
                onSignUpFailure(it.message ?: "Sign Up Error")
            }
    }

    fun signIn(
        email: String,
        password: String,
        onSignInSuccess: (MainScreenDataObject) -> Unit,
        onSignInFailure: (String) -> Unit,
    ) {
        if (email.isBlank() || password.isBlank()) {
            onSignInFailure("Email snd Password be empty")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    onSignInSuccess(
                        MainScreenDataObject(
                            task.result.user?.uid!!,
                            task.result.user?.email!!

                        )
                    )
            }
            .addOnFailureListener() {
                onSignInFailure(it.message ?: "Sign Up Error")
            }
    }

    fun resetPassword(
        email: String,
        onResetPasswordSuccess: () -> Unit,
        onResetPasswordFailure: (String) -> Unit,
    ) {
        if (email.isEmpty()) {
            onResetPasswordFailure("Email cannot be empty")
            return
        }
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResetPasswordSuccess()
                }
            }.addOnFailureListener{result->
                onResetPasswordFailure(result.message ?: "Result Password Error")
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }
    /*@Provides
    @Singleton
    fun provideFirebaseManager(
        auth: FirebaseAuth,
        db: FirebaseFirestore
    ): FireStoreManager{
        return FireStoreManager(auth, db)
    }

    @Provides
    @Singleton
    fun provideAuthManager(
        auth: FirebaseAuth,
    ): AuthManager{
        return AuthManager(auth)
    }*/
}