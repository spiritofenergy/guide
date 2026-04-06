package com.kodex.guide.ui.settingsScreen

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
    private val fireStoreManagerPaging: FireStoreManagerPaging
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
        }

        if (!newAddressData.upToDate(oldAddressData)) {
            fireStoreManagerPaging.insertAddressData(newAddressData)
        }

        if (!newUserSettingsData.upToDate(oldUserSettingsData)) {
            fireStoreManagerPaging.insertUserSettingsData(newUserSettingsData)
        }
    }

    fun getSettings(onSettingsLoaded: (UserSettingsData)-> Unit) = viewModelScope.launch {
        fireStoreManagerPaging.getSettings(
            onSettingsLoaded = { pData, aData, sData ->
                oldPersonalData = pData
                oldAddressData = aData
                personalData.value = pData
                addressData.value = aData
                oldUserSettingsData = sData
                onSettingsLoaded(sData)
            }
        )
    }
}