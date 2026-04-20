package com.kodex.guide.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.data.source.remote.FirebaseAuthDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.kodex.guide.ui.utils.StoreManager


@HiltViewModel
class LoginViewModel@Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val storeManager: StoreManager
): ViewModel() {
    val currentUser = mutableStateOf<FirebaseUser?>(null)
    val errorState = mutableStateOf("")
    val successState = mutableStateOf(false)
    val emailState = mutableStateOf("" +
           //' "nillsimon24@gmail.com" +
            "")
    val passwordState = mutableStateOf("test2401")
    val resetPasswordState = mutableStateOf(false)
    val showResetPasswordDialog = mutableStateOf(false)

    fun signIn(
        onSignInSuccess: (NavRoutes.HomeDataObject)-> Unit,
    ){
        errorState.value = ""
        firebaseAuthDataSource.signIn(
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
    fun resetPassword() {
        errorState.value = ""
        firebaseAuthDataSource.resetPassword(
            emailState.value,
            onResetPasswordSuccess = {
                resetPasswordState.value = false
                showResetPasswordDialog.value = true
            },
            onResetPasswordFailure = { errorMassage ->
                errorState.value = errorMassage
            }
        )
    }
    fun getAccountState(){
        currentUser.value = firebaseAuthDataSource.getCurrentUser()
    }
        fun signOut(){
            firebaseAuthDataSource.signOut()
            currentUser.value = null
        }
}