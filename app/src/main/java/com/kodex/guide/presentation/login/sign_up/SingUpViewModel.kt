package com.kodex.guide.presentation.login.sign_up

import android.util.Log
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
    ) = viewModelScope.launch {
        val authResult = authRepo.signUp(emailState.value, passwordState.value)

        authResult.fold(
            onSuccess = { user ->
                // Сохраняем персональные данные в Firestore
                val insertResult = userSettingsDataSource.insertPersonalData(
                    personalData = PersonalData(
                        nameState.value,
                        phoneNumberState.value,
                    )
                )

                // ВАЖНО: обрабатываем результат сохранения
                insertResult.fold(
                    onSuccess = {
                        // Успех — переходим на главный экран
                        onSignUpSuccess(NavRoutes.HomeDataObject(user.uid, user.email))
                    },
                    onFailure = { exception ->
                        // Ошибка сохранения — показываем пользователю
                        errorState.value = "Ошибка сохранения: ${exception.message}"
                        Log.e("SignUp", "Не удалось сохранить данные", exception)
                    }
                )
            },
            onFailure = { exception ->
                errorState.value = exception.message ?: "Unknown error"
            }
        )
    }
 /*    fun signUp(
        onSignUpSuccess: (NavRoutes.HomeDataObject) -> Unit
    ) = viewModelScope.launch{
           // emailState.value = ""
        val authResult = authRepo.signUp(emailState.value, passwordState.value)
         authResult.fold(
             onSuccess = { user ->
                val insertResult = userSettingsDataSource.insertPersonalData(
                     personalData = PersonalData(
                         nameState.value,
                         phoneNumberState.value,
                     )
                   *//*  onDataSaved = {
                         onSignUpSuccess(NavRoutes.HomeDataObject(user.uid, user.email))
                         //   onSignUpSuccess(navData)
                     }*//*
                 )
                 insertResult.fold(onSuccess = onSignUpSuccess(NavRoutes.HomeDataObject(user.uid, user.email)))

             },
             onFailure = { exception->
                 errorState.value = exception.message ?: "Unknown error"
             }
         )
    }*/
}