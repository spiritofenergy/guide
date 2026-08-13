package com.kodex.guide.presentation.home

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = Color(0x9c008BF5),
        strokeWidth = 8.dp,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}