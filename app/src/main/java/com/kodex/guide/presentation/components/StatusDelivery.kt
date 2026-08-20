package com.kodex.guide.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
