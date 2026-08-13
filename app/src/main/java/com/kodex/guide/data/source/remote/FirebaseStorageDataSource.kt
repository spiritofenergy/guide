package com.kodex.guide.data.source.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.kodex.guide.data.images.BitmapEncoder
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorageDataSource @Inject constructor(
    private val storage: FirebaseStorage,
    private val imageEncoder: BitmapEncoder
) {
    // Cохранение в Storage не используется
    suspend fun uploadImage(
        oldImageUrl: String,
        uri: Uri?
    ): String {
        if (uri == null) return oldImageUrl
        val timeStamp = System.currentTimeMillis()
        val storageRef = if (oldImageUrl.isEmpty()) {
             storage.reference
               .child(FirebaseConst.BOOK_IMAGES)
               .child("image_$timeStamp.jpg")
        } else {
             storage.getReferenceFromUrl(oldImageUrl)
        }
        val byteArray = imageEncoder.uriToByteArray(uri)
        storageRef.putBytes(byteArray).await()
        return storageRef.downloadUrl.await().toString()
    }
}