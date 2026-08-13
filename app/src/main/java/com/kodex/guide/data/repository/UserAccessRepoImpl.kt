package com.kodex.guide.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.kodex.guide.domain.repository.UserAccessRepo
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class UserAccessRepoImpl @Inject constructor() : UserAccessRepo {

    private val firestore = Firebase.firestore

    override suspend fun isAdmin(uid: String): Boolean =
        suspendCancellableCoroutine { continuation ->
            firestore.collection("admin")
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    continuation.resume(doc.get("isAdmin") as? Boolean ?: false)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }

    override suspend fun isRegistered(uid: String): Boolean =
        suspendCancellableCoroutine { continuation ->
            firestore.collection("guide_users")
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    continuation.resume(doc.get("isRegistered") as? Boolean ?: false)
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }
}