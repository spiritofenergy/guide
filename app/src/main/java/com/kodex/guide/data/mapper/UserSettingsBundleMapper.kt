package com.kodex.guide.data.mapper

import com.kodex.guide.data.model.UserSettingsBundleDTO
import com.kodex.guide.domain.model.UserSettingsBundle

fun UserSettingsBundleDTO.toDomain(): UserSettingsBundle {
    return UserSettingsBundle(
        personalData = personalData,
        addressData = addressData,
        userSettingsData = userSettingsData
    )
}