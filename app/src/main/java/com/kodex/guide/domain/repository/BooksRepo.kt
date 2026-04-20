package com.kodex.guide.domain.repository

import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.domain.model.User

interface BooksRepo {
    suspend fun deleteBook(book: Book): Result<Unit>
    suspend fun saveBook(book: Book): Result<Unit>
    suspend fun submitUserRating(ratingData: RatingData, bookId: String): Result<Unit>
    suspend fun deleteComment(uid: String): Result<Unit>
    suspend fun getBookComments(bookId: String): Result<List<RatingData>>
    suspend fun getUserRating(bookId: String): Result<RatingData?>

    }