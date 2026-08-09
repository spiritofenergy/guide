package com.kodex.guide.presentation.login.sign_up

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.data.model.PersonalDataDTO
import com.kodex.guide.data.source.local.PreferenceDataSource
import com.kodex.guide.data.source.remote.UserSettingsDataSource
import com.kodex.guide.domain.model.User
import com.kodex.guide.domain.repository.AuthRepo
import com.kodex.guide.presentation.navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class SingUpViewModel @Inject constructor(
    private val authRepo : AuthRepo,
    private val userSettingsDataSource: UserSettingsDataSource,
    private val preferenceDataSource: PreferenceDataSource
) : ViewModel() {
    val currentUser = mutableStateOf<User?>(null)

    val errorState = mutableStateOf("")
    val successState = mutableStateOf(false)
    val emailState = mutableStateOf("")
    val passwordState = mutableStateOf("")
    val nameState = mutableStateOf("")
    val phoneState = mutableStateOf("")

    fun signUp(
        onSignUpSuccess: (NavRoutes.HomeDataObject) -> Unit
    ) = viewModelScope.launch {
        val authResult = authRepo.signUp(emailState.value, passwordState.value)

        authResult.fold(
            onSuccess = { user ->
                val userWithName = user.copy(userName = nameState.value)
                authRepo.createUserProfile(userWithName)
                // Сохраняем персональные данные в Firestore
                val insertResult = userSettingsDataSource.insertPersonalData(
                    personalData = PersonalDataDTO(
                        nameState.value,
                        phoneState.value,))
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
    fun saveLastName(){
        preferenceDataSource.saveName(PreferenceDataSource.NAME_KEY, nameState.value)
    }
    fun saveLastPhone(){
        preferenceDataSource.savePhone(PreferenceDataSource.PHONE_KEY, phoneState.value)
    }
    fun saveLastEmail(){
        preferenceDataSource.saveEmail(PreferenceDataSource.EMAIL_KEY, emailState.value)
    }
    fun saveLastPassword(){
        preferenceDataSource.savePassword(PreferenceDataSource.PASSWORD_KEY, passwordState.value)
    }


    fun getName() {
        nameState.value = preferenceDataSource.getName(PreferenceDataSource.NAME_KEY, "")
    }
    fun getPhone() {
        phoneState.value = preferenceDataSource.getPhone(PreferenceDataSource.PHONE_KEY, "")
    }
    fun getEmail() {
        emailState.value = preferenceDataSource.getEmail(PreferenceDataSource.EMAIL_KEY, "")
    }
    fun getPassword() {
        passwordState.value = preferenceDataSource.getPassword(PreferenceDataSource.PASSWORD_KEY, "")
    }



        fun getAccountState(){
        currentUser.value = authRepo.getCurrentUser()
    }
}