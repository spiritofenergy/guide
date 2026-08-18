package com.kodex.guide.data.repository

import com.kodex.guide.data.source.local.SavedPostsLocalSource
import com.kodex.guide.domain.model.Book
import com.kodex.guide.ui.db.RoomDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

    class SavedPostsLocalSourceImpl @Inject constructor(
        private val dao: RoomDao
    ) : SavedPostsLocalSource {

        override fun observePosts(): Flow<List<Book>> {
            return dao.getAllPosts()
                .flowOn(Dispatchers.IO)
        }

        override suspend fun exists(key: String): Boolean {
            return withContext(Dispatchers.IO) {
                dao.existsByKey(key)
            }
        }

        override suspend fun insert(book: Book) {
            withContext(Dispatchers.IO) {
                dao.insertPost(book)
            }
        }

        override suspend fun deleteByKey(key: String): Int {
            return withContext(Dispatchers.IO) {
                dao.deleteByKey(key)
            }
        }
    }
