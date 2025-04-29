package com.kodex.guide.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.kodex.guide.ui.data.MainScreenDataObject
import com.kodex.guide.ui.utils.firebase.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.utils.StoreManager


@HiltViewModel
class LViewModel@Inject constructor(
    private val authManager: AuthManager,
    private val storeManager: StoreManager
): ViewModel() {
    val currentUser = mutableStateOf<FirebaseUser?>(null)
    val errorState = mutableStateOf("")
    val successState = mutableStateOf(false)
    val emailState = mutableStateOf("nillsimon24@gmail.com")
    val passwordState = mutableStateOf("test24")
    val resetPasswordState = mutableStateOf(false)
    val showResetPasswordDialog = mutableStateOf(false)

    fun signIn(
        onSignInSuccess: (MainScreenDataObject)-> Unit,
    ){
        errorState.value = ""
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
    fun getEmail(){
        emailState.value = storeManager.getString(StoreManager.EMAIL_KEY, "")
    }
    fun saveLastEmail(){
        storeManager.saveString(StoreManager.EMAIL_KEY, emailState.value)
    }
    fun signUp(
        onSignUpSuccess: (MainScreenDataObject)-> Unit,
    ){
        errorState.value = ""
        if (resetPasswordState.value){
            authManager.resetPassword(
                emailState.value,
                onResetPasswordSuccess = {
                    resetPasswordState.value = false
                   showResetPasswordDialog.value = true
                },
            onResetPasswordFailure = {errorMassage->
                errorState.value = errorMassage
                 }
            )
            return
        }
        authManager.signUp(
            emailState.value,
            passwordState.value,
            onSignUpSuccess = { navData->
                onSignUpSuccess(navData)
               // successState.value = true//?
            },
            onSignUpFailure = { errorMessage->
                errorState.value = errorMessage
            }
        )
    }
    fun getAccountState(){
        currentUser.value = authManager.getCurrentUser()
    }
        fun signOut(){
            authManager.signOut()
            currentUser.value = null
        }
}