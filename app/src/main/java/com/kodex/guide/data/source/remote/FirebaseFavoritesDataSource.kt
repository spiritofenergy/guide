package com.kodex.guide.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.domain.model.Favorite
import com.kodex.guide.data.source.remote.FirebaseConst.USERS
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton
import kotlin.jvm.java

@Singleton
class FirebaseFavoritesDataSource (
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,

){
     suspend fun getIdsFavesList(): Result<List<String>> {
        try {
            val snapshot = getFavesCategoryReference().get().await()
            val idsList = snapshot.toObjects(Favorite::class.java)
            val keysList = arrayListOf<String>()

            idsList.forEach {
                keysList.add(it.key)
            }
            return Result.success(if ( keysList.isEmpty()) emptyList() else keysList)
        }catch (e: Exception){
           return Result.failure(e)
            }
        }

    private fun getFavesCategoryReference(): CollectionReference {
        return db.collection(USERS)
            .document(auth.uid ?: "")
            .collection(FirebaseConst.FAVORITES)
    }

    suspend fun onFavorites(
        favorite: Favorite,
        isFav: Boolean,
    ) : Result<Unit>{
        try {
            val favesDokRef = getFavesCategoryReference()
                .document(favorite.key)
            if (isFav) {
                favesDokRef.set(favorite).await()
            } else {
                favesDokRef.delete().await()
            }
            return Result.success(Unit)
        }catch (e: Exception){
            return Result.failure(e)
        }
    }


  /*  fun changeFavesState(books: List<Book>, book: Book): List<Book> {
        return books.map { bk ->
            if (bk.key == book.key) {
                onFavorites(
                    Favorite(bk.key),
                    !bk.isFavorite
                )
                bk.copy(isFavorite = !bk.isFavorite)
            } else {
                bk
            }
        }
    }*/
}