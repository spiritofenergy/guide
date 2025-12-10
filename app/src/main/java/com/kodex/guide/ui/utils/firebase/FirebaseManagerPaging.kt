package com.kodex.guide.ui.utils.firebase

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
import com.kodex.guide.ui.utils.Categories
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
    var category = Categories.ALL
    var searchText = ""

    var minPrice = 0
    var maxPrice = 5000
    var isTitleFilter = false
    var isPriceFilter = false

    suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?,
    ): Pair<QuerySnapshot, List<Book>> {
        var query: Query = db.collection(FirebaseConst.POSTS).limit(pageSize).orderBy(FirebaseConst.TITLE)
        val keysFavesList = getIdsFavesList()

        query = when (category) {
            Categories.ALL -> query
            Categories.FANTASY -> query.whereIn(FieldPath.of(FirebaseConst.KEY), keysFavesList)
            else -> query.whereEqualTo(FirebaseConst.CATEGORY_INDEX, category)
        }

        if (searchText.isNotEmpty()){
            query = query.whereGreaterThanOrEqualTo(FirebaseConst.SEARCH_TITLE, searchText.lowercase())
                .whereLessThan(FirebaseConst.SEARCH_TITLE,"${searchText.lowercase()}\uF7FF") 
        }

        if (!isPriceFilter) {
            query = query.whereGreaterThanOrEqualTo(FirebaseConst.PRICE, minPrice)
                .whereLessThanOrEqualTo(FirebaseConst.PRICE, maxPrice)
        }
        if (currentKey != null) {
            query = query.startAfter(currentKey)
        }
        val querySnapshot = query.get().await()
        val books = querySnapshot.toObjects(Book::class.java)
        val updatedBooks = books.map {
            if (keysFavesList.contains(it.key)) {
                it.copy(isFaves = true)
            } else {
                it
            }
        }
        return Pair(querySnapshot, updatedBooks)
    }


    private suspend fun getIdsFavesList(): List<String> {
        var snapshot = getFavesCategoryReference().get().await()
        val idsList = snapshot.toObjects(Favorite::class.java)
        val keysList = arrayListOf<String>()

        idsList.forEach {
            keysList.add(it.key)
        }
        return if (keysList.isEmpty()) listOf("-1") else keysList
    }

    fun getFavesCategoryReference(): CollectionReference {
        return db.collection(FirebaseConst.USERS)
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
                    !bk.isFaves
                )
                bk.copy(isFaves = !bk.isFaves)
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
        db.collection(FirebaseConst.POSTS)
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
}
