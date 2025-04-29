package com.kodex.guide.ui.castom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringArrayResource
import com.kodex.bookmarketcompose.R

@Composable
fun RadioButtonSet(
    isFilterByTitle: Boolean = true,
    onValueChange: (String) -> Unit
) {
    val orderByList = stringArrayResource(R.array.order_by)
    val selectedOption = remember {
        mutableStateOf(orderByList[if (isFilterByTitle) 0 else 1])
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        orderByList.forEach { option ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption.value == option,
                    onClick = {
                        selectedOption.value = option
                        onValueChange(option)
                    }
                )
                Text(text = option)
            }
        }
    }

}