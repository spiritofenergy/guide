package com.kodex.guide

import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.repository.SavedPostsRepo
import com.kodex.guide.domain.usecase.ToggleSavedPostUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ToggleSavedPostUseCaseTest {

    private class FakeSavedPostsRepository : SavedPostsRepo {

        private val savedKeys = mutableSetOf<String>()

        override fun observeSavedPosts(): Flow<List<Book>> {
            return flowOf(emptyList())
        }

        override fun observeSavedKeys(): Flow<Set<String>> {
            return flowOf(savedKeys.toSet())
        }

        override suspend fun isSaved(key: String): Boolean {
            return savedKeys.contains(key)
        }

        override suspend fun save(book: Book) {
            savedKeys.add(book.key)
        }

        override suspend fun remove(key: String) {
            savedKeys.remove(key)
        }

        override suspend fun toggle(book: Book): Boolean {
            return if (isSaved(book.key)) {
                remove(book.key)
                false
            } else {
                save(book)
                true
            }
        }
    }

    @Test
    fun `toggle should save post when not saved and remove when already saved`() = runTest {
        val repository = FakeSavedPostsRepository()
        val useCase = ToggleSavedPostUseCase(repository)

        val book = Book().copy(key = "test-key")

        // Пост изначально не сохранен
        assertFalse(repository.isSaved(book.key))

        // Первый клик - сохраняем
        val firstResult = useCase(book)
        assertTrue(firstResult)
        assertTrue(repository.isSaved(book.key))

        // Второй клик - удаляем
        val secondResult = useCase(book)
        assertFalse(secondResult)
        assertFalse(repository.isSaved(book.key))
    }
}