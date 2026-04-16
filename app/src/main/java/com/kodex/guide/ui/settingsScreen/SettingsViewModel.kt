package com.kodex.guide.ui.settingsScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.ui.settingsScreen.data.AddressData
import com.kodex.guide.ui.settingsScreen.data.PersonalData
import com.kodex.guide.ui.settingsScreen.data.UserSettingsData
import com.kodex.guide.ui.utils.firebase.AuthManager
import com.kodex.guide.ui.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val fireStoreManagerPaging: FireStoreManagerPaging,
    private val globalSettings: GlobalSettings,
) : ViewModel() {
    var newPersonalData = PersonalData()
    var oldPersonalData = PersonalData()
    var newAddressData = AddressData()
    var oldAddressData = AddressData()

    var oldUserSettingsData = UserSettingsData()
    var newUserSettingsData = UserSettingsData()
    var emailToDelete = ""
    var passwordToDelete = ""
    val personalData = mutableStateOf(PersonalData())
    val addressData = mutableStateOf(AddressData())

    fun resetPassword(
        email: String,
        onResetPasswordSuccess: () -> Unit,
        onResetPasswordFailure: (String) -> Unit
    ) {
        authManager.resetPassword(
            email,
            onResetPasswordSuccess,
            onResetPasswordFailure
        )
    }

    fun deleteAccount(
        onAccountDeleted: () -> Unit,
        onAccountDeleteFailure: (String) -> Unit
    ) {
        if (emailToDelete.isEmpty() || passwordToDelete.isEmpty()) {
            onAccountDeleteFailure("Email snd Password be empty")
            return
        }
        authManager.deleteAccount(
            emailToDelete,
            passwordToDelete,
            onDeleteSuccess = {
                emailToDelete = ""
                passwordToDelete = ""
                onAccountDeleted()
            },
            onDeleteFailure = { error ->
                onAccountDeleteFailure(error)

            }
        )
    }

    fun signOut() = authManager.signOut()

    fun saveSettings() {
        if (!newPersonalData.upToDate(oldPersonalData)) {
            fireStoreManagerPaging.insertPersonalData(newPersonalData)
            Log.d("MyLog","newPersonalData ${newPersonalData}")
        }

        if (!newAddressData.upToDate(oldAddressData)) {
            fireStoreManagerPaging.insertAddressData(newAddressData)
            Log.d("MyLog"," newAddressData ${newAddressData}")
        }

        if (!newUserSettingsData.upToDate(oldUserSettingsData)) {
            fireStoreManagerPaging.insertUserSettingsData(newUserSettingsData)
            Log.d("MyLog","newUserSettingsData  ${newUserSettingsData}")
        }
    }

    fun getSettings(onSettingsLoaded: (UserSettingsData)-> Unit) = viewModelScope.launch {

        fireStoreManagerPaging.getSettings { personal, address, settings ->
            // Сохраняем в globalSettings для кэширования
            globalSettings.personalData = personal
            globalSettings.addressData = address
            globalSettings.userSettingsData = settings

            // Обновляем ViewModel поля
            oldPersonalData = personal
            personalData.value = personal

            oldAddressData = address
            addressData.value = address

            oldUserSettingsData = settings

            // Вызываем колбэк с настройками
            onSettingsLoaded(settings)

            /*
            oldPersonalData = globalSettings.personalData

        personalData.value = oldPersonalData
        Log.d("MyLog","personalData.value = oldPersonalData  ${personalData}")

        oldAddressData = globalSettings.addressData

        addressData.value = oldAddressData
        Log.d("MyLog","addressData.value = oldAddressData  ${addressData}")
        oldUserSettingsData = globalSettings.userSettingsData

        onSettingsLoaded(globalSettings.userSettingsData)*/
        }
    }
}