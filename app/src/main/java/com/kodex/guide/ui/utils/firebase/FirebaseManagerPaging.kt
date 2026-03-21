package com.kodex.guide.ui.utils.firebase

import android.R.attr.rating
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.addscreen.data.Favorite
import com.kodex.guide.ui.detailScreen.RatingData
import com.kodex.guide.ui.utils.Categories
import com.kodex.guide.ui.utils.Categories.ALL
import com.kodex.guide.ui.utils.Categories.FAVORITES
import com.kodex.guide.ui.utils.FirebaseConst.GUIDE_RATING
import com.kodex.guide.ui.utils.FirebaseConst.RATING
import com.kodex.guide.ui.utils.firebase.FirebaseConst.CATEGORY_INDEX
import com.kodex.guide.ui.utils.firebase.FirebaseConst.KEY
import com.kodex.guide.ui.utils.firebase.FirebaseConst.POSTS
import com.kodex.guide.ui.utils.firebase.FirebaseConst.SEARCH_TITLE
import com.kodex.guide.ui.utils.firebase.FirebaseConst.USERS
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

const val IS_BASE_64 = true

@Singleton
class FireStoreManagerPaging(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    //private val contentResolver: ContentResolver
    // private val storage: FirebaseStorage,
) {
    var categoryIndex: Int = Categories.ALL
    var searchText = ""
    var minPrice = 0
    var maxPrice = 5000
    var isTitleFilter = false
    var isPriceFilter = false

    suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?,
    ): Pair<QuerySnapshot, List<Book>> {
        var query: Query = db.collection(POSTS).limit(pageSize)
          //  .orderBy(FirebaseConst.TITLE)
        val keysFavesList = getIdsFavesList()

        query = when (categoryIndex) {
            ALL -> query
            FAVORITES -> query.whereIn(FieldPath.of(KEY), keysFavesList)
            else -> query.whereEqualTo(CATEGORY_INDEX, categoryIndex)
        }

       if (searchText.isNotEmpty()){
            query = query.whereGreaterThanOrEqualTo(SEARCH_TITLE, searchText.lowercase())
                .whereLessThan(SEARCH_TITLE,"${searchText.lowercase()}\uF7FF")
        }

       /* if (!isPriceFilter) {
            query = query.whereGreaterThanOrEqualTo(FirebaseConst.PRICE, minPrice)
                .whereLessThanOrEqualTo(FirebaseConst.PRICE, maxPrice)
        }*/
        if (currentKey != null) {
            query = query.startAfter(currentKey)
        }
        val querySnapshot = query.get().await()
        val books = querySnapshot.toObjects(Book::class.java)
        val updatedBooks = books.map {
            if (keysFavesList.contains(it.key)) {
                it.copy(isFavorite = true)
            } else {
                it
            }
        }
        return Pair(querySnapshot, updatedBooks)
    }


    private suspend fun getIdsFavesList(): List<String> {
        val snapshot = getFavesCategoryReference().get().await()
        val idsList = snapshot.toObjects(Favorite::class.java)
        val keysList = arrayListOf<String>()

        idsList.forEach {
            keysList.add(it.key)
        }
        return if (keysList.isEmpty()) listOf("-1") else keysList
    }

    fun getFavesCategoryReference(): CollectionReference {
        return db.collection(USERS)
            .document(auth.uid ?: "")
            .collection(FirebaseConst.FAVES)
    }

    fun onFaves(
        favorite: Favorite,
        isFav: Boolean,
    ) {
        val favesDokRef = getFavesCategoryReference()
            .document(favorite.key)
        if (isFav) {
            favesDokRef.set(favorite)
        } else {
            favesDokRef.delete()
        }
    }

    fun changeFavesState(books: List<Book>, book: Book): List<Book> {
        return books.map { bk ->
            if (bk.key == book.key) {
                onFaves(
                    Favorite(bk.key),
                    !bk.isFavorite
                )
                bk.copy(isFavorite = !bk.isFavorite)
            } else {
                bk
            }
        }
    }

    fun deleteBook(
        book: Book,
        onDeleted: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        db.collection(POSTS)
            .document(book.key)
            .delete()
            .addOnSuccessListener {
                onDeleted()
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Error deleting book")

            }
    }

    fun saveBookToFireStore(
        book: Book,
        onSaved: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val db = db.collection(FirebaseConst.POSTS)
        val key = if (book.key.isEmpty()) db.document().id else book.key
        db.document(key)
            .set(
                book.copy(key = key)
            ).addOnSuccessListener {
                onSaved()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error saved book")
            }
        onError
    }

    private fun uploadImageToFirestore(
        oldImageUrl: String,
        uri: Uri?,
        book: Book,
        onSaved: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val timeStamp = System.currentTimeMillis()
        val storageRef = if (oldImageUrl.isEmpty()) {
            // storage!!.reference
            //   .child("spark_posts")
            //   .child("image_$timeStamp.jpg")
        } else {
            // storage?.getReferenceFromUrl(oldImageUrl)
        }
        if (uri == null) {
            saveBookToFireStore(
                book.copy(imageUrl = oldImageUrl),
                onSaved = {
                    onSaved()
                },
                onError = { massage ->
                    onError(massage)
                }
            )
            return
        }
//        val imageBytes = ImageUtils.uriToBiteArray(uri, contentResolver)
//        val uploadTask = storageRef?.putBytes(imageBytes)
//        uploadTask?.addOnSuccessListener{
//            storageRef.downloadUrl.addOnSuccessListener{url ->
//                saveBookToFireStore(
//                    book.copy(imageUrl = url.toString()),
//                    onSaved = {
//                        onSaved()
//                    },
//                    onError = {massage->
//                        onError(massage)
//                    }
//                )
//            }
//        }
    }

    fun saveBookImage(
        oldImageUrl: String,
        uri: Uri?,
        book: Book,
        onSaved: () -> Unit,
        onError: (String) -> Unit,
    ) {
        if (IS_BASE_64) {
            saveBookToFireStore(
                book,
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError("Error save Image1 ")
                },
            )
        } else {
            uploadImageToFirestore(
                oldImageUrl = oldImageUrl,
                uri = uri,
                book = book,
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError("Error save Image2")
                }
            )
        }
    }
    fun insertRating(ratingData: RatingData, bookId: String){
        if (auth.uid == null)return
        db.collection(GUIDE_RATING)
            .document(bookId)
            .collection(RATING)
            .document(auth.uid!!)
            .set(ratingData.copy(name = auth.currentUser?.email?: "Unknown" ))
    }

    suspend fun getRating(bookId: String): Pair<Double, List<RatingData>> {
       val querySnapshot = db.collection(GUIDE_RATING)
            .document(bookId)
            .collection(RATING)
            .get().await()
        val ratingList = querySnapshot.toObjects(RatingData::class.java)
        val averageRating = ratingList.map {it.rating}.average()
        return Pair(averageRating, ratingList)
    }

    suspend fun getUserRating(bookId: String): RatingData? {
        if (auth.uid == null) return null
        val querySnapshot = db.collection(GUIDE_RATING)
            .document(bookId)
            .collection(RATING)
            .document(auth.uid!!)
            .get().await()
        return querySnapshot.toObject(RatingData::class.java)
    }
}
