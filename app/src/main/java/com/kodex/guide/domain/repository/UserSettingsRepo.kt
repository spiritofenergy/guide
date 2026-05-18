package com.kodex.guide.domain.repository

import com.kodex.guide.domain.model.AddressData
import com.kodex.guide.domain.model.PersonalData
import com.kodex.guide.domain.model.User
import com.kodex.guide.domain.model.UserSettingsBundle
import com.kodex.guide.domain.model.UserSettingsData

interface UserSettingsRepo {
    suspend fun insertPersonalData(personalData: PersonalData
    ): Result<Unit>
    suspend fun insertAddressData(addressData: AddressData
    ): Result<Unit>
    suspend fun insertUserSettingsData(userSettingsData: UserSettingsData
    ): Result<Unit>
    suspend fun updateLastVisit(): Result<Unit>
    suspend fun getSettings(): Result<UserSettingsBundle>
}