package com.kodex.guide.domain.usecase

import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.repository.SavedPostsRepo
import javax.inject.Inject

class ToggleSavedPostUseCase @Inject constructor(
    private val repository: SavedPostsRepo
) {

    suspend operator fun invoke(book: Book): Boolean {
        return repository.toggle(book)
    }
}