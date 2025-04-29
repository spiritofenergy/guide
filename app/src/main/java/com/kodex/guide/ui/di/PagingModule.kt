package com.kodex.guide.ui.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.mainScreen.data.BookFactoryPaging
import com.kodex.guide.ui.utils.firebase.FireStoreManagerPaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(ViewModelComponent::class)
object PagingModule {

    @Provides
    @ViewModelScoped
    fun providePagingFlow(
        fireStoreManagerPaging: FireStoreManagerPaging
    ): Flow<PagingData<Book>>{
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                prefetchDistance = 3,
                initialLoadSize = 30,
            ),
            pagingSourceFactory = { BookFactoryPaging(fireStoreManagerPaging) }
        ).flow
    }
}