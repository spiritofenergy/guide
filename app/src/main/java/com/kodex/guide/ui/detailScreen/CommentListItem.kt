package com.kodex.guide.ui.detailScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.ui.theme.DarkWhite
import com.kodex.guide.utils.toFormattedDate


 @RequiresApi(Build.VERSION_CODES.O)
 @Composable
fun CommentListItem(
     onClick: (RatingData) -> Unit,
     ratingData: RatingData

) {
    Card(
        modifier = Modifier
            .width(250.dp)
            .height(130.dp)
            .clickable {
                onClick(ratingData)
            },
        colors = CardDefaults.cardColors(
            containerColor = DarkWhite
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
        ) {
            StarsIndicator(rating = ratingData.rating)
            Spacer(modifier = Modifier.width(2.dp))
        }

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ){
            Text(
                text = ratingData.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1

            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = ratingData.message,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis

            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = ratingData.timestamp.toFormattedDate(),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis

            )
        }

    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun ShowCommentList(){
    CommentListItem(
        onClick = {},
        ratingData = RatingData(
            name = "User",
            rating = 5,
            message = "Very good book",


            )
    )

}
