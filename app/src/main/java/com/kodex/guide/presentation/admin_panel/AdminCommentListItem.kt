package com.kodex.guide.presentation.admin_panel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.ui.detailScreen.StarsIndicator
import com.kodex.guide.presentation.login.LoginButton
import com.kodex.guide.ui.theme.DarkWhite
import com.kodex.guide.utils.toFormattedDate

 @RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminCommentListItem(
    ratingData: RatingData = RatingData(),
    onClickDecline: (RatingData) -> Unit = {},
    onClickAccept: (RatingData) -> Unit = {}

    ) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = DarkWhite
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            StarsIndicator(rating = ratingData.rating ?: 1)

            Text(
                text = ratingData.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1

            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ratingData.message,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp

            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ratingData.timestamp.toFormattedDate(),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,

            )
             Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                LoginButton(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                        .padding(5.dp),
                    text = "Decline") {
                        onClickDecline(ratingData)
                }
                Spacer(modifier = Modifier.width(10.dp))
                LoginButton(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                        .padding(5.dp),
                     text = "Accept") {
                        onClickAccept(ratingData)
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun ShowAdminCommentListItem() {
    AdminCommentListItem(
        ratingData = RatingData(
            name = "Maric",
            rating = 4,
            message = "Very good!"
        ),
        onClickDecline = {},
        onClickAccept = {},

    )
}

