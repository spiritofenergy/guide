package com.kodex.guide.presentation.admin_panel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kodex.guide.ui.theme.PurpleGrey80


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ModerationScreen(
    viewModel: ModerationScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllComments()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(PurpleGrey80),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
        ) {
            items(viewModel.commentState.value) { comment ->
                AdminCommentListItem(
                    ratingData = comment,
                    onClickDecline = {
                        viewModel.deleteComment(comment.uid)
                    },
                    onClickAccept = {
                        viewModel.insertModerationRating(comment)
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        if (viewModel.commentState.value.isEmpty()) {
            Text(
                text = "No comments to moderation",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    }
}



@Composable
@Preview(showBackground = true)
fun ShowModerationCommentListItem(){




}