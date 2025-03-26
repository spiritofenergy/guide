package com.kodex.guide.ui.utils.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.addscreen.data.Favorite
import javax.inject.Singleton

@Singleton
class FireStoreManager(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
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
     fun getAllBooksFromCategory (
         category: String,
        onBooks: (List<Book>)-> Unit
    ){
        getAllFavesIds {idsList->
            db.collection("guide_posts")
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener { task ->
                    val bookList = task.toObjects(Book::class.java).map {
                        if (idsList.contains(it.key)){
                            it.copy(isFaves = true)
                        }else{
                            it
                        }
                    }
                    onBooks(bookList)
                }
                .addOnFailureListener{
                }
        }
    }

     fun getAllBooks (
        onBooks: (List<Book>)-> Unit
    ){
        getAllFavesIds {idsList->
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
         isFav: Boolean
    ){
        val favesDocRef = getFavesCategoryReference()
            .document(favorite.key)
        if(isFav){
               favesDocRef.set(favorite)
        }else{
            favesDocRef
                .delete()
        }
    }
    fun changeFavesState(books: List<Book>, book: Book):List<Book>{
        return books.map{ bk ->
            if(bk.key == book.key){
                onFaves(
                    Favorite(bk.key),
                    !bk.isFaves
                )
                bk.copy(isFaves = !bk.isFaves)
            }else{
                bk
            }
        }
    }
    private fun getFavesCategoryReference(): CollectionReference{
       return db.collection("guide_users")
            .document(auth.uid ?: "")
            .collection("guide_faves")
    }
    fun deleteBook(book: Book){
        db.collection("guide_posts")
            .document(book.key)
            .delete()
    }


}