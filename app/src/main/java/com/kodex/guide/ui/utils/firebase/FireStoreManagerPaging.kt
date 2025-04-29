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
import com.kodex.guide.ui.castom.FilterData
import com.kodex.guide.ui.utils.Categories
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

const val IS_BASE_64 = true

@Singleton
class FireStoreManagerPaging(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
   // private val storage: FirebaseStorage

) {
    var categoryIndex = Categories.ALL
    var searchText = ""
    var filterData = FilterData()

  /*  var minPrice = 3
    var maxPrice = 5
    var isTitleFilter = true
*/
    suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?
    ): Pair<QuerySnapshot, List<Book>> {
        var query: Query = db.collection(FirebaseConst.POSTS).limit(pageSize).orderBy(
            filterData.filterType)
        val keysFavesList = getIdsFavesList()

        query = when (categoryIndex) {
            Categories.ALL -> query
            Categories.FAVORITES -> query.whereIn(FieldPath.of(FirebaseConst.KEY), keysFavesList)
            else -> query.whereEqualTo(FirebaseConst.CATEGORY_INDEX, categoryIndex)
        }

        if (searchText.isNotEmpty()){
            query = query.whereGreaterThanOrEqualTo(FirebaseConst.SEARCH_TITLE, searchText.lowercase())
                .whereLessThan(FirebaseConst.SEARCH_TITLE,"${searchText.lowercase()}\uF7FF") // "test"
        }

        if (filterData.filterType == FirebaseConst.PRICE
            && filterData.minPrise != 0
            && filterData.maxPrise != 0
            && filterData.minPrise <= filterData.maxPrise
            ) {
            query = query.whereGreaterThanOrEqualTo(FirebaseConst.PRICE, filterData.minPrise)
                .whereLessThanOrEqualTo(FirebaseConst.PRICE, filterData.maxPrise)
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
        val snapshot = getFavesCategoryReference().get().await()
        val idsList = snapshot.toObjects(Favorite::class.java)
        val keysList = arrayListOf<String>()

        idsList.forEach {
            keysList.add(it.key)
        }
        return if (keysList.isEmpty()) listOf("-1") else keysList
    }

    private fun getFavesCategoryReference(): CollectionReference {
        return db.collection(FirebaseConst.USERS)
            .document(auth.uid ?: "")
            .collection(FirebaseConst.FAVES)
    }


    fun onFaves(
        favorite: Favorite,
        isFav: Boolean,
    ) {
        val favesDocRef = getFavesCategoryReference()
            .document(favorite.key)
        if (isFav) {
            favesDocRef.set(favorite)
        } else {
            favesDocRef.delete()
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
        onFailure: (String) -> Unit
    ) {
        db.collection(FirebaseConst.POSTS)
            .document(book.key)
            .delete()
            .addOnSuccessListener {
                onDeleted()
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Error delete book")
            }
    }

    fun saveBookToFireStore(
        book: Book,
        onSaved: () -> Unit,
        onError: (String) -> Unit,
    ) {
        //Куда соханять фото
        val db = db.collection(FirebaseConst.POSTS)
        val key = if (book.key.isEmpty()) db.document().id else book.key//???
        db.document(key)
            .set(
                book.copy(key = key)
            ).addOnSuccessListener {
                onSaved()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error saved book")
            }
    }

    private fun uploadImageToStorage(
        oldImageUrl: String,
        uri: Uri?,
        book: Book,
        onSaved: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val timeStamp = System.currentTimeMillis()
        val storageRef = if (oldImageUrl.isEmpty()) {
           // storage.reference
              // .child(FirebaseConstant.BOOK_IMAGES)
             //   .child("image_$timeStamp.jpg")
        } else {
           // storage.getReferenceFromUrl(oldImageUrl)
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
                uploadImageToStorage(
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
