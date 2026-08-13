package com.kodex.guide.presentation.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kodex.guide.data.mapper.toBook
import com.kodex.guide.data.model.BookFilter
import com.kodex.guide.data.source.remote.FirebaseBooksDataSource
import com.kodex.guide.domain.model.Book
import java.io.IOException
import javax.inject.Inject

class BookFactoryPaging @Inject constructor(
    private val dataSource: FirebaseBooksDataSource,
    private val keysFavsList: List<String>,
    private val bookFilter: BookFilter,
    ): PagingSource<String, Book>() {

    override fun getRefreshKey(state: PagingState<String, Book>)
    : String? {
        return null
    }

    override suspend fun load (params: LoadParams<String>)
    : LoadResult<String, Book> {
        try {
            val currentPage = params.key
            val booksPageDTO = dataSource.nextPage(
                keysFavesList = keysFavsList,
                pageSize = params.loadSize.toLong(),
                currentKey = currentPage,
                bookFilter = bookFilter,
            )
            return LoadResult.Page(
                data = booksPageDTO.books.map { it.toBook() },
                prevKey = null,
                nextKey = booksPageDTO.listItemId
            )
        }catch (e: IOException){
            return LoadResult.Error(e)
        }
    }
}