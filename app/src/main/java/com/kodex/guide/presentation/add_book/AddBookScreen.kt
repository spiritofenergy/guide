package com.kodex.guide.presentation.add_book

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.kodex.bookmarketcompose.R
import com.kodex.guide.data.mapper.toDomain
import com.kodex.guide.domain.model.BookCategories
import com.kodex.guide.ui.addscreen.data.RoundedCornerDropDownMenu
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.presentation.login.LoginButton
import com.kodex.guide.presentation.login.RoundedCornerTextField
import com.kodex.guide.ui.theme.BoxFilter
import com.kodex.guide.ui.theme.ButtonColor

const val IS_BASE_64 = true

 @Composable
fun AddBookScreen(
    navData: NavRoutes.AddScreenObject = NavRoutes.AddScreenObject(),
    onSaved: () -> Unit = {},
    isDelivery: () -> Unit = {},
    viewModel: AddBookViewModel = hiltViewModel(),
    ) {

    // ✅ Декодируем Base64 в Bitmap только один раз при старте (для режима редактирования)
    val initialBitmap = remember(navData.imageUrl) {
        if (navData.imageUrl.isNotEmpty()) {
            try {
                val base64Image = Base64.decode(navData.imageUrl, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(base64Image, 0, base64Image.size)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else null
    }

    val context = LocalContext.current
    val categories = remember { context.resources.getStringArray(R.array.category_array) }
    val selectedCategory = remember { mutableStateOf(navData.categoryIndex) }
    val navImageUrl = remember { mutableStateOf(navData.imageUrl) }
    val scrollState = rememberScrollState()
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            navImageUrl.value = ""
            viewModel.selectedImageUri.value = uri // Сохраняем Uri для быстрого отображения в UI
            // ✅ Запускаем асинхронную конвертацию в Base64, чтобы не фризило UI
            viewModel.convertImageToBase64(uri)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setDefaultData(navData) }

// 🔔 Диалог с ошибкой валидации
    viewModel.validationError.value?.let { errorMessage ->
        AlertDialog(
            onDismissRequest = { viewModel.clearValidationError() },
            title = {
                Text(
                    text = "Проверьте данные",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = errorMessage,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.clearValidationError() }) {
                    Text("Понятно", color = Color(0xFF03A9F4))
                }
            },
            containerColor = Color(0xFF2C2C2E),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }
    //фон
    Image(
        painter = painterResource(id = R.drawable.bereg),
        contentDescription = "Logo",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop

    )
    // Основной лист
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
             .verticalScroll(scrollState),  // СКРОЛЛИНГ
    verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = rememberAsyncImagePainter(
                model = viewModel.selectedImageUri.value ?: initialBitmap
            ),
            contentDescription = "Фото",
            modifier = Modifier
                .height(300.dp)
                .width(600.dp)
        )

        Text(
            text = stringResource(R.string.сreate_post),
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
        )

        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerDropDownMenu(
            categories.toList(),
            categories[viewModel.selectedCategory.value.id],
            onOptionSelected = { selectedItemIndex ->
                viewModel.selectedCategory.value = BookCategories.fromId(selectedItemIndex)
            },
        )

        Spacer(modifier = Modifier.height(5.dp))
        RoundedCornerTextField(
            text = viewModel.title.value,
            label = "Название:"
        ) {
            viewModel.title.value = it
        }
        Spacer(modifier = Modifier.height(5.dp))

        RoundedCornerTextField(
            text = viewModel.description.value,
            label = "Краткое описание:",
            singleLine = false,
            maxLines = 5
        ) {
            viewModel.description.value = it
        }
        Spacer(modifier = Modifier.height(5.dp))

        RoundedCornerTextField(
            text = viewModel.village.value,
            label = "Станица:",
            singleLine = false,
            maxLines = 1
        ) {
            viewModel.village.value = it
        }
        Spacer(modifier = Modifier.height(5.dp))
                     //чекбокс Показывать на карте
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier,
                checked = viewModel.location.value,
                onCheckedChange = { viewModel.location.value = it },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = Color.White,     // ЦВЕТ КОНТУРА, когда НЕ отмечено (белый)
                    checkedColor = Color(0xFF03A9F4),
                    checkmarkColor = Color.White)
            )
            Text(
                stringResource(R.string.location),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif)
        }
        Spacer(modifier = Modifier.height(5.dp))

        RoundedCornerTextField(
            text = viewModel.street.value,
            label = "Улица:",
            singleLine = false,
            maxLines = 1
        ) {
            viewModel.street.value = it
        }
        Spacer(modifier = Modifier.height(5.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                RoundedCornerTextField(
                    text = viewModel.home.value,
                    label = "Дом:",
                    singleLine = true,
                    maxLines = 1
                ) {
                    viewModel.home.value = it
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                RoundedCornerTextField(
                    text = viewModel.apartment.value,
                    label = "Кв:",
                    singleLine = true,
                    maxLines = 1
                ) {
                    viewModel.apartment.value = it
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        RoundedCornerTextField(
            text = viewModel.price.intValue.toString(),
            label = "Цена:"
        ) { userInput ->
            // ✅ Фильтруем только цифры и безопасно преобразуем в Int
            val filteredInput = userInput.filter { it.isDigit() }
            viewModel.price.intValue = filteredInput.toIntOrNull() ?: 0
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier,
                checked = viewModel.delivery.value,
                onCheckedChange = { viewModel.delivery.value = it },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = Color.White,
                    checkedColor = Color(0xFF03A9F4),
                    checkmarkColor = Color.White)
            )
            Text(
                stringResource(R.string.delivery),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.height(5.dp))

            Checkbox(
                modifier = Modifier,
                checked = viewModel.payment.value,
                onCheckedChange = { viewModel.payment.value = it },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = Color.White,
                    checkedColor = Color(0xFF03A9F4),
                    checkmarkColor = Color.White
                )

            )
            Text(
                stringResource(R.string.card_payment),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
        }
        LoginButton(text = "Выбрать фото") {
            imageLauncher.launch("image/*")
        }

        LoginButton(text = "Сохранить ") {
            if (viewModel.validateBook()) {
                val bookToSave = navData.toDomain().copy(
                    imageUrl = viewModel.imageBase64.value,
                    title = viewModel.title.value,
                    description = viewModel.description.value,
                    price = viewModel.price.intValue,
                    village = viewModel.village.value,
                    categoryIndex = viewModel.selectedCategory.value,
                    delivery = viewModel.delivery.value,
                    payment = viewModel.payment.value
                )
                viewModel.uploadBook(bookToSave)
                onSaved()
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AddBookScreenPreview() {

}