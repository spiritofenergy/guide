package com.kodex.guide.ui.castom

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PricePicker2(
    title: String = "Min:",
    priceValue: Float = 0f,
    onValueChange: (Float) -> Unit = {}
) {

    Column {
        Text(text = "$title ${priceValue.toInt()}")
        Slider(
            value = priceValue,
            onValueChange = { value ->
              onValueChange(value)
            },
            valueRange = 0f..10000f,
            steps = 99
        )
    }
}