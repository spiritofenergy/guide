package com.kodex.guide.domain.usecase

import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.model.BookCategories
import com.kodex.guide.domain.repository.RelatedBooksRepo
import javax.inject.Inject

class GetRelatedBooksUseCase @Inject constructor(
    private val repository: RelatedBooksRepo
) {
    suspend operator fun invoke(
        category: BookCategories,
        excludeKey: String,
        limit: Int = 10
    ): Result<List<Book>> =
        repository.getRelatedBooks(category, excludeKey)
            .map { list -> list.take(limit) }
}