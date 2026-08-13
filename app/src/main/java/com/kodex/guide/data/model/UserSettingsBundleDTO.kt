package com.kodex.guide.data.model

import com.kodex.guide.domain.model.AddressData
import com.kodex.guide.domain.model.PersonalData
import com.kodex.guide.domain.model.UserSettingsData

data class UserSettingsBundleDTO(
    val personalData: PersonalData,
    val addressData: AddressData,
    val userSettingsData: UserSettingsData
)
