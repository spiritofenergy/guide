package com.kodex.guide.domain.model

data class UserSettingsBundle(
    val personalData: PersonalData,
    val addressData: AddressData,
    val userSettingsData: UserSettingsData
)
