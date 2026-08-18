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

  /*  @Delete
    suspend fun deletePost(book: Book)
*/
    @Query("SELECT * FROM books")
    fun getAllPosts(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE isFavorite = 1")
    fun getFavoriteBooks(): Flow<List<Book>>

   /* // Новый метод для проверки существования
    @Query("SELECT EXISTS(SELECT 1 FROM books WHERE id = :bookId)")
    suspend fun isPostExists(bookId: String): Boolean
    */
    // Или так:
  /*  @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getPostById(bookId: String): Book?
*/
    @Query("SELECT EXISTS(SELECT 1 FROM books WHERE `key` = :key)")
    suspend fun existsByKey(key: String): Boolean

    @Query("DELETE FROM books WHERE `key` = :key")
    suspend fun deleteByKey(key: String): Int

}
