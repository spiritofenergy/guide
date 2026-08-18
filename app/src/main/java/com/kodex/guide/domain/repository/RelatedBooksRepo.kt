package com.kodex.guide.domain.repository

import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.model.BookCategories

interface RelatedBooksRepo {
    suspend fun getRelatedBooks(category: BookCategories, excludeKey: String): Result<List<Book>>
}