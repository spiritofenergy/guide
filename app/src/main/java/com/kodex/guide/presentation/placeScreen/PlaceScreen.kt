package com.kodex.guide.presentation.placeScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kodex.bookmarketcompose.R
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.presentation.navigation.NavRoutes.CommentsNavData
import com.kodex.guide.presentation.detailScreen.DetailsScreenViewModel
import com.kodex.guide.domain.model.RatingData


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceScreen(
    onCommentsClick: (CommentsNavData) -> Unit = {},
    navObject: NavRoutes.PlaceNavObject = NavRoutes.PlaceNavObject(),
    viewModel: DetailsScreenViewModel = hiltViewModel(),
    navController: NavController,
    placeId: String? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showRateDialog by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
    var ratingDataToShow by remember { mutableStateOf(RatingData()) }


    var bitmap: Bitmap? = null
    try {
        val base64Image = Base64.decode(navObject.imageUrl, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(
            base64Image, 0,
            base64Image.size
        )
    } catch (e: IllegalArgumentException) {

    }
    // Состояния
    var isFavorite by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    //val pagerState = rememberPagerState()

    // Моковые данные (в реальном приложении загружайте из ViewModel)
/*    val navObject = remember {
        PlaceData(
            id = "1",
            title = "Кофейня «Уютный уголок»",
            description = "Уютная кофейня в центре города. Мы предлагаем свежую выпечку, авторский кофе и приятную атмосферу для работы и встреч с друзьями. У нас есть бесплатный Wi-Fi, розетки и вежливый персонал.",
            categoryIndex = 0,
            price = "₽₽",
            rating = 4.8f,
            address = "ул. Центральная, 15, Москва",
            isOpen = true,
            workTime = "09:00 - 22:00",
            contact = "info@cozyplace.ru",
            telephone = "+7 (999) 123-45-67",
            site = "https://cozyplace.ru",
            images = listOf(
                "https://picsum.photos/id/20/800/600",
                "https://picsum.photos/id/30/800/600",
                "https://picsum.photos/id/40/800/600",
                "https://picsum.photos/id/50/800/600"
            ),
            isFavorite = false
        )
    }*/

    // Моковые отзывы
  /*  val ratingData = remember {
        listOf(
            RatingData(
                id = 1,
                name = "Анна Смирнова",
                rating = 5,
                message = "Отличное место! Очень вкусный кофе и приятная атмосфера. Обязательно вернусь сюда снова!",
                timestamp = 2,
               // likes = 12
            ),
            RatingData(
                id = 2,
                name = "Михаил Петров",
                rating = 3,
                message = "Хорошее место для работы. Быстрый Wi-Fi, много розеток. Кофе вкусный, но цены немного высоковаты.",
                timestamp = 26,
                //likes = 8
            ),
            RatingData(
                id = 3,
                name = "Елена Иванова",
                rating = 5,
                message = "Лучшая кофейня в районе! Обслуживание на высоте, десерты просто великолепны!",
                timestamp = 1,
               // likes = 24
            )
        )
    }
*/
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = stringArrayResource(id = R.array.category_array)[navObject.categoryIndex.id],
                    fontSize = 20.sp)
                        },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    // Поделиться
                    IconButton(onClick = { /* Поделиться */ }) {
                        Icon(Icons.Outlined.Share, contentDescription = "Поделиться")
                    }
                    // Избранное
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                            contentDescription = "Избранное",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // 1. Галерея изображений
            item {
                AsyncImage(
                    model = bitmap,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 20.dp)
                        .height(190.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.FillHeight
                )

               /* ImageGallery(images = bitmap)*/
            }

            // 2. Заголовок и рейтинг
            item {
                TitleAndRatingSection(
                    title = navObject.title,
                    rating = if (navObject.ratingsList.isEmpty()) "0.0" else navObject.ratingsList.average().toString(),
                    price = navObject.price,
                    ratingSize ="(${ navObject.ratingsList.size})"

                )
            }

            // 3. Информация о месте
            item {
                InfoSection(
                    address = navObject.address,
                    isOpen = navObject.isOpen,
                    workTime = navObject.workTime,
                    contact = navObject.contact,
                    telephone = navObject.telephone,
                    site = navObject.site
                )
            }

            // 4. Описание
            item {
                DescriptionSection(description = navObject.description)
            }

            // 5. Кнопка звонка
            item {
                CallButton(telephone = navObject.telephone)
            }

            // 6. Вкладки с отзывами
// 6. Вкладки с отзывами
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Табы
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Tab(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            text = { Text("Отзывы (${navObject.ratingsList.size})") }
                        )
                        Tab(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            text = { Text("Информация") }
                        )
                    }

                    // Контент вкладок - используем Column вместо LazyColumn
                 /*   when (selectedTabIndex) {
                        0 -> Column(modifier = Modifier.fillMaxWidth()) {
                            viewModel.commentState.value.forEach { ratingData ->
                                CommentListItem(
                                    onClick = { rData ->
                                        showCommentDialog = true
                                        ratingDataToShow = rData
                                    },
                                    ratingData = ratingData
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        1 -> AdditionalInfoSection()
                    }*/
                }
            }
            /*item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Табы
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Tab(
                            selected = selectedTabIndex == 0,
                            onClick = { selectedTabIndex = 0 },
                            text = { Text("Отзывы (${navObject.ratingsList.size})") }
                        )
                        Tab(
                            selected = selectedTabIndex == 1,
                            onClick = { selectedTabIndex = 1 },
                            text = { Text("Информация") }
                        )
                    }

                    // Контент вкладок
                    when (selectedTabIndex) {
                        0 -> ReviewsSection()
                        1 -> AdditionalInfoSection()
                    }
                }
            }*/

            // 7. Кнопка "Написать отзыв"
            item {
                WriteReviewButton(

                )
            }
        }
    }
    }
/*}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewsSection(
    ratingData: List<Int>
) {
    var showCommentDialog by remember { mutableStateOf(false) }
    var ratingDataToShow by remember { mutableStateOf(RatingData()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(ratingData) { ratingData ->
            CommentListItem(
                onClick = { rData ->
                    showCommentDialog = true
                    ratingDataToShow = rData
                },
                ratingData = ratingData()
            )
        }
       *//* ratingData.forEach { review ->
            ReviewCard(ratingData = review)
        }*//*
    }
}*/
/*
@Composable
@Preview(showBackground = true, device = "id:pixel_6")
fun ShowPlaceScreen() {
    PlaceScreen(
        navController = NavController(LocalContext.current),
        placeId = "1",
        navObject = NavRoutes.PlaceNavObject.toString()
    )
}*/
