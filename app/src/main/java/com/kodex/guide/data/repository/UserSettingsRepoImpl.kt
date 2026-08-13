package com.kodex.guide.data.repository

import com.kodex.guide.data.mapper.toDTO
import com.kodex.guide.data.mapper.toDomain
import com.kodex.guide.data.source.remote.FirebaseUserSettingsDataSource
import com.kodex.guide.domain.model.AddressData
import com.kodex.guide.domain.model.PersonalData
import com.kodex.guide.domain.model.UserSettingsBundle
import com.kodex.guide.domain.model.UserSettingsData
import com.kodex.guide.domain.repository.UserSettingsRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsRepoImpl@Inject constructor(
    private val firebaseUserSettingsDataSource: FirebaseUserSettingsDataSource
): UserSettingsRepo{
    override suspend fun insertPersonalData(personalData: PersonalData): Result<Unit> {
        // Просто конвертируем полученный personalData в DTO
        return firebaseUserSettingsDataSource.insertPersonalData(personalData.toDTO())
    }

    override suspend fun insertAddressData(addressData: AddressData): Result<Unit> {
        return firebaseUserSettingsDataSource.insertAddressData(addressData.toDTO())
    }

    override suspend fun insertUserSettingsData(userSettingsData: UserSettingsData): Result<Unit> {
        return firebaseUserSettingsDataSource.insertUserSettingsData(userSettingsData.toDTO())
    }

    override suspend fun updateLastVisit(): Result<Unit> {
       return firebaseUserSettingsDataSource.updateLastVisit()
    }

    override suspend fun getSettings(): Result<UserSettingsBundle> {
       return firebaseUserSettingsDataSource.getSettings().map {
           it.toDomain()
       }
    }

}



