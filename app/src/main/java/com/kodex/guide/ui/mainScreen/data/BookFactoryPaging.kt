package com.kodex.guide.ui.mainScreen.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.utils.firebase.FireStoreManagerPaging
import java.io.IOException
import javax.inject.Inject

class BookFactoryPaging @Inject constructor(
    private val fireStoreManagerPaging: FireStoreManagerPaging
): PagingSource<DocumentSnapshot, Book>() {

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Book>)
    : DocumentSnapshot? {
        return null
    }

    override suspend fun load (params: LoadParams<DocumentSnapshot>)
    : LoadResult<DocumentSnapshot, Book> {
        try {
            val currentPage = params.key
            Log.d("MyLog", "Current page: $currentPage")

            val (snapshot, books) = fireStoreManagerPaging.nextPage(
                pageSize = params.loadSize.toLong(),
                currentKey = currentPage
            )
            Log.d("MyLog", "Book List size: ${books.size}")

            val prefKey = if (currentPage == null) null else snapshot.documents.firstOrNull()
            Log.d("MyLog", "PrefKey: ${prefKey?.id}")

            val nextKey = snapshot.documents.lastOrNull()
            Log.d("MyLog", "PrefKey: ${nextKey?.id}")

            return LoadResult.Page(
                data = books,
                prevKey = prefKey,
                nextKey = nextKey
            )
        }catch (e:IOException){
            return LoadResult.Error(e)
        }



    }
}