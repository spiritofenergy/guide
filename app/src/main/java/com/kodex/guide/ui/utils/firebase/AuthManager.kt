package com.kodex.guide.ui.utils.firebase

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kodex.guide.ui.data.NavRoutes
import javax.inject.Singleton

@Singleton
class AuthManager(
    private val auth: FirebaseAuth,
) {
    fun signUp(
        email: String,
        password: String,
        onSignUpSuccess: (NavRoutes.MainScreenDataObject) -> Unit,
        onSignUpFailure: (String) -> Unit,
    ) {
        if (email.isBlank() || password.isBlank()) {
            onSignUpFailure("Email snd Password be empty")
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSignUpSuccess(
                    NavRoutes.MainScreenDataObject(
                        task.result.user?.uid!!,
                        task.result.user?.email!!
                    )
                )
            }
            .addOnFailureListener() {
                onSignUpFailure(it.message ?: "Sign Up Error")
            }
    }

    fun signIn(
        email: String,
        password: String,
        onSignInSuccess: (NavRoutes.MainScreenDataObject) -> Unit,
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
                        NavRoutes.MainScreenDataObject(
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

    fun deleteAccount(
        email: String,
        password: String,
        onDeleteSuccess: () -> Unit,
        onDeleteFailure: (String) -> Unit,
    ) {
    val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser?.reauthenticate(credential)?.addOnSuccessListener {
            auth.currentUser?.delete()?.addOnSuccessListener {
                onDeleteSuccess()
            }?.addOnFailureListener { task ->
                onDeleteFailure(task.message ?: "Delete Account Error")
            }
        }?.addOnFailureListener { task->
            onDeleteFailure(task.message ?: "Re-Authenticate Account Error")
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }
}