package com.kodex.guide.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.kodex.guide.data.mapper.toBookDTO
import com.kodex.guide.data.source.remote.FirebaseBooksDataSource
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.repository.MyPostsRepo
import com.kodex.guide.ui.db.RoomDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class MyPostsRepositoryImpl @Inject constructor(
    private val dao: RoomDao,
    private val remote: FirebaseBooksDataSource,
    private val auth: FirebaseAuth
) : MyPostsRepo {

    override fun observeMyPosts(uid: String): Flow<List<Book>> =
        dao.observeMyPosts(uid).flowOn(Dispatchers.IO)

    override suspend fun getPost(key: String): Result<Book?> = runCatching {
        withContext(Dispatchers.IO) { dao.getPostByKey(key) }
    }

    override suspend fun saveDraft(book: Book): Result<Book> = runCatching {
        withContext(Dispatchers.IO) {
            val uid = auth.uid ?: throw IllegalStateException("Нужен вход в аккаунт")
            val key = book.key.ifEmpty { UUID.randomUUID().toString() }
            val saved = book.copy(
                key = key,
                authorUid = uid,
                isAuthor = true,
                isUploaded = false,
                searchTitle = book.title.lowercase(),
                timeStamp = System.currentTimeMillis()
            )
            dao.insertPost(saved)
            saved
        }
    }

    override suspend fun upload(book: Book): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            remote.saveBook(book.toBookDTO()).getOrThrow()  // set() по key = create или update
            dao.setUploaded(book.key, true)
        }
    }

    override suspend fun delete(book: Book): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            if (book.isUploaded) remote.deleteBook(book.toBookDTO()).getOrThrow()
            dao.deleteByKey(book.key)
        }
    }
}