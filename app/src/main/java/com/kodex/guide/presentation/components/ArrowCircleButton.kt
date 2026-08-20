package com.kodex.guide.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kodex.guide.ui.theme.InfoBlue

@Composable
fun ArrowCircleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    borderColor: Color = InfoBlue,
    arrowColor: Color = InfoBlue,
    backgroundColor: Color = InfoBlue.copy(alpha = 0.3f)
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .border(1.5.dp, borderColor, CircleShape)
            .background(backgroundColor, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = arrowColor, modifier = Modifier.size(20.dp))
    }
}