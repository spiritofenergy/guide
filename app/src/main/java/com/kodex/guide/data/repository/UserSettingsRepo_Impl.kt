package com.kodex.guide.data.repository

import com.kodex.guide.data.mapper.toDTO
import com.kodex.guide.data.mapper.toDomain
import com.kodex.guide.data.source.remote.UserSettingsDataSource
import com.kodex.guide.domain.model.AddressData
import com.kodex.guide.domain.model.PersonalData
import com.kodex.guide.domain.model.UserSettingsBundle
import com.kodex.guide.domain.model.UserSettingsData
import com.kodex.guide.domain.repository.UserSettingsRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsRepo_Impl@Inject constructor(
    private val userSettingsDataSource: UserSettingsDataSource
): UserSettingsRepo{
    override suspend fun insertPersonalData(personalData: PersonalData): Result<Unit> {
        // Просто конвертируем полученный personalData в DTO
        return userSettingsDataSource.insertPersonalData(personalData.toDTO())
    }

    override suspend fun insertAddressData(addressData: AddressData): Result<Unit> {
        return userSettingsDataSource.insertAddressData(addressData.toDTO())
    }

    override suspend fun insertUserSettingsData(userSettingsData: UserSettingsData): Result<Unit> {
        return userSettingsDataSource.insertUserSettingsData(userSettingsData.toDTO())
    }

    override suspend fun updateLastVisit(): Result<Unit> {
       return userSettingsDataSource.updateLastVisit()
    }

    override suspend fun getSettings(): Result<UserSettingsBundle> {
       return userSettingsDataSource.getSettings().map {
           it.toDomain()
       }
    }

}



