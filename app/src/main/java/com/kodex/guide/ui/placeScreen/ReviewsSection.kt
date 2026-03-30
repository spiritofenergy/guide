package com.kodex.guide.ui.placeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ReviewsSection(reviews: List<Review>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        reviews.forEach { review ->
            ReviewCard(review = review)
        }
    }
}
/*
@Composable
fun ReviewsSection(reviews: List<Review>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        reviews.forEach { review ->
            ReviewCard(review = review)
        }
        */
/*items(reviews.size) { index ->
            ReviewCard(review = reviews[index])
        }*//*

    }
}*/
