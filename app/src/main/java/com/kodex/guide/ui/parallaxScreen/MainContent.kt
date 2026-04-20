package com.kodex.guide.ui.parallaxScreen

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kodex.bookmarketcompose.R
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.ui.detailScreen.CommentListItem
import com.kodex.guide.ui.detailScreen.DetailsScreenViewModel
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.ui.dialods.DialogComments
import com.kodex.guide.ui.dialods.DialogRating
import com.kodex.guide.ui.theme.ButtonColorBlue
import com.kodex.guide.ui.theme.Orange


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(
    navObject: NavRoutes.ParallaxNavObject,
    onNavigateToReviews: () -> Unit,
    viewModel: DetailsScreenViewModel = viewModel()
) {
    var showRateDialog by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
    var ratingDataToShow by remember { mutableStateOf(RatingData()) }
    val context = LocalContext.current
    val telephone = "+79197716667"

    DialogRating(
        ratingData = viewModel.ratingDataState.value ?: RatingData(),
        onDismiss = { showRateDialog = false },
        onSubmit = { rating, message ->
            val ratingData = RatingData(
                name = "",
                rating = rating,
                message = message,
                lastRating = viewModel.ratingDataState.value?.rating ?: 0
            )
            viewModel.insertRating(ratingData, navObject.bookId)
            showRateDialog = false
        },
        show = showRateDialog
    )

    DialogComments(
        showDialog = showCommentDialog,
        onDismiss = { showCommentDialog = false },
        ratingData = ratingDataToShow,
        onConfirm = { showCommentDialog = false }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Название и цена
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = navObject.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${navObject.price} р",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Рейтинг
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.clickable { onNavigateToReviews() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Orange
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    val averageRating = if (navObject.ratingsList.isNotEmpty()) {
                        navObject.ratingsList.average()
                    } else 0.0

                    Text(
                        text = String.format("%.1f", averageRating),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "(${navObject.ratingsList.size})",
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp
                    )
                }
            }

            // Статус работы
            StatusChip(isOpen = navObject.isOpenNow)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Адрес
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = navObject.address,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Часы работы и телефон
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Часы работы
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = navObject.openingHours,
                        fontSize = 14.sp,
                        color = if (navObject.isOpenNow)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                    if (!navObject.isOpenNow) {
                        Text(
                            text = "Закрыто",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Телефон
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$telephone")
                    }
                    context.startActivity(intent)
                }
            ) {
                Icon(
                    Icons.Default.Call,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = telephone,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопки
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = {
                    viewModel.getUserRating(bookId = navObject.bookId)
                    showRateDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Orange)
            ) {
                Text(text = "Оценка")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$telephone")
                    }
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColorBlue)
            ) {
                Text(text = "Позвонить")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Описание
        Text(
            text = navObject.description,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Заголовок отзывов
        Text(
            text = stringResource(R.string.comments),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Отзывы в виде горизонтального списка
        if (viewModel.commentState.value.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp) // Фиксированная высота для LazyRow
            ) {
                items(viewModel.commentState.value) { ratingData ->
                    CommentListItem(
                        onClick = { rData ->
                            showCommentDialog = true
                            ratingDataToShow = rData
                        },
                        ratingData = ratingData
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        } else {
            // Показываем сообщение, если нет отзывов
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет отзывов. Будьте первым!",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }
    }
}