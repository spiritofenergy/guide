package com.kodex.guide.domain.repository

import com.kodex.guide.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface SavedPostsRepo {
    fun observeSavedPosts(): Flow<List<Book>>

    fun observeSavedKeys(): Flow<Set<String>>

    suspend fun isSaved(key: String): Boolean

    suspend fun save(book: Book)

    suspend fun remove(key: String)

    suspend fun toggle(book: Book): Boolean
}