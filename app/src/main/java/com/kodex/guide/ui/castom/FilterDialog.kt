package com.kodex.guide.ui.castom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.mainScreen.MainScreenViewModel

@Composable
@Preview(showBackground = true)
fun FilterDialog(
    showDialog: Boolean = false,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    title: String = "Order by:",
    confirmButtonText: String = "Ok",
    viewModel: MainScreenViewModel = hiltViewModel()

) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                    viewModel.setFilter()

                }) {
                    Text(text = confirmButtonText)
                }

                Button(onClick = {
                    onDismiss()
                }) {
                    Text(text = "Cansel")
                }
            },
            title = {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            },
            text = {
                val orderBySelection = stringArrayResource(R.array.order_by)[0]
                Column(modifier = Modifier.fillMaxWidth()) {
                    RadioButtonSet(
                        isFilterByTitle = viewModel.isFilterByTitle.value,
                    ) { option ->
                        viewModel.isFilterByTitle.value = option == orderBySelection
                    }

                    if (!viewModel.isFilterByTitle.value) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Price range:",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(5.dp))

                        PricePicker2(
                            priceValue = viewModel.minPriceValue.floatValue,
                            title = "Min",
                            onValueChange = { value ->
                                viewModel.minPriceValue.floatValue = value
                            }
                        )
                        Spacer(modifier = Modifier.width(5.dp))

                        PricePicker2(
                            priceValue = viewModel.maxPriceValue.floatValue,
                            title = "Max",
                            onValueChange = { value ->
                                viewModel.maxPriceValue.floatValue = value
                            }
                        )
                    }
                }
            }
        )
    }
}

/*Text(text = "From")
                         PricePicker(
                             minPriceValue.value,
                             priceRange
                         ) { value ->
                             minPriceValue.intValue = value
                         }

                         Text(text = "To")
                         PricePicker(
                             maxPriceValue.value,
                             priceRange
                         ) { value ->
                             maxPriceValue.intValue = value
                         }*/