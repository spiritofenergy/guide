package com.kodex.guide.presentation.home

    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.width
    import androidx.compose.material3.AlertDialog
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextButton
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.unit.dp
    import com.kodex.guide.ui.theme.GreenSea

    @Composable
    fun TariffDialog(
        currentRoleName: String,
        nextRoleName: String?,
        nextRequiresPayment: Boolean,
        showNextButton: Boolean,
        showPremiumButton: Boolean,
        showGuestButton: Boolean,
        onDismiss: () -> Unit,
        onNextTariffClick: () -> Unit,
        onPremiumClick: () -> Unit,
        onLogoutClick: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Ваш тариф",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Column {
                    Text(
                        text = "Текущий тариф: $currentRoleName",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (nextRoleName != null) {
                        Text(
                            text = "Доступно повышение до: $nextRoleName",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (nextRequiresPayment) {
                                "Для перехода требуется оплата"
                            } else {
                                "Для перехода требуется регистрация"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    } else {
                        Text(
                            text = "У вас максимальный тариф",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    if (showNextButton && nextRoleName != null) {
                        Button(
                            onClick = onNextTariffClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenSea)
                        ) {
                            Text("Перейти на «$nextRoleName»")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (showPremiumButton) {
                        Button(
                            onClick = onPremiumClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                        ) {
                            Text("Перейти на «Премиум»")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (showGuestButton) {
                            TextButton(onClick = onLogoutClick) {
                                Text("Гость")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        TextButton(onClick = onDismiss) {
                            Text("Отмена")
                        }
                    }
                }
            },
            dismissButton = {}
        )
    }
