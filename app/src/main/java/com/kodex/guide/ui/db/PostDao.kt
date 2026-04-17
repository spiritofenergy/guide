package com.kodex.guide.ui.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kodex.guide.ui.addscreen.data.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postData: Book)

    @Delete
    suspend fun deletePost(postData: Book)

    @Query("SELECT * FROM books")
    fun getAllPost(): Flow<List<Book>>


}