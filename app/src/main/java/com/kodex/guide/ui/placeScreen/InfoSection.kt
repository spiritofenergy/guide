package com.kodex.guide.ui.placeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodex.guide.ui.theme.GreenSea


@Composable
fun InfoSection(
    address: String,
    isOpen: Boolean,
    workTime: String,
    contact: String,
    telephone: String,
    site: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Адрес
            InfoRow(
                icon = Icons.Filled.LocationOn,
                title = "Адрес",
                value = address
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Режим работы
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Режим работы",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = workTime,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isOpen) GreenSea else Color.Red)
                        )
                        Text(
                            text = if (isOpen) "Открыто" else "Закрыто",
                            fontSize = 14.sp,
                            color = if (isOpen) GreenSea else Color.Red
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Контакты
            InfoRow(
                icon = Icons.Filled.Email,
                title = "Email",
                value = contact,
                isClickable = true
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            InfoRow(
                icon = Icons.Filled.Phone,
                title = "Телефон",
                value = telephone,
                isClickable = true
            )

            if (site.isNotEmpty()) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(
                    icon = Icons.Filled.Language,
                    title = "Сайт",
                    value = site,
                    isClickable = true
                )
            }
        }
    }
}
