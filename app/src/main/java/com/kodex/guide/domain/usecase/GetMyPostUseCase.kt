package com.kodex.guide.domain.usecase

import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.repository.MyPostsRepo
import javax.inject.Inject

class GetMyPostUseCase @Inject constructor(private val repo: MyPostsRepo) {
    suspend operator fun invoke(key: String): Result<Book?> = repo.getPost(key)
}