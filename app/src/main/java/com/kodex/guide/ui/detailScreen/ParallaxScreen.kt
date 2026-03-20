package com.kodex.guide.ui.detailScreen
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kodex.guide.ui.theme.Orange
import kotlinx.coroutines.launch

// Модель данных места
data class PlaceDetail(
    val id: String,
    val name: String,
    val address: String,
    val rating: Double,
    val reviewsCount: Int,
    val priceLevel: String,
    val photos: List<String>,
    val isOpenNow: Boolean,
    val openingHours: String,
    val phone: String,
    val website: String,
    val amenities: List<String>,
    val latitude: Double,
    val longitude: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParallaxScreen(
    place: PlaceDetail,
    onBackPressed: () -> Unit,
    onCallTaxi: (Double, Double) -> Unit,
    onNavigateToReviews: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val collapsedHeight = 280.dp
    val expandedHeight = screenHeight * 0.5f

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    val imageHeight = remember { Animatable(expandedHeight.value) }

    // NestedScroll для Parallax эффекта
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newHeight = (imageHeight.value - delta).coerceIn(
                    collapsedHeight.value,
                    expandedHeight.value
                )
                coroutineScope.launch {
                    imageHeight.animateTo(
                        newHeight,
                        animationSpec = tween(durationMillis = 0)
                    )
                }
                return Offset.Zero
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = place.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Поделиться */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Поделиться")
                    }
                    IconButton(onClick = { /* Избранное */ }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Избранное")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(nestedScrollConnection)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Фото-галерея с Parallax эффектом
                item {
                    PhotoGallery(
                        photos = place.photos,
                        height = with(LocalDensity.current) { imageHeight.value.toDp() }
                    )
                }

                // Основной контент
                item {
                    MainContent(
                        place = place,
                        onCallTaxi = { onCallTaxi(place.latitude, place.longitude) },
                        onNavigateToReviews = onNavigateToReviews
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoGallery(photos: List<String>, height: Dp) {
    if (photos.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Image,
                contentDescription = "Нет фото",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Основное фото (первое)
            AsyncImage(
                model = photos.first(),
                contentDescription = "Фото места",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                contentScale = ContentScale.Crop
            )

            // Индикатор количества фото
            if (photos.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Default.Collections,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${photos.size} фото",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Кнопка "Все фото"
            Button(
                onClick = { /* Открыть галерею */ },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            ) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Все фото", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun MainContent(
    place: PlaceDetail,
    onCallTaxi: () -> Unit,
    onNavigateToReviews: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Название
        Text(
            text = place.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Рейтинг и цена
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
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", place.rating),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = " (${place.reviewsCount})",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            if (place.priceLevel.isNotEmpty()) {
                Text(
                    text = place.priceLevel,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Статус работы
            StatusChip(isOpen = place.isOpenNow)
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
                text = place.address,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Часы работы
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    text = place.openingHours,
                    fontSize = 14.sp,
                    color = if (place.isOpenNow)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
                if (!place.isOpenNow) {
                    Text(
                        text = "Закрыто",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Вызвать такси"
        Button(
            onClick = onCallTaxi,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                Icons.Default.TaxiAlert,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Вызвать такси",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Удобства
        if (place.amenities.isNotEmpty()) {
            Text(
                text = "Удобства",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(place.amenities) { amenity ->
                   /* AssistChip(
                        onClick = { *//* Фильтр по удобству *//* },
                        label = { Text(amenity, fontSize = 12.sp) },
                        leading = {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Orange
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    )*/
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Контактная информация
        if (place.phone.isNotEmpty() || place.website.isNotEmpty()) {
            Text(
                text = "Контакты",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (place.phone.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Позвонить */ }
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = place.phone,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (place.website.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Открыть сайт */ }
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Language,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = place.website,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun StatusChip(isOpen: Boolean) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isOpen) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (isOpen) Color(0xFF4CAF50) else Color(0xFFF44336),
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isOpen) "Открыто" else "Закрыто",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isOpen) Color(0xFF2E7D32) else Color(0xFFC62828)
            )
        }
    }
}

// Для демонстрации добавьте в Preview:
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun PreviewDetailScreen() {
    MaterialTheme {
        ParallaxScreen(
            place = PlaceDetail(
                id = "1",
                name = "Coffee House & Bakery",
                address = "ул. Тверская, 15, Москва",
                rating = 4.7,
                reviewsCount = 1243,
                priceLevel = "₽₽",
                photos = listOf(
                    "https://example.com/photo1.jpg",
                    "https://example.com/photo2.jpg"
                ),
                isOpenNow = true,
                openingHours = "09:00 - 23:00",
                phone = "+7 (495) 123-45-67",
                website = "coffeehouse.ru",
                amenities = listOf("Wi-Fi", "Парковка", "Детское меню", "Веранда"),
                latitude = 55.7558,
                longitude = 37.6173
            ),
            onBackPressed = {},
            onCallTaxi = { _, _ -> },
            onNavigateToReviews = {}
        )
    }
}