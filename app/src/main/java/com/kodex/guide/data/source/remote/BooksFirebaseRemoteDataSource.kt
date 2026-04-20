package com.kodex.guide.data.source.remote

 import com.google.firebase.auth.FirebaseAuth
 import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.domain.model.Book
 import com.kodex.guide.domain.model.RatingData
 import com.kodex.guide.ui.utils.FirebaseConst
 import com.kodex.guide.ui.utils.FirebaseConst.MODERATION
 import com.kodex.guide.ui.utils.FirebaseConst.POSTS
 import com.kodex.guide.ui.utils.FirebaseConst.RATING
 import com.kodex.guide.ui.utils.FirebaseConst.RATING_DATA
 import kotlinx.coroutines.tasks.await
 import javax.inject.Singleton

@Singleton
class BooksFirebaseRemoteDataSource(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,

    ) {
   suspend fun deleteBook(book: Book): Result<Unit> {
    return try {
        fireStore.collection(POSTS)
            .document(book.key)
            .delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
      }
   }

  private suspend fun saveBook(book: Book): Result<Unit> {
       return try {
           val db = fireStore.collection(FirebaseConst.POSTS)
           val key = if (book.key.isEmpty()) db.document().id else book.key
           db.document(key)
               .set(
                   book.copy(key = key)).await()
           Result.success(Unit)
       }catch (e: Exception){
           Result.failure(e)
       }
   }
    suspend fun submitUserRating(ratingData: RatingData, bookId: String): Result<Unit> {
        if (auth.uid == null) return Result.failure(Throwable("User not authenticated"))
      return  try {
            fireStore.collection(MODERATION)
                .document(auth.uid!!)
                .set(ratingData.copy(
                    name = auth.currentUser?.email ?: "Unknown",
                    uid = auth.uid!!,
                    bookId = bookId
                )).await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
    suspend fun deleteComment(uid: String): Result<Unit> {
       return try {
           fireStore.collection(MODERATION)
               .document(uid)
               .delete().await()
           Result.success(Unit)
       }catch (e: Exception){
           Result.failure(e)
       }
    }

    suspend fun getBookComments(bookId: String): Result<List<RatingData>> {
       return try {
           val querySnapshot = fireStore.collection(RATING)
               .document(bookId)
               .collection(RATING_DATA)
               .get().await()
           Result.success(querySnapshot.toObjects(RatingData::class.java))
       }catch (e: Exception){
           Result.failure(e)
       }
    }
    suspend fun getUserRating(bookId: String): Result<RatingData?> {
        if (auth.uid == null) return  Result.failure(Throwable("User not authenticated"))
       return try {
            val querySnapshot = fireStore.collection(RATING)
                .document(bookId)
                .collection(RATING_DATA)
                .document(auth.uid!!)
                .get().await()
            return  Result.success(querySnapshot.toObject(RatingData::class.java))
      }  catch (e:Exception){
            Result.failure(e)
        }
    }
}