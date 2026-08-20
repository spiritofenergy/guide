package com.kodex.guide

import com.google.firebase.auth.FirebaseAuth
import com.kodex.guide.data.repository.MyPostsRepositoryImpl
import com.kodex.guide.data.source.remote.FirebaseBooksDataSource
import com.kodex.guide.domain.model.Book
import com.kodex.guide.ui.db.RoomDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeRoomDao : RoomDao {
    val db = mutableMapOf<String, Book>()
    private val flow = MutableStateFlow<List<Book>>(emptyList())
    private fun refresh() { flow.value = db.values.toList() }

    override suspend fun insertPost(book: Book) { db[book.key] = book; refresh() }
    override suspend fun insertAllBooks(books: List<Book>) { books.forEach { db[it.key] = it }; refresh() }
    override fun getAllPosts(): Flow<List<Book>> = flow
    override fun getFavoriteBooks(): Flow<List<Book>> = flow.map { l -> l.filter { it.isFavorite } }
    override suspend fun existsByKey(key: String) = db.containsKey(key)
    override suspend fun deleteByKey(key: String): Int {
        val r = if (db.remove(key) != null) 1 else 0; refresh(); return r
    }
    override fun observeMyPosts(uid: String): Flow<List<Book>> =
        flow.map { l -> l.filter { it.authorUid == uid } }
    override suspend fun setUploaded(key: String, uploaded: Boolean) {
        db[key]?.let { db[key] = it.copy(isUploaded = uploaded) }; refresh() }
    override suspend fun getPostByKey(key: String) = db[key]
}

class MyPostsRepositoryImplTest {
    private val dao = FakeRoomDao()
    private val remote: FirebaseBooksDataSource = mockk()
    private val auth: FirebaseAuth = mockk { every { uid } returns "user1" }
    private val repo = MyPostsRepositoryImpl(dao, remote, auth)

    @Test
    fun `saveDraft генерирует key и проставляет флаги`() = runTest {
        val saved = repo.saveDraft(Book(title = "Тест")).getOrThrow()
        assertTrue(saved.key.isNotEmpty())
        assertEquals("user1", saved.authorUid)
        assertFalse(saved.isUploaded)
        assertEquals(saved, dao.db[saved.key])
    }

    @Test
    fun `saveDraft без uid возвращает ошибку`() = runTest {
        val anonRepo = MyPostsRepositoryImpl(dao, remote, mockk { every { uid } returns null })
        assertTrue(anonRepo.saveDraft(Book()).isFailure)
    }

    @Test
    fun `upload пишет в Firebase и помечает isUploaded`() = runTest {
        coEvery { remote.saveBook(any()) } returns Result.success(Unit)
        val saved = repo.saveDraft(Book(title = "Т")).getOrThrow()
        repo.upload(saved).getOrThrow()
        coVerify(exactly = 1) { remote.saveBook(any()) }
        assertTrue(dao.db[saved.key]!!.isUploaded)
    }

    @Test
    fun `delete черновика НЕ трогает Firebase`() = runTest {
        val saved = repo.saveDraft(Book(title = "Т")).getOrThrow()
        repo.delete(saved).getOrThrow()
        coVerify(exactly = 0) { remote.deleteBook(any()) }
        assertNull(dao.db[saved.key])
    }

    @Test
    fun `delete опубликованного удаляет и из Firebase`() = runTest {
        coEvery { remote.saveBook(any()) } returns Result.success(Unit)
        coEvery { remote.deleteBook(any()) } returns Result.success(Unit)
        val saved = repo.saveDraft(Book(title = "Т")).getOrThrow()
        repo.upload(saved).getOrThrow()
        repo.delete(saved).getOrThrow()
        coVerify(exactly = 1) { remote.deleteBook(any()) }
        assertNull(dao.db[saved.key])
    }
}