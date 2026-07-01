package com.kodex.guide.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.data.model.AddressDataDTO
import com.kodex.guide.data.model.UserSettingsBundleDTO
import com.kodex.guide.data.model.UserSettingsDataDTO
import com.kodex.guide.domain.model.AddressData
import com.kodex.guide.domain.model.PersonalData
import com.kodex.guide.domain.model.UserSettingsData
import com.kodex.guide.utils.FirebaseConst.ADDRESS_DATA
import com.kodex.guide.utils.FirebaseConst.DATA
import com.kodex.guide.utils.FirebaseConst.PERSONAL_DATA
import com.kodex.guide.utils.FirebaseConst.USER_DATA
import com.kodex.guide.utils.FirebaseConst.USER_SETTINGS
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class UserSettingsDataSource(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun insertPersonalData(
        personalData: PersonalData,
        onDataSaved:()-> Unit = {},
    ): Result<Unit> {
        if (auth.uid == null) return Result.failure(Exception("No user uid found"))
        try {
            db.collection(USER_DATA)
                .document(auth.uid!!)
                .collection(PERSONAL_DATA)
                .document(DATA)
                .set(personalData).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun insertAddressData(
        addressData: AddressDataDTO
    ): Result<Unit> {
        if (auth.uid == null) return Result.failure(Exception("No user uid found"))
        try {
            db.collection(USER_DATA)
                .document(auth.uid!!)
                .collection(ADDRESS_DATA)
                .document(DATA)
                .set(addressData).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun insertUserSettingsData(
        userSettingsData: UserSettingsDataDTO
    ): Result<Unit> {
        if (auth.uid == null) return Result.failure(Exception("No user uid found"))
        try {
            db.collection(USER_DATA)
                .document(auth.uid!!)
                .collection(USER_SETTINGS)
                .document(DATA)
                .set(userSettingsData).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getSettings(): Result<UserSettingsBundleDTO> {
        if (auth.uid == null) return Result.failure(Exception("No user uid found"))
        try {
            val querySnapshotPersonal = db.collection(USER_DATA)
                .document(auth.uid!!)
                .collection(PERSONAL_DATA)
                .document(DATA)
                .get().await()
            val personalData =
                querySnapshotPersonal.toObject(PersonalData::class.java) ?: PersonalData()

            val querySnapshotAddress = db.collection(USER_DATA)
                .document(auth.uid!!)
                .collection(ADDRESS_DATA)
                .document(DATA)
                .get().await()
            val addressData =
                querySnapshotAddress.toObject(AddressData::class.java) ?: AddressData()

            val querySnapshotSettings = db.collection(USER_DATA)
                .document(auth.uid!!)
                .collection(USER_SETTINGS)
                .document(DATA)
                .get().await()
            val userSettingsData =
                querySnapshotSettings.toObject(UserSettingsData::class.java) ?: UserSettingsData()
            return Result.success(
                UserSettingsBundleDTO(
                    personalData,
                    addressData,
                    userSettingsData
                )
            )
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun updateLastVisit(): Result<Unit> {
        if (auth.uid == null) return Result.failure(Exception("No user uid found"))
        try {
            db.collection(USER_DATA)
                .document(auth.uid!!)
                .collection(PERSONAL_DATA)
                .document(DATA)
                .update("lastVisit", System.currentTimeMillis()).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}