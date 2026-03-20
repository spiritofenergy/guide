package com.kodex.guide.ui.detailScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.theme.ButtonColor
import com.kodex.guide.ui.theme.Orange
import com.kodex.guide.ui.utils.toFormattedDate


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
 @Composable
fun DetailScreen2(
    navObject: DetailNavObject = DetailNavObject(),
   // viewModel: DetailsScreenViewModel = hiltViewModel()

) {

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        var showReteDialog by remember { mutableStateOf(false) }
        var showCommentDialog by remember { mutableStateOf(false) }
      //  var ratingDataToShow by remember { mutableStateOf(RatingData()) }

        var bitmap: Bitmap? = null
        try {
            val base64Image = Base64.decode(navObject.imageUrl, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(
                base64Image,0,
                base64Image.size
            )
        }catch (e:IllegalArgumentException){

        }
        RateDialog(
           // ratingData = viewModel.ratingDataState.value ?: RatingData(),
            onDismiss = {
                showReteDialog = false
            },
            onSubmit = { rating, message ->
                val ratingData = RatingData(
                    name = "",
                    rating = rating,
                    message = message,
                   // lastRating = viewModel.ratingDataState.value?.rating ?: 0
                )
                //viewModel.insertRating(ratingData, navObject.bookId)
                showReteDialog = false
            },
            show = showReteDialog,
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)){
                AsyncImage(
                    model = bitmap,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth(0.6F)
                        .height(190.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray
                        ),
                    contentScale = ContentScale.FillHeight
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column (modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start)
                {

                    Text(text = "Категория:",
                        color = Color.Gray)
                    Text(
                        text = stringArrayResource(R.array.category_array)[navObject.categoryIndex],
                        fontWeight = FontWeight.Bold)
                    Text(text = "Автор:",
                        color = Color.Gray)
                    Text(
                        text = "Роман:",
                        fontWeight = FontWeight.Bold)
                    Text(text = "Дата печати:",
                        color = Color.Gray)
                    Text(
                        text = navObject.timestamp.toFormattedDate(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp

                    )
                    Text(text = "Оценка:",
                        color = Color.Gray)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =  Arrangement.Center
                    ) {
                        // text = viewModel.ratingState.value,
                       // if (viewModel.commentState.value.isNotEmpty()) {
                            // if (navObject.ratingsList.isNotEmpty()) {
                            Log.d("MyLog", "DetailScreen ratingsList: ${navObject.ratingsList}")
                            Text(
                                // text = String.format("%.1f",viewModel.commentState.value
                                text = String.format("%.1f", navObject.ratingsList.average(),                                       // "(${navObject.ratingsList.size})"
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                      //  } else {
                            Text(text = "Нет оценок")
                        }

                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Orange

                        )
                   // }

                }
            }
            Text(
                text = navObject.title,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.fillMaxWidth().padding(10.dp))
            Text(
                text = navObject.description,
               // text = "«Властелин колец» является одним из самых крупных проектов в истории кино. Его реализация заняла восемь лет; все три фильма были сняты одновременно в Новой Зеландии, родной стране Питера Джексона. У каждого из фильмов трилогии есть специальная расширенная версия, выпущенная на DVD спустя год после выхода соответствующей театральной версии. Фильмы следуют за основной сюжетной линией книги, но опускают некоторые существенные элементы повествования, включают дополнения и отклонения от исходного материала.",
                fontSize = 16.sp
            )
        }
        RateDialog()

        Spacer(modifier = Modifier.width(26.dp))
        Row(modifier = Modifier.fillMaxWidth()) {

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                onClick = {
                  //  viewModel.getUserRating(bookId = navObject.bookId)
                    showReteDialog = true

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                )
            )
            {
                Text(text = "Оценка")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),

                onClick = {

                }, colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonColor
                )
            )
            {
                Text(text = "${navObject.price} Купить сейчас")


                //Toast.makeText(context, "Опция в разрабоке")
            }
        }


        Spacer(modifier = Modifier.width(50.dp))
        Text(
            text = navObject.title,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
        Text(
            text = stringArrayResource(id = R.array.category_array)[navObject.categoryIndex],
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(
                text = navObject.description, fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(10.dp))

        /*if (viewModel.commentState.value.isNotEmpty()) {
            Text(
                text = "Коментарии",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(Modifier.height(10.dp))
            LazyRow(modifier = Modifier
                .fillMaxWidth()
                .weight(0.3F)) {
                items(viewModel.commentState.value) { ratingData ->
                    CommentListItem(
                        onClick = { rData->
                            showCommentDialog = true
                            ratingDataToShow = rData
                        },
                        ratingData = ratingData
                    )
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp))
                }

            }
        }*/

    }
    Spacer(modifier = Modifier.width(5.dp))
}




