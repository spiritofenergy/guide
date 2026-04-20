package com.kodex.guide.presentation.castom

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PricePickerThumb(
    title: String = "Min:",
    priceValue: Float = 0f,
    onValueChange: (Float) -> Unit = {}
) {
    val myBlue = Color(0xAD008BF5)
    val myBlueDark = Color(0xFF0D6EBD)
    Column {
        Text(text = "$title ${priceValue.toInt()}")
        Slider(
            value = priceValue,
            onValueChange = { value ->
              onValueChange(value)
            },
            valueRange = 0f..10000f,
            steps = 99,
            colors = SliderDefaults.colors(
                thumbColor = myBlueDark,
                activeTrackColor = myBlue,
                inactiveTrackColor = myBlue,
                inactiveTickColor = myBlue.copy(alpha = 0.3f)
            )
        )
    }
}
@Composable
@Preview(showBackground = true)
fun ShowPricePickerThumb(){
    PricePickerThumb()

}