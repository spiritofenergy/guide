package com.kodex.guide.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.android.play.core.integrity.d
import com.kodex.guide.data.mapper.toBookDTO
import com.kodex.guide.data.mapper.toDTO
import com.kodex.guide.data.mapper.toRatingData
import com.kodex.guide.data.model.BookFilter
import com.kodex.guide.data.source.remote.BooksFirebaseRemoteDataSource
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.domain.repository.BooksRepo
import com.kodex.guide.presentation.home.BookFactoryPaging
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BooksRepo_Impl @Inject constructor(
    private val dataSource: BooksFirebaseRemoteDataSource,

): BooksRepo{
    override fun getBooks(favsKeysList: List<String>, bookFilter: BookFilter): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = 16,
                prefetchDistance = 3,
                initialLoadSize = 30,
            ),
            pagingSourceFactory = { BookFactoryPaging(
                dataSource,
                favsKeysList,
                bookFilter,
                )
            }
        ).flow
    }

    override suspend fun deleteBook(book: Book): Result<Unit> {
        return dataSource.deleteBook(book.toBookDTO())
    }

    override suspend fun saveBook(book: Book): Result<Unit> {
        return dataSource.saveBook(book.toBookDTO())
    }

    override suspend fun submitUserRating(
        ratingData: RatingData,
        bookId: String
    ): Result<Unit> {
        return dataSource.submitUserRating(ratingData.toDTO(), bookId)
    }

    override suspend fun deleteComment(uid: String): Result<Unit> {
        return dataSource.deleteComment(uid)
    }

    override suspend fun getBookComments(bookId: String): Result<List<RatingData>> {
        return dataSource.getBookComments(bookId).map { list->
            list.map {
                rData -> rData.toRatingData()
            }
        }
    }

    override suspend fun getUserRating(bookId: String): Result<RatingData?> {
        return dataSource.getUserRating(bookId).map {
            it?.toRatingData()
        }
    }
}