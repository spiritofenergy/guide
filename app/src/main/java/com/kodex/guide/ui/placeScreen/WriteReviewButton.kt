package com.kodex.guide.ui.placeScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.ui.detailScreen.DetailsScreenViewModel
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.ui.dialods.DialogComments
import com.kodex.guide.ui.dialods.DialogRating
import com.kodex.guide.ui.theme.ButtonColor
import com.kodex.guide.ui.theme.ButtonColorBlue
import com.kodex.guide.ui.theme.DrawerColorBlue

@Composable
fun WriteReviewButton(
    navObject: NavRoutes.DetailNavObject = NavRoutes.DetailNavObject(),
    viewModel: DetailsScreenViewModel = viewModel(),
) {
    var showRateDialog by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
   // val ratingDataToShow = viewModel.commentState.value
    var ratingDataToShow by remember { mutableStateOf(RatingData()) }

    DialogRating(
       // ratingData = viewModel.ratingDataState.value ?: RatingData(),
        onDismiss = {
            showRateDialog = false
        },
        onSubmit = { rating, message ->
            val ratingData = RatingData(
                name = "",
                rating = rating,
                message = message,
                //lastRating = viewModel.ratingDataState.value?.rating ?: 0,
             )
          //  viewModel.insertRating(ratingData, navObject.bookId)
            showRateDialog = false
        },
        show = showRateDialog
    )
    DialogComments(
        showDialog = showCommentDialog,
        onDismiss = {
            showCommentDialog = false
        },
        ratingData = ratingDataToShow,
        onConfirm = {
            showCommentDialog = false
        }
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        OutlinedButton(
            onClick = { showRateDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DrawerColorBlue
            )

        ) {
            Icon(
                Icons.Filled.RateReview,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Написать отзыв")
        }
    }
}
@Composable
@Preview(showBackground = true, device = "id:pixel_6")
fun ShowWriteReviewButton() {
    WriteReviewButton()
}
