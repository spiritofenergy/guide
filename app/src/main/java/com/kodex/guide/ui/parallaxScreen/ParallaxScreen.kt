package com.kodex.guide.ui.parallaxScreen
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
import kotlinx.coroutines.launch

// Модель данных места


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParallaxScreen(
    place: ParallaxData,
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


// Для демонстрации добавьте в Preview:
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun PreviewDetailScreen() {
    MaterialTheme {
        ParallaxScreen(
            place = ParallaxData(
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