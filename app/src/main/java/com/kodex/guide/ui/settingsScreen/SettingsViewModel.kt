package com.kodex.guide.ui.settingsScreen

import androidx.lifecycle.ViewModel
import com.kodex.guide.ui.utils.firebase.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
   private val authManager: AuthManager
): ViewModel(){
    var emailToDelete = ""
    var passwordToDelete = ""
    fun resetPassword(
        email: String,
        onResetPasswordSuccess: ()-> Unit,
        onResetPasswordFailure: (String) -> Unit
    ){
        authManager.resetPassword(
            email,
            onResetPasswordSuccess,
            onResetPasswordFailure
        )
    }
    fun deleteAccount(
        onAccountDeleted: () -> Unit,
        onAccountDeleteFailure: (String) -> Unit
    ){
        if (emailToDelete.isEmpty() || passwordToDelete.isEmpty()){
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
            onDeleteFailure = {

            }
        )
    }
    fun signOut() = authManager.signOut()
}