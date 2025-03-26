package com.kodex.guide.ui.utils.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.ui.data.MainScreenDataObject
import dagger.Provides
import javax.inject.Singleton

@Singleton
class AuthManager(
    private val auth: FirebaseAuth
) {
    fun signUp(
        email: String,
        password: String,
        onSignUpSuccess: (MainScreenDataObject)-> Unit,
        onSignUpFailure: (String)-> Unit
    ){
        if (email.isBlank()|| password.isBlank()){
            onSignUpFailure("Email snd Password be empty")
            return
        }
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if(task.isSuccessful)onSignUpSuccess
            }
            .addOnFailureListener(){
                onSignUpFailure(it.message ?: "Sign Up Error")
            }
    }

    fun signIn(
        email: String,
        password: String,
        onSignInSuccess: (MainScreenDataObject)-> Unit,
        onSignInFailure: (String)-> Unit
    ){
        if (email.isBlank()|| password.isBlank()){
            onSignInFailure("Email snd Password be empty")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    onSignInSuccess(MainScreenDataObject(
                        task.result.user?.uid!!,
                        task.result.user?.email!!

                    ))
            }
            .addOnFailureListener() {
                onSignInFailure(it.message ?: "Sign Up Error")
            }
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