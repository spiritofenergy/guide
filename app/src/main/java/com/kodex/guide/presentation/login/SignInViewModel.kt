package com.kodex.guide.presentation.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.data.repository.FirebaseAuthRepo_Impl
import com.kodex.guide.domain.model.User
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.data.source.local.PreferenceDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel@Inject constructor(
    private val authRepo: FirebaseAuthRepo_Impl,
    private val preferenceDataSource: PreferenceDataSource
): ViewModel() {
    val currentUser = mutableStateOf<User?>(null)
    val errorState = mutableStateOf("")
    val successState = mutableStateOf(false)
    val emailState = mutableStateOf(
        "" +
                //' "nillsimon24@gmail.com" +
                ""
    )
    val passwordState = mutableStateOf("test2401")
    val resetPasswordState = mutableStateOf(false)
    val showResetPasswordDialog = mutableStateOf(false)

    fun signIn(
        onSignInSuccess: (NavRoutes.HomeDataObject)-> Unit,
    ) = viewModelScope.launch(Dispatchers.IO){
        errorState.value = ""
        val result = authRepo.signIn(emailState.value, passwordState.value)
        result.fold(
            onSuccess = { user ->
                withContext(Dispatchers.Main){
                    onSignInSuccess(NavRoutes.HomeDataObject(user.uid, user.email))
                }

            },
            onFailure = { exception->
                errorState.value = exception.message ?: "Unknown error"
            }
        )
    }
    fun getEmail(){
        emailState.value = preferenceDataSource.getEmail(PreferenceDataSource.EMAIL_KEY, "")
    }
    fun saveLastEmail(){
        preferenceDataSource.saveEmail(PreferenceDataSource.EMAIL_KEY, emailState.value)
    }
     fun resetPassword() = viewModelScope.launch(Dispatchers.IO) {
        errorState.value = ""
         val result = authRepo.signUp(emailState.value, passwordState.value)
        result.fold(
            onSuccess = {
                resetPasswordState.value = false
                showResetPasswordDialog.value = true
             },
            onFailure = { exception->
                errorState.value = exception.message ?: "Unknown error"
            }
        )
    }
  fun getAccountState(){
         currentUser.value = authRepo.getCurrentUser()
    }
        fun signOut(){
            authRepo.signOut()
            currentUser.value = null
        }
}