package com.kodex.guide.data.source.remote

 import com.google.firebase.auth.FirebaseAuth
 import com.google.firebase.firestore.FieldPath
 import com.google.firebase.firestore.FirebaseFirestore
 import com.google.firebase.firestore.Query
 import com.kodex.guide.data.model.BookDTO
 import com.kodex.guide.data.model.BooksPageDTO
 import com.kodex.guide.domain.model.Book
 import com.kodex.guide.domain.model.RatingData
 import com.kodex.guide.presentation.castom.FilterData
 import com.kodex.guide.utils.Categories
 import com.kodex.guide.utils.Categories.ALL
 import com.kodex.guide.utils.FirebaseConst.CATEGORY_INDEX
 import com.kodex.guide.utils.FirebaseConst.KEY
 import com.kodex.guide.utils.FirebaseConst.MODERATION
 import com.kodex.guide.utils.FirebaseConst.POSTS
 import com.kodex.guide.utils.FirebaseConst.RATING
 import com.kodex.guide.utils.FirebaseConst.RATING_DATA
 import com.kodex.guide.utils.FirebaseConst.SEARCH_TITLE
 import kotlinx.coroutines.tasks.await
 import javax.inject.Singleton

@Singleton
class BooksFirebaseRemoteDataSource(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,

    ) {
    var categoryIndex: Int = ALL
    var searchText = ""
    var filterData = FilterData()

    suspend fun nextPage(
        keysFavesList: List<String>,
        pageSize: Long,
        currentKey: String?,
    ): BooksPageDTO{
        var query: Query = fireStore.collection(POSTS)
            .limit(pageSize)
            .orderBy(filterData.filterType)
        // val keysFaves List = getIdsFavesList()

        query = when (categoryIndex) {
            ALL -> query
            Categories.FAVORITES -> query.whereIn(FieldPath.of(KEY), keysFavesList)
            else -> query.whereEqualTo(CATEGORY_INDEX, categoryIndex)
        }

        if (searchText.isNotEmpty()) {
            query = query.whereGreaterThanOrEqualTo(SEARCH_TITLE, searchText.lowercase())
                .whereLessThan(SEARCH_TITLE, "${searchText.lowercase()}\uF7FF")
        }

        /* if (!isPriceFilter) {
            query = query.whereGreaterThanOrEqualTo(FirebaseConst.PRICE, minPrice)
                .whereLessThanOrEqualTo(FirebaseConst.PRICE, maxPrice)
        }*/
        if (currentKey != null) {
            query = query.startAfter(currentKey)
        }
        val querySnapshot = query.get().await()
        val books = querySnapshot.toObjects(BookDTO::class.java)
        val updatedBooks = books.map {
            if (keysFavesList.contains(it.key)) {
                it.copy(isFavorite = true)
            } else {
                it
            }
        }
        return BooksPageDTO(updatedBooks, books.lastOrNull()?.title)
    }

    suspend fun saveBook(book: Book): Result<Unit> {
       return try {
           val db = fireStore.collection(POSTS)
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
}