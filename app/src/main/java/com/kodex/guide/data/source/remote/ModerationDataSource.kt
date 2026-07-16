package com.kodex.guide.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.data.model.BookDTO
import com.kodex.guide.data.model.RatingDataDTO
import com.kodex.guide.data.source.remote.FirebaseConst.MODERATION
import com.kodex.guide.data.source.remote.FirebaseConst.POSTS
import com.kodex.guide.data.source.remote.FirebaseConst.RATING
import com.kodex.guide.data.source.remote.FirebaseConst.RATING_DATA
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class ModerationDataSource(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,

) {
    suspend fun acceptComment(ratingData: RatingDataDTO): Result<Unit> {
        if (auth.uid == null) return Result.failure(Throwable("User not authenticated"))
     return try {
          db.collection(RATING)
              .document(ratingData.bookId)
              .collection(RATING_DATA)
              .document(ratingData.uid)
              .set(ratingData)

          val book: BookDTO = db.collection(POSTS)
              .document(ratingData.bookId)
              .get().await().toObject(BookDTO::class.java) ?: return Result.failure(Throwable("Book not found"))

          val ratingsList = book.ratingsList.toMutableList()

          if (ratingData.lastRating == 0) {
              ratingsList.add(ratingData.rating ?: 1)
          } else {
              val index = ratingsList.indexOf(ratingData.lastRating)
              ratingsList[index] = ratingData.rating ?: 1
          }
          db.collection(POSTS)
              .document(ratingData.bookId)
              .update("ratingsList", ratingsList)
         deleteComment(ratingData.uid)
         Result.success(Unit)
      }catch (e: Exception){
         Result.failure(e)
      }
    }
    suspend fun getAllCommentsToModerate(): Result <List<RatingDataDTO>> {
        return try {
            val querySnapshot = db.collection(MODERATION)
                .get().await()
            val commentsList = querySnapshot.toObjects(RatingDataDTO::class.java)
            Result.success(commentsList)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun deleteComment(uid: String): Result<Unit> {
        return try {
            db.collection(MODERATION)
                .document(uid)
                .delete().await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }

    }
}