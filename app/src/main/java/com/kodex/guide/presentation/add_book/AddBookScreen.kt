package com.kodex.guide.ui.addscreen

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.bookmarketcompose.R
import com.kodex.guide.data.images.toBitmap
import com.kodex.guide.data.mapper.toDomain
import com.kodex.guide.data.source.remote.FireBaseStorageDataSource
import com.kodex.guide.presentation.add_book.AddBookViewModel
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.model.BookCategories
import com.kodex.guide.domain.model.BookFirestore
import com.kodex.guide.ui.addscreen.data.RoundedCornerDropDownMenu
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.presentation.login.LoginButton
import com.kodex.guide.presentation.login.RoundedCornerTextField
import com.kodex.guide.ui.theme.BoxFilter
import com.kodex.guide.data.source.remote.FirebaseConst.POSTS

 const val IS_BASE_64 = true
@Composable
fun AddBookScreen(
    navData: NavRoutes.AddScreenObject = NavRoutes.AddScreenObject(),
    onSaved: () -> Unit = {},
    isDelivery: () -> Unit = {},
    viewModel: AddBookViewModel = hiltViewModel(),

    ) {
    val cv = LocalContext.current.contentResolver
    val context = LocalContext.current
    val categories = remember { context.resources.getStringArray(R.array.category_array) }
    val selectedCategory = remember { mutableStateOf(navData.categoryIndex) }
    val navImageUrl = remember { mutableStateOf(navData.imageUrl) }
    val imageBase64 = remember { mutableStateOf(if (IS_BASE_64) navData.imageUrl else "") }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            navImageUrl.value = ""
            viewModel.selectedImageUri.value = uri
        }

    }

    LaunchedEffect(Unit) {
        viewModel.setDefaultData(navData)
    }
    //фон
    Image(
        painter = painterResource(id = R.drawable.bereg),
        contentDescription = "Logo",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop

    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BoxFilter)
    )

    // Основной лист
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Фото
        Image(
            painter = rememberAsyncImagePainter(
                model = if (imageBase64.value.isNotEmpty()) {
                    imageBase64.value.toBitmap()
                } else {
                    navImageUrl.value.ifEmpty { viewModel.selectedImageUri.value }
                }
            ),
            contentDescription = "",
            modifier = Modifier
                .height(400.dp)
                .width(600.dp)


        )
           Text(
            text = "Taman",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerDropDownMenu(
            categories.toList(),
            categories[viewModel.selectedCategory.value.id],
            onOptionSelected = { selectedItemIndex ->
                viewModel.selectedCategory.value = BookCategories.fromId(selectedItemIndex)
            },
        )

        /*      Spacer(modifier = Modifier.height(5.dp))
        RoundedCornerDropDownMenuV(
            viewModel.selectedVillage.intValue,
            onOptionSelected = { selectedItemVillage ->
                viewModel.selectedVillage.intValue = selectedItemVillage
            },
        )*/

        Spacer(modifier = Modifier.height(5.dp))
        RoundedCornerTextField(
            text = viewModel.title.value,
            label = "Название:"
        ) {
            viewModel.title.value = it
        }
        Spacer(modifier = Modifier.height(5.dp))

        /* RoundedCornerTextField(
            text = viewModel.location.value,
            label = "Location:"
        ) {
            viewModel.title.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
*/
        RoundedCornerTextField(
            text = viewModel.description.value,
            label = "Краткое описание:",
            singleLine = false,
            maxLines = 5
        ) {
            viewModel.description.value = it
        }
        RoundedCornerTextField(
            text = viewModel.village.value,
            label = "Станица:",
            singleLine = false,
            maxLines = 5
        ) {
            viewModel.village.value = it
        }

        Spacer(modifier = Modifier.height(5.dp))

        RoundedCornerTextField(
            text = viewModel.price.intValue.toString(),
            label = "Цена:"
        ) {
            viewModel.price.intValue = it.ifEmpty { "0" }.toInt()
        }
        /*{ userInput ->
            // Преобразуем всё, что ввел пользователь, в String и оставляем только цифры
            val stringValue = userInput.toString()
                 val onlyDigits = stringValue.filter { it.isDigit() }
            viewModel.price.value = onlyDigits
        }*/

        LoginButton(text = "Выбрать фото") {
            imageLauncher.launch("image/*")
        }
        LoginButton(text = "Сохранить ") {
            navData.toDomain().copy()
            viewModel.uploadBook(
                navData.toDomain().copy(imageUrl = imageBase64.value)
            )
            onSaved()
            Log.d(
                "MyLog", "Add image64 size: , ${navData.imageUrl.toByteArray(Charsets.UTF_8).size}"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddBookScreenPreview() {

}