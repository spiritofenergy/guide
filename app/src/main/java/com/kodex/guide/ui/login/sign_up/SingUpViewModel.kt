package com.kodex.guide.ui.login.sign_up

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kodex.guide.ui.data.NavRoutes
import com.kodex.guide.ui.utils.firebase.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.kodex.guide.ui.utils.StoreManager


@HiltViewModel
class SingUpViewModel@Inject constructor(
    private val authManager: AuthManager,
    private val storeManager: StoreManager
): ViewModel() {
    val errorState = mutableStateOf("")
    val successState = mutableStateOf(false)
    val emailState = mutableStateOf("" +
           //' "nillsimon24@gmail.com" +
            "")
    val passwordState = mutableStateOf("test2401")
    val nameState = mutableStateOf("")
    val phoneNumberState = mutableStateOf("")

    fun signUp(
        onSignUpSuccess: (NavRoutes.MainScreenDataObject)-> Unit,
    ){
        errorState.value = ""
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
}