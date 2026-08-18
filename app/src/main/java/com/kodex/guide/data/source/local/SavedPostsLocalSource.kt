package com.kodex.guide.data.source.local

import com.kodex.guide.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface SavedPostsLocalSource {
    fun observePosts(): Flow<List<Book>>

    suspend fun exists(key: String): Boolean

    suspend fun insert(book: Book)

    suspend fun deleteByKey(key: String): Int
}