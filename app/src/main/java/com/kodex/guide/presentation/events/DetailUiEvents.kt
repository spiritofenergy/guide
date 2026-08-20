package com.kodex.guide.presentation.events

import com.kodex.guide.domain.model.RatingData

sealed class DetailUiEvents {
    sealed class DetailUiEvent {
        data class CommentDialogEvent(
            val show: Boolean,
            val ratingData: RatingData?,
        ): DetailUiEvent()

        data class ShowUserRatingDialogEvent(
            val bookId: String,
        ): DetailUiEvent()

        data class InsertRatingDialogEvent(
            val ratingData: RatingData,
            val bookId: String,
        ): DetailUiEvent()

        data class GetCommentsEvent(
            val bookId: String,
        ): DetailUiEvent()

        object HideUserRatingDialog: DetailUiEvent()
    }

}