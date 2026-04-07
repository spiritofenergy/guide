package com.kodex.guide.ui.settingsScreen

import com.kodex.guide.ui.settingsScreen.data.AddressData
import com.kodex.guide.ui.settingsScreen.data.PersonalData
import com.kodex.guide.ui.settingsScreen.data.UserSettingsData
import javax.inject.Singleton

@Singleton
class GlobalSettings(){
    var personalData = PersonalData()
    var addressData = AddressData()
    var userSettingsData = UserSettingsData()

}