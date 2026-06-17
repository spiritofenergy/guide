package com.kodex.guide.domain.repository

import com.kodex.guide.domain.model.RatingData

interface ModerationRepo {
    suspend fun acceptComment(ratingData: RatingData): Result<Unit>
    suspend fun getCommentsToModerate(): Result <List<RatingData>>
    suspend fun deleteComment(uid: String): Result<Unit>
}