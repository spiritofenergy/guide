package com.kodex.guide.data.mapper

import com.kodex.guide.data.model.UserSettingsDataDTO
import com.kodex.guide.domain.model.UserSettingsData

fun UserSettingsDataDTO.toUserSettingData(): UserSettingsData{
    return UserSettingsData(
        imageFormat = imageFormat,
        quality = quality,
        size = size,
    )
}
fun UserSettingsData.toDTO(): UserSettingsDataDTO{
    return UserSettingsDataDTO(
        imageFormat = imageFormat,
        quality = quality,
        size = size,
    )
}
