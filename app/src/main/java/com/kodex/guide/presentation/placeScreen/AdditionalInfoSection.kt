package com.kodex.guide.presentation.placeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdditionalInfoSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdditionalInfoItem(
                icon = Icons.Filled.Info,
                title = "Правила заведения",
                text = "Бесплатный Wi-Fi, разрешено с животными, детское меню"
            )
            AdditionalInfoItem(
                icon = Icons.Filled.Payment,
                title = "Способы оплаты",
                text = "Наличные, карты, Apple Pay, Google Pay"
            )
            AdditionalInfoItem(
                icon = Icons.Filled.Restaurant,
                title = "Особенности",
                text = "Веранда, парковка, бизнес-ланчи, кофе с собой"
            )
        }
    }
}