package com.kodex.guide.ui.utils.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.addscreen.data.Favorite
import javax.inject.Singleton

const val IS_BASE_64 = true
@Singleton
class FireStoreManager(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseFirestore
) {
    fun getAllFavesIds(
        onFaves: (List<String>) -> Unit,
    ) {
        getFavesCategoryReference()
            .get()
            .addOnSuccessListener { task ->
                val idsList = task.toObjects(Favorite::class.java)
                val keysList = arrayListOf<String>()
                idsList.forEach {
                    keysList.add(it.key)
                }
                onFaves(keysList)
            }
            .addOnFailureListener {
            }
    }

    fun getAllFavesBooks(
        onBooks: (List<Book>) -> Unit,
    ) {
        getAllFavesIds { idsList ->
            if (idsList.isNotEmpty()) {
                db.collection("guide_posts")
                    .whereIn(FieldPath.documentId(), idsList)
                    .get()
                    .addOnSuccessListener { task ->
                        val bookList = task.toObjects(Book::class.java).map {
                            if (idsList.contains(it.key)) {
                                it.copy(isFaves = true)
                            } else {
                                it
                            }
                        }
                        onBooks(bookList)
                    }
                    .addOnFailureListener {
                    }
            } else {
                onBooks(emptyList())
            }
        }
    }

    fun getAllBooksFromCategory(
        categoryIndex: Int,
        onBooks: (List<Book>) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        getAllFavesIds { idsList ->
            db.collection("guide_posts")
                .whereEqualTo("categoryIndex", categoryIndex)
                .get()
                .addOnSuccessListener { task ->
                    val bookList = task.toObjects(Book::class.java).map {
                        if (idsList.contains(it.key)) {
                            it.copy(isFaves = true)
                        } else {
                            it
                        }
                    }
                    onBooks(bookList)
                }
                .addOnFailureListener {
                    onFailure("Error")
                }
        }
    }


    fun getAllBooks(
        onBooks: (List<Book>) -> Unit,
    ) {
        getAllFavesIds { idsList ->
            db.collection("guide_posts")
                .get()
                .addOnSuccessListener { task ->
                    val bookList = task.toObjects(Book::class.java).map {
                        if (idsList.contains(it.key)) {
                            it.copy(isFaves = true)
                        } else {
                            it
                        }
                    }
                    onBooks(bookList)
                }
                .addOnFailureListener {
                }
        }
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
            favesDocRef
                .delete()
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
    ) {
        db.collection("guide_posts")
            .document(book.key)
            .delete()
            .addOnSuccessListener {
                onDeleted()
            }
            .addOnFailureListener {

            }
    }

    private fun getFavesCategoryReference(): CollectionReference {
        return db.collection("guide_users")
            .document(auth.uid ?: "")
            .collection("guide_faves")
    }


   private  fun saveBookToFireStore(
        book: Book,
        onSaved: () -> Unit,
        onError: () -> Unit,
    ) {
        //Куда соханять фото
        val db = db.collection("guide_posts")
        val key = if (book.key.isEmpty()) db.document().id else book.key
        db.document(key)
            .set(
                book.copy(key = key)
            ).addOnSuccessListener {
                onSaved()
            }
            .addOnFailureListener {
                onError()
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
           /* uploadImageToFirestore(
                oldImageUrl = oldImageUrl,
                uri = uri,
                book = book,
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError("Error save Image2")
                }
            )*/
        }
    }
}