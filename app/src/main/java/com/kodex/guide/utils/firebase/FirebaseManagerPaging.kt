package com.kodex.guide.utils.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.model.BookCategories.ALL
import com.kodex.guide.utils.FirebaseConst.POSTS
import javax.inject.Inject
import javax.inject.Singleton

const val IS_BASE_64 = true

@Singleton
class FireStoreManagerPaging @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    // private val contentResolver: ContentResolver
    // private val storage: FirebaseStorage,
) {
    var categoryIndex: Int = ALL.id
    var searchText = ""
    var minPrice = 0
    var maxPrice = 5000
    var isTitleFilter = false
    var isPriceFilter = false

   /* suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?,
    ): Pair<QuerySnapshot, List<Book>> {
        var query: Query = db.collection(POSTS).limit(pageSize)
        //  .orderBy(FirebaseConst.TITLE)
        // val keysFavesList = getIdsFavesList()

        query = when (categoryIndex) {
            ALL -> query
            //Categories.FAVORITES -> query.whereIn(FieldPath.of(KEY), keysFavesList)
            else -> query.whereEqualTo(CATEGORY_INDEX, categoryIndex)
        }

        if (searchText.isNotEmpty()) {
            query = query.whereGreaterThanOrEqualTo(SEARCH_TITLE, searchText.lowercase())
                .whereLessThan(SEARCH_TITLE, "${searchText.lowercase()}\uF7FF")
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
        *//*  val updatedBooks = books.map {
            if (keysFavesList.contains(it.key)) {
                it.copy(isFavorite = true)
            } else {
                it
            }
        }
        return Pair(querySnapshot, updatedBooks)
    }
    *//*

    }*/
/*
        fun getFavesCategoryReference(): CollectionReference {
            return db.collection(USERS)
                .document(auth.uid ?: "")
                .collection(FirebaseConst.FAVORITES)
        }*/


        fun saveBookToFireStore(
            book: Book,
            onSaved: () -> Unit,
            onError: (String) -> Unit,
        ) {
            val db = db.collection(POSTS)
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

        fun uploadImageToFirestore(
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
        }




}


/*
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
                *//*  saveBookToFireStore(
                book,
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError("Error save Image1 ")
                },
            )*//*
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

        */





