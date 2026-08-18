package com.kodex.guide.presentation.details.parallaxScreen


import android.R.attr.navigationIcon
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kodex.bookmarketcompose.R
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.presentation.detailScreen.CommentListItem
import com.kodex.guide.presentation.detailScreen.DetailsScreenViewModel
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.presentation.events.DetailUiEvents
import com.kodex.guide.presentation.placeScreen.InfoRow
import com.kodex.guide.ui.dialods.DialogComments
import com.kodex.guide.ui.dialods.DialogRating
import com.kodex.guide.ui.theme.ButtonColorBlue
import com.kodex.guide.ui.theme.DrawerColorBlue
import com.kodex.guide.ui.theme.Orange
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.nio.ByteOrder

// Цвета по образцу
private val PrimaryDark = Color(0xFF3B2F8F) // Темно-синий/фиолетовый
private val PrimaryLight = Color(0xFF5B4FCF) // Светлее
private val AccentPurple = Color(0xFF6C5CE7)
private val BackgroundWhite = Color(0xFFFFFFFF)
private val TextDark = Color(0xFF1A1A2E)
private val TextGray = Color(0xFF6B7280)
private val DividerColor = Color(0xFFE5E7EB)
private val IconBgLight = Color(0xFFF0EDFF)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(
    onCommentClick: (NavRoutes.CommentsNavData) -> Unit = {},
    navObject: NavRoutes.ParallaxNavObject,
    onNavigateToReviews: () -> Unit,
    onShowMapClick: () -> Unit,
    viewModel: DetailsScreenViewModel = viewModel(),
    onBackPressed: () -> Unit,

) {
    var showRateDialog by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val telephone = navObject.telephone
    val scope = rememberCoroutineScope()

    var bitmap: Bitmap? = null

    try {
        val base64Image = Base64.decode(navObject.imageUrl, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(base64Image, 0, base64Image.size)
    } catch (e: IllegalArgumentException) {
    }
    DialogRating(
        ratingData = uiState.value.ratingData,
        onDismiss = { viewModel.onEvent(DetailUiEvents.DetailUiEvent.HideUserRatingDialog) },
        onSubmit = { ratingData ->
            viewModel.onEvent(
                DetailUiEvents.DetailUiEvent.InsertRatingDialogEvent(ratingData, navObject.bookId)
            )
        },
        show = uiState.value.showRateDialog,
    )

    DialogComments(
        showDialog = uiState.value.showCommentDialog,
        onDismiss = { showCommentDialog = false },
        ratingData = uiState.value.ratingDataToShow,
        onConfirm = { showCommentDialog = false }
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // ===== ВЕРХНЯЯ ЧАСТЬ: Фото как фон =====
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {

            // Фото на весь фон
            AsyncImage(
                model = bitmap,
                contentDescription = navObject.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Затемнение снизу для читаемости текста
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 150f
                        )
                    )
            )
            // ===== ВЕРХНЯЯ ПАНЕЛЬ: Стрелка назад + Категория =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка назад (БЕЛАЯ)
                IconButton(
                    onClick = onBackPressed, // <-- НАВИГАЦИЯ НАЗАД
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color.Black.copy(alpha = 0.3f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = Color.White, // <-- БЕЛЫЙ ЦВЕТ
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                // Категория по центру
                Text(
                    text = stringArrayResource(id = R.array.category_array)[navObject.categoryIndex.id],
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Контент поверх фото (внизу) - ВСЁ внутри одного Column
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {

                // Кнопки действий - ТОЖЕ внутри Column
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionButton(
                        icon = Icons.Default.Email,
                        label = "Email",
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:ooozeeya@yandex.ru")
                            }
                            context.startActivity(intent)
                        }
                    )
                    ActionButton(
                        icon = Icons.Default.Call,
                        label = "Позвонить",
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$telephone")
                            }
                            context.startActivity(intent)
                        }
                    )
                    ActionButton(
                        icon = Icons.Default.Share,
                        label = "Поделиться",
                        onClick = {
                            scope.launch {
                                viewModel.sharePlace(
                                    context = context,
                                    place = navObject,
                                    coroutineScope = scope
                                )
                            }
                        }
                    )
                    ActionButton(
                        icon = Icons.Default.Star,
                        label = "Оценка",
                        onClick = {
                            viewModel.onEvent(
                                DetailUiEvents.DetailUiEvent.ShowUserRatingDialogEvent(navObject.bookId)
                            )
                            showRateDialog = true
                        }
                    )
                }
            }
        }
        // ===== НИЖНЯЯ ЧАСТЬ: Белый фон =====
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundWhite)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Рейтинг и статус
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = IconBgLight,
                    modifier = Modifier.clickable { onNavigateToReviews() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Orange
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val averageRating = if (navObject.ratingsList.isNotEmpty()) {
                            navObject.ratingsList.average()
                        } else 0.0
                        Text(
                            text = String.format("%.1f", averageRating),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextDark
                        )
                        Text(
                            text = " (${navObject.ratingsList.size})",
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp,
                            color = TextGray
                        )
                    }
                }
                StatusChip(isOpen = navObject.isOpenNow)

                if (navObject.delivery)
                    StatusDelivery(navObject.delivery)
                Spacer(modifier = Modifier.width(4.dp))


            }
// Название места + Цена в одну линию
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Название места (занимает основное пространство)
                Text(
                    text = navObject.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))

                // Цена справа
                    Text(
                        text = "${navObject.price} р",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
            }
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = navObject.description,

                fontSize = 15.sp,
                color = TextGray,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))


// ===== Секция: Адрес =====
            SectionHeader(title = "Адрес")

// Местоположение в одну линию с круглой кнопкой-стрелкой
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onShowMapClick() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Левая часть: иконка + текст
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = IconBgLight,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF3B82F6)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Местоположение",
                            fontSize = 12.sp,
                            color = TextGray
                        )
                        Text(
                            text = navObject.village,
                            fontSize = 15.sp,
                            color = TextGray,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Правая часть: круглая кнопка со стрелкой
                ArrowCircleButton(
                    modifier = Modifier.padding(20.dp),
                    onClick = onShowMapClick
                )
            }

// Детальный адрес в одну линию (улица, дом, квартира)
            if (navObject.street.isNotEmpty() || navObject.house.isNotEmpty() || navObject.flat.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Улица
                    if (navObject.street.isNotEmpty()) {
                        InfoRow(
                            icon = Icons.Default.Streetview,
                            iconTint = Color(0xFF3B82F6),
                            label = "Улица",
                            value = navObject.street,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (navObject.house.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        // Дом
                        InfoRow(
                            icon = Icons.Default.Home,
                            iconTint = Color(0xFF3B82F6),
                            label = "Дом",
                            value = navObject.house,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (navObject.flat.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        // Квартира
                        InfoRow(
                            icon = Icons.Default.MeetingRoom,
                            iconTint = Color(0xFF3B82F6),
                            label = "Кв.",
                            value = navObject.flat,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(4.dp))
// ===== Объединенная секция: Режим работы + Телефон =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Левая часть: Режим работы
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    SectionHeader(title = "Режим работы")
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = Color(0xFF3B82F6)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = navObject.openingHours,
                                fontSize = 15.sp,
                                color = TextDark,
                                fontWeight = FontWeight.Medium
                            )
                            if (!navObject.isOpenNow) {
                                Text(
                                    text = "Сейчас закрыто",
                                    fontSize = 13.sp,
                                    color = Color(0xFFEF4444),
                                    fontWeight = FontWeight.Medium
                                )
                            } else {
                                Text(
                                    text = "Сейчас открыто",
                                    fontSize = 13.sp,
                                    color = Color(0xFF10B981),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Разделитель
                Spacer(modifier = Modifier.width(16.dp))

                // Правая часть: Телефон
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    SectionHeader(title = "Телефон")
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
                            modifier = Modifier.size(22.dp),
                            tint = Color(0xFF3B82F6)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = telephone,
                            fontSize = 15.sp,
                            color = TextDark,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(4.dp))

            // ===== Секция: Услуги (Доставка и Оплата) =====
            if (navObject.hasDelivery || navObject.payment) {
                SectionHeader(title = "Услуги")

                if (navObject.hasDelivery) {
                    ServiceInfoRow(
                        icon = Icons.Default.LocalShipping,
                        title = "Доставка",
                        subtitle = "",
                        color = Color(0xFF10B981)
                    )
                }

                if (navObject.payment) {
                    ServiceInfoRow(
                        icon = Icons.Default.CreditCard,
                        title = "Оплата картой",
                        subtitle = "Принимаем банковские карты",
                        color = Color(0xFF3B82F6)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
            }

            // ===== Секция: Отзывы =====
            SectionHeader(title = "Отзывы")

            if (uiState.value.comments.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth().height(280.dp)
                ) {
                    items(uiState.value.comments) { ratingData ->
                        CommentListItem(
                            onClick = { rData ->
                                viewModel.onEvent(
                                    DetailUiEvents.DetailUiEvent.CommentDialogEvent(true, rData)
                                )
                            },
                            ratingData = ratingData
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ChatBubbleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = TextGray.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Нет отзывов. Будьте первым!",
                            color = TextGray.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== Нижние кнопки: Добавить в контакт и Поделиться =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        // Добавить в контакты
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(listOf(AccentPurple, PrimaryLight))
                    )
                ) {
                    Icon(
                        Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = AccentPurple
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("В избранное", color = AccentPurple, fontWeight = FontWeight.Medium)
                }

                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch {
                            viewModel.sharePlace(
                                context = context,
                                place = navObject,
                                coroutineScope = this
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.linearGradient(listOf(AccentPurple, PrimaryLight))
                    )
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = AccentPurple
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Поделиться", color = AccentPurple, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ===== Вспомогательные компоненты =====

@Composable
fun ActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White.copy(alpha = 0.15f),
            modifier = Modifier
                .size(56.dp)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = label,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.9f),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark,
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun InfoRow(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = IconBgLight,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = iconTint
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextGray
            )
            Text(
                text = value,
                fontSize = 15.sp,
                color = TextDark,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ServiceInfoRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = color.copy(alpha = 0.1f),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = color
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextDark
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextGray
            )
        }
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = "Доступно",
            tint = color,
            modifier = Modifier.size(32.dp)
        )
    }
}
@Composable
fun AddressItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = IconBgLight,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AccentPurple
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextGray
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextDark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
@Composable
fun StatusChip(isOpen: Boolean) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isOpen) Color(0xFF10B981).copy(alpha = 0.1f)
        else Color(0xFFEF4444).copy(alpha = 0.1f)
    ) {
        Text(
            text = if (isOpen) "Открыто" else "Закрыто",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isOpen) Color(0xFF10B981) else Color(0xFFEF4444)
        )
    }
}
@Composable
fun StatusDelivery(isDelivery: Boolean) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isDelivery) Color(0xFF10B981).copy(alpha = 0.1f)
        else Color(0xFFEF4444).copy(alpha = 0.1f)
    ) {
        Text(
            text = "Доставка",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDelivery) Color(0xFF10B981) else Color(0xFFEF4444)
        )
    }
}


@Composable
fun ArrowCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(20.dp)
            .background(
                color = Color(0xFF3B82F6),
                shape = CircleShape
            )
    ) {
        Icon(
            Icons.Default.ArrowForward,
            contentDescription = "Перейти",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}
@Composable
fun AddressCompactItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = IconBgLight,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AccentPurple
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextGray
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextDark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}