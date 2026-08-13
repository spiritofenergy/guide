package com.kodex.guide.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import com.kodex.guide.data.model.BookFilter
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.domain.model.User
import kotlinx.coroutines.flow.Flow

interface  BooksRepo {
    fun getBooks(favsKeysList: List<String>, bookFilter: BookFilter): Flow<PagingData<Book>>
    suspend fun deleteBook(book: Book): Result<Unit>
    suspend fun saveBook(book: Book, uri: Uri?): Result<Unit>
    suspend fun submitUserRating(ratingData: RatingData, bookId: String): Result<Unit>
    suspend fun deleteComment(uid: String): Result<Unit>
    suspend fun getBookComments(bookId: String): Result<List<RatingData>>
    suspend fun getUserRating(bookId: String): Result<RatingData?>

    }