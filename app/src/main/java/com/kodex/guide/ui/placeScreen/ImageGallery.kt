package com.kodex.guide.ui.placeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ImageGallery(images: List<String>) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { images.size },

     )

    Column {
        HorizontalPager(
            //count = images.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(images[page])
                    .crossfade(true)
                    .build(),
                contentDescription = "Фото места",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Индикаторы страниц
        /*if (images.size > 1) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                activeColor = MaterialTheme.colorScheme.primary,
                inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }*/
    }
}
