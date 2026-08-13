package com.kodex.guide.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.data.model.RatingDataDTO
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataSource модерации комментариев.
 * Только сырые операции Firestore — без бизнес-логики.
 */
@Singleton
class FirebaseModerationDataSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    // ⚠️ ПРИВЕДИ К СВОЕЙ СХЕМЕ FIRESTORE
    // (имена коллекций/полей сверь с FirebaseConst или старым кодом в git)
    private companion object {
        const val COLLECTION_COMMENTS = "comments"
        const val FIELD_STATUS = "status"
        const val STATUS_PENDING = "pending"
        const val STATUS_ACCEPTED = "accepted"
    }

    /** Все комментарии, ожидающие модерации */
    suspend fun getAllCommentsToModerate(): Result<List<RatingDataDTO>> {
        return try {
            val snapshot = db.collection(COLLECTION_COMMENTS)
                .whereEqualTo(FIELD_STATUS, STATUS_PENDING)
                .get()
                .await()

            val comments = snapshot.documents.mapNotNull { document ->
                document.toObject(RatingDataDTO::class.java)
            }
            Result.success(comments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Одобрить комментарий */
    suspend fun acceptComment(ratingDataDTO: RatingDataDTO): Result<Unit> {
        return try {
            db.collection(COLLECTION_COMMENTS)
                .document(ratingDataDTO.uid)
                .update(FIELD_STATUS, STATUS_ACCEPTED)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Отклонить / удалить комментарий */
    suspend fun deleteComment(uid: String): Result<Unit> {
        return try {
            db.collection(COLLECTION_COMMENTS)
                .document(uid)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}