package com.kodex.guide.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kodex.guide.ui.data.MainScreenDataObject
import com.kodex.guide.ui.utils.firebase.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LViewModel@Inject constructor(
    private val authManager: AuthManager
): ViewModel() {

    val errorState = mutableStateOf("")
    val emailState = mutableStateOf("nillsimon24@gmail.com")
    val passwordState = mutableStateOf("test24")

    fun signIn(
        onSignInSuccess: (MainScreenDataObject)-> Unit,
    ){
        authManager.signIn(
            emailState.value,
            passwordState.value,
            onSignInSuccess = { navData->
                onSignInSuccess(navData)
            },
            onSignInFailure = { errorMessage->
                errorState.value = errorMessage
            }
        )
    }
    fun signUp(
        onSignUpSuccess: (MainScreenDataObject)-> Unit,
    ){
        authManager.signUp(
            emailState.value,
            passwordState.value,
            onSignUpSuccess = { navData->
                onSignUpSuccess(navData)
            },
            onSignUpFailure = { errorMessage->
                errorState.value = errorMessage
            }
        )
    }
}