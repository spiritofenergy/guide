package com.kodex.guide.ui.settingsScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.integrity.internal.u
import com.kodex.guide.domain.model.AddressData
import com.kodex.guide.domain.model.PersonalData
import com.kodex.guide.domain.model.UserSettingsData
import com.kodex.guide.data.source.remote.FirebaseAuthDataSource
import com.kodex.guide.domain.model.UserSettingsBundle
import com.kodex.guide.domain.repository.AuthRepo
import com.kodex.guide.domain.repository.UserSettingsRepo
import com.kodex.guide.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val userSettingsRepo: UserSettingsRepo,
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
   // val userSettingsData = mutableStateOf(UserSettingsData())

    private val _settingsBundleState = MutableStateFlow(
        UserSettingsBundle(
            PersonalData(),
            AddressData(),
            UserSettingsData()
        )
    )
    val settingsBundleState = _settingsBundleState.asStateFlow()

    fun resetPassword(email: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = authRepo.resetPassword(email)
        result.fold(
            onSuccess = {

            },
            onFailure = {

            }
        )
    }

    fun deleteAccount() = viewModelScope.launch(Dispatchers.IO) {
        val result = authRepo.deleteAccount(emailToDelete, passwordToDelete)
        result.fold(
            onSuccess = {
            },
            onFailure = {
            }
        )
    }

    fun signOut() = authRepo.signOut()

    fun saveSettings() = viewModelScope.launch(Dispatchers.IO) {
        if (!newPersonalData.upToDate(oldPersonalData)) {
            userSettingsRepo.insertPersonalData(newPersonalData)
            Log.d("MyLog", "newPersonalData ${newPersonalData}")
        }

        if (!newAddressData.upToDate(oldAddressData)) {
            userSettingsRepo.insertAddressData(newAddressData)
            Log.d("MyLog", " newAddressData ${newAddressData}")
        }

        if (!newUserSettingsData.upToDate(oldUserSettingsData)) {
            userSettingsRepo.insertUserSettingsData(newUserSettingsData)
            Log.d("MyLog", "newUserSettingsData  ${newUserSettingsData}")
        }
    }

    fun getSettings() = viewModelScope.launch(Dispatchers.IO) {
        val resul = userSettingsRepo.getSettings()
        resul.fold(
            onSuccess = { userSettingsBundle ->
                oldPersonalData = userSettingsBundle.personalData
                personalData.value = oldPersonalData
                oldAddressData = userSettingsBundle.addressData
                addressData.value = oldAddressData
                oldUserSettingsData = userSettingsBundle.userSettingsData
              //  userSettingsData.value = oldUserSettingsData
                _settingsBundleState.value = userSettingsBundle
            },
            onFailure = {
            }
        )
    }
}
