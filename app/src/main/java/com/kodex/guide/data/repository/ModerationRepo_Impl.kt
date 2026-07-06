package com.kodex.guide.data.repository

import com.kodex.guide.data.mapper.toDTO
import com.kodex.guide.data.mapper.toRatingData
import com.kodex.guide.data.source.remote.ModerationDataSource
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.domain.repository.ModerationRepo
import javax.inject.Inject

class ModerationRepo_Impl @Inject constructor(
    private val moderationDataSource: ModerationDataSource
): ModerationRepo {
    override suspend fun acceptComment(ratingData: RatingData): Result<Unit> {
        return moderationDataSource.acceptComment(ratingData.toDTO())
    }

    override suspend fun getCommentsToModerate(): Result<List<RatingData>> {
        return moderationDataSource.getAllCommentsToModerate().map { list->
            list.map { ratingDataDTO ->
                ratingDataDTO.toRatingData()
            }
        }
    }

    override suspend fun deleteComment(uid: String): Result<Unit> {
        return moderationDataSource.deleteComment(uid)
    }
}