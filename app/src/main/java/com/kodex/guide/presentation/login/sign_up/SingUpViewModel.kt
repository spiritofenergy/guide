package com.kodex.guide.presentation.login.sign_up

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.data.repository.FirebaseAuthRepo_Impl
import com.kodex.guide.data.source.remote.UserSettingsDataSource
import com.kodex.guide.domain.model.PersonalData
import com.kodex.guide.presentation.navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.kodex.guide.utils.firebase.FireStoreManagerPaging
import kotlinx.coroutines.launch


@HiltViewModel
class SingUpViewModel @Inject constructor(
    private val authRepo : FirebaseAuthRepo_Impl,
    private val fireStoreManager: FireStoreManagerPaging,
    private val userSettingsDataSource: UserSettingsDataSource
) : ViewModel() {
    val errorState = mutableStateOf("")
    val successState = mutableStateOf(false)
    val emailState = mutableStateOf("")
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
                 userSettingsDataSource.insertPersonalData(
                     personalData = PersonalData(
                         nameState.value,
                         phoneNumberState.value,
                     ),
                     onDataSaved = {
                         onSignUpSuccess(NavRoutes.HomeDataObject(user.uid, user.email))
                         //   onSignUpSuccess(navData)
                     }
                 )
             },
             onFailure = { exception->
                 errorState.value = exception.message ?: "Unknown error"
             }
         )
    }
}