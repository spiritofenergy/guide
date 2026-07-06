package com.kodex.guide.presentation.details.parallaxScreen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kodex.bookmarketcompose.R
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.presentation.detailScreen.CommentDialog
import com.kodex.guide.presentation.detailScreen.CommentListItem
import com.kodex.guide.presentation.detailScreen.DetailsScreenViewModel
import com.kodex.guide.presentation.detailScreen.RateDialog
import com.kodex.guide.presentation.events.DetailUiEvents
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.ui.theme.ButtonColorBlue
import com.kodex.guide.ui.theme.Orange
import com.kodex.guide.utils.toFormattedDate


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DefaultLocale")
@Preview(showBackground = true)
@Composable
fun DetailScreen(
    onCommentClick: (NavRoutes.CommentsNavData) -> Unit = {},
    navObject: NavRoutes.DetailNavObject = NavRoutes.DetailNavObject(),
    viewModel: DetailsScreenViewModel = hiltViewModel()
) {


    //val context = application.
    var showReteDialog by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
    var ratingDataToShow by remember { mutableStateOf(RatingData()) }

    val uiState = viewModel.uiState.collectAsState()
    var bitmap: Bitmap? = null
    try {
        val base64Image = Base64.decode(navObject.imageUrl, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(
            base64Image, 0,
            base64Image.size
        )
    } catch (e: IllegalArgumentException) {

    }

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(DetailUiEvents.DetailUiEvent.GetCommentsEvent(navObject.bookId))
    }

    RateDialog(
        ratingData = uiState.value.ratingData,
        onDismiss = {
            viewModel.onEvent(DetailUiEvents.DetailUiEvent.HideUserRatingDialog)
        },
        onSubmit = { ratingData ->
            viewModel.onEvent(
                DetailUiEvents.DetailUiEvent.InsertRatingDialogEvent(
                    ratingData, navObject.bookId))
        },
        show = uiState.value.showRateDialog,
        )

    CommentDialog(
        showDialog = uiState.value.showCommentDialog,
        onDismiss = {
            viewModel.onEvent(
                DetailUiEvents.DetailUiEvent.CommentDialogEvent(
                false,
                null
            ))
        },
        ratingData = uiState.value.ratingDataToShow
    )
    // Information
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(top = 25.dp))
        {
            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = bitmap,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth(0.7F)
                        .padding(top = 10.dp, bottom = 20.dp)
                        .height(190.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.FillHeight
                )
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Категория:",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Text(
                        text = stringArrayResource(id = R.array.category_array)[navObject.categoryIndex.id],
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Автор:",
                        color = Color.Gray,
                        fontSize = 16.sp

                    )
                    if (navObject.author.isNotEmpty()){
                        Text(
                            text = navObject.author,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }else{
                        Text(
                            text = "Admin",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp

                        )
                    }


                    Text(
                        text = "Дата:",
                        color = Color.Gray,
                        fontSize = 16.sp

                    )
                    Text(
                        text = navObject.timestamp.toFormattedDate().toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp

                    )
                    Text(
                        text = "Оценка",
                        color = Color.Gray,
                        fontSize = 16.sp

                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable{
                                onCommentClick(
                                    NavRoutes.CommentsNavData(
                                        bookId = navObject.bookId,
                                        title = navObject.title,
                                        ratingsList = navObject.ratingsList
                                    )
                                )
                        },
                        horizontalArrangement =  Arrangement.Center
                    ) {

                        if (uiState.value.comments.isNotEmpty()) {
                            Log.d("MyLog", "DetailScreen ratingsList: ${navObject.ratingsList}")
                            Text(text = String.format("%.1f", navObject.ratingsList.average(),                                     // "(${navObject.ratingsList.size})"
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        } else {
                            Text(text = "Нет оценок")
                        }

                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Orange

                        )
                    }
                }
            }
            //Button
            Spacer(modifier = Modifier.width(26.dp))
            Row(modifier = Modifier.fillMaxWidth()) {

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F),
                    onClick = {
                        viewModel.onEvent(
                            DetailUiEvents.DetailUiEvent.ShowUserRatingDialogEvent(navObject.bookId ))
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
                        viewModel.onEvent(
                            DetailUiEvents.DetailUiEvent.CommentDialogEvent(
                            true,
                           // ratingData = RatingData() // новый объект
                           uiState.value.ratingData,

                        ))

                    }, colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonColorBlue
                    )
                )
                {
                    Text(text = "${navObject.price} Купить сейчас")


                    //Toast.makeText(context, "Опция в разрабоке")
                }
            }

            //Title
            Spacer(modifier = Modifier.width(50.dp))
            Text(
                text = navObject.title,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Text(
                text = stringArrayResource(id = R.array.category_array)[navObject.categoryIndex.id],
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp))
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())) {
                Text(
                    text = navObject.description, fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            //Comments
            if (uiState.value.comments.isNotEmpty()) {
                Text(
                    text = "Коментарии",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(10.dp))
                LazyRow(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3F)) {
                    items(uiState.value.comments) { ratingData ->
                        CommentListItem(
                            onClick = { rData ->
                                //showCommentDialog = true
                                //ratingDataToShow = rData
                            },
                            ratingData = ratingData
                        )
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(5.dp))
    }
}





