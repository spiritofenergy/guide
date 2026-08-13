package com.kodex.guide.data.repository

import com.kodex.guide.data.mapper.toDTO
import com.kodex.guide.data.mapper.toRatingData
import com.kodex.guide.data.source.remote.FirebaseModerationDataSource
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.domain.repository.ModerationRepo
import javax.inject.Inject

class ModerationRepoImpl @Inject constructor(
    private val firebaseModerationDataSource: FirebaseModerationDataSource
): ModerationRepo {
    override suspend fun acceptComment(ratingData: RatingData): Result<Unit> {
        return firebaseModerationDataSource.acceptComment(ratingData.toDTO())
    }

    override suspend fun getCommentsToModerate(): Result<List<RatingData>> {
        return firebaseModerationDataSource.getAllCommentsToModerate().map { list->
            list.map { ratingDataDTO ->
                ratingDataDTO.toRatingData()
            }
        }
    }

    override suspend fun deleteComment(uid: String): Result<Unit> {
        return firebaseModerationDataSource.deleteComment(uid)
    }
}