package com.kodex.guide.domain.usecase

import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.repository.SavedPostsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSavedPostsUseCase @Inject constructor(
    private val repository: SavedPostsRepo
    ) {
    operator fun invoke(): Flow<List<Book>> {
        return repository.observeSavedPosts()
    }
}
