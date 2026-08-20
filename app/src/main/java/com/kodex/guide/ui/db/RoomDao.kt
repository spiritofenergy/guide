package com.kodex.guide.ui.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kodex.guide.domain.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(book: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBooks(books: List<Book>)

    @Query("SELECT * FROM books")
    fun getAllPosts(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE isFavorite = 1")
    fun getFavoriteBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE authorUid = :uid ORDER BY timeStamp DESC")
    fun observeMyPosts(uid: String): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE `key` = :key")
    suspend fun getPostByKey(key: String): Book?

    @Query("UPDATE books SET isUploaded = :uploaded WHERE `key` = :key")
    suspend fun setUploaded(key: String, uploaded: Boolean)
    @Query("SELECT EXISTS(SELECT 1 FROM books WHERE `key` = :key)")
    suspend fun existsByKey(key: String): Boolean

    @Query("DELETE FROM books WHERE `key` = :key")
    suspend fun deleteByKey(key: String): Int

}
