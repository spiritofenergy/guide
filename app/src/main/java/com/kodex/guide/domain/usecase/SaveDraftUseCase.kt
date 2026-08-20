package com.kodex.guide.domain.usecase

import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.repository.MyPostsRepo
import javax.inject.Inject

class SaveDraftUseCase @Inject constructor(private val repo: MyPostsRepo) {
    suspend operator fun invoke(book: Book): Result<Book> = repo.saveDraft(book)
}