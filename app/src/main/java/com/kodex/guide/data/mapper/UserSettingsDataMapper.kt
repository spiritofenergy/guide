package com.kodex.guide.data.mapper

import com.kodex.guide.data.model.UserSettingsData

fun UserSettingsData.toUserSetting(): UserSettingsData{
    return UserSettingsData(
        imageFormat = imageFormat,
        quality = quality,
        size = size,
    )

}
