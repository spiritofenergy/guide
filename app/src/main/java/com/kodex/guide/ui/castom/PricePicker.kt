package com.kodex.guide.ui.castom

import android.widget.NumberPicker
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PricePicker(
    value: Int,
    ranger: IntRange,
    onValueChange: (Int) -> Unit,
) {
    Row {
        AndroidView(
            factory = { context ->
                NumberPicker(context).apply {
                    minValue = ranger.first
                    maxValue = ranger.last
                    setOnValueChangedListener { _, _, value ->
                        onValueChange(value)
                    }
                    this.value = value
                }
            },
            update = { numberPicker ->
                numberPicker.minValue = ranger.first
                numberPicker.maxValue = ranger.last
                if (numberPicker.value != value) {
                    numberPicker.value = value
                }
            }
        )
    }
}