package com.kodex.guide.presentation.login.sign_up

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.data.repository.FirebaseAuthRepo_Impl
import com.kodex.guide.presentation.navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.kodex.guide.utils.firebase.FireStoreManagerPaging
import kotlinx.coroutines.launch


@HiltViewModel
class SingUpViewModel @Inject constructor(
    private val authRepo : FirebaseAuthRepo_Impl,
    private val fireStoreManager: FireStoreManagerPaging
) : ViewModel() {
    val errorState = mutableStateOf("")
    val successState = mutableStateOf(false)
    val emailState = mutableStateOf("nillsimon24@gmail.com")
    val passwordState = mutableStateOf("test2401")
    val nameState = mutableStateOf("")
    val phoneNumberState = mutableStateOf("")

     fun signUp(
        onSignUpSuccess: (NavRoutes.HomeDataObject) -> Unit
    ) = viewModelScope.launch{
           // emailState.value = ""
        val result = authRepo.signUp(emailState.value, passwordState.value)
         result.fold(
             onSuccess = { user ->
                onSignUpSuccess(NavRoutes.HomeDataObject(user.uid, user.email))
             },
             onFailure = { exception->
                 errorState.value = exception.message ?: "Unknown error"
             }
         )
    }
}