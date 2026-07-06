package com.kodex.guide.presentation.detailScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kodex.guide.ui.theme.Orange

 @Composable
fun StarsIndicator(
    rating: Int
) {
    Row (modifier = Modifier.fillMaxWidth()){
        for (i in 1..rating) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = Orange
            )
            Spacer(Modifier.width(5.dp))
        }
    }
}