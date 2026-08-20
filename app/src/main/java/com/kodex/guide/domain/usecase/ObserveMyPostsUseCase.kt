package com.kodex.guide.domain.usecase

import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.repository.MyPostsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMyPostsUseCase @Inject constructor(private val repo: MyPostsRepo) {
    operator fun invoke(uid: String): Flow<List<Book>> = repo.observeMyPosts(uid)
}