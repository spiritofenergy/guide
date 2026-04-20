package com.kodex.guide.ui.settingsScreen

import com.kodex.guide.domain.model.AddressData
import com.kodex.guide.domain.model.PersonalData
import com.kodex.guide.domain.model.UserSettingsData
import javax.inject.Singleton

@Singleton
class GlobalSettings(){
    var personalData = PersonalData()
    var addressData = AddressData()
    var userSettingsData = UserSettingsData()

}