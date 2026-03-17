package com.kodex.guide.ui.addscreen

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.addscreen.data.AddBookViewModel
import com.kodex.guide.ui.addscreen.data.AddScreenObject
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.addscreen.data.RoundedCornerDropDownMenu
import com.kodex.guide.ui.login.LoginButton
import com.kodex.guide.ui.login.RoundedCornerTextField
import com.kodex.guide.ui.theme.BoxFilter
import com.kodex.guide.ui.utils.FirebaseConst.POSTS
import com.kodex.guide.ui.utils.ImageUtils.imageToBase64
import com.kodex.guide.ui.utils.firebase.IS_BASE_64
import com.kodex.guide.ui.utils.toBitmap

@Composable
fun AddBookScreen(
    navData: AddScreenObject = AddScreenObject(),
    onSaved: () -> Unit = {},
    isDelivery: () -> Unit = {},
    viewModel: AddBookViewModel = hiltViewModel()
) {
    val cv = LocalContext.current.contentResolver
    val context = LocalContext.current
    var selectedCategory = remember { mutableStateOf(navData.categoryIndex) }
    var navImageUrl = remember { mutableStateOf(navData.imageUrl) }
    val imageBase64 = remember { mutableStateOf(if (IS_BASE_64) navData.imageUrl else "") }
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.selectedImageUri.value = uri
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
            .padding(46.dp),
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
        /*   Text(
            text = "Taman",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )*/

        Spacer(modifier = Modifier.height(20.dp))
        RoundedCornerDropDownMenu(
            viewModel.selectedCategory.intValue,
            onOptionSelected = { selectedItemIndex ->
                viewModel.selectedCategory.intValue = selectedItemIndex
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
            text = viewModel.price.value,
            label = "Цена:"
        ) {
            viewModel.price.value = it
        }

        LoginButton(text = "Выбрать фото") {
            imageLauncher.launch("image/*")
        }
        LoginButton(text = "Сохранить ") {
            for (i in 1..10) {
                //showProgressIndicator.value = true
                saveBookToFirestore(
                    firestore = FirebaseFirestore.getInstance(),
                    Book(
                        key = navData.key,
                        title = viewModel.title.value + " ${i}",
                        description = viewModel.description.value,
                        price = i + i+200,
                        //price = viewModel.price.value.toInt(),
                        categoryIndex = viewModel.selectedCategory.intValue,
                        village = viewModel.village.value,

                        imageUrl = if (viewModel.selectedImageUri.value != null) {
                            imageToBase64(
                                viewModel.selectedImageUri.value!!,
                                cv
                            )
                        } else {
                            navData.imageUrl
                        }
                    ),
                    onSaved = {
                        onSaved(

                        )
                        Log.d(
                            "MyLog",
                            "Add image64 size: , ${navData.imageUrl.toByteArray(Charsets.UTF_8).size}"
                        )

                    },
                    onError = { error ->
                        Log.d("MyLog4", "Error: ${error}")

                    }
                )
            }
                // viewModel.uploadBook(navData.copy(imageUrl = imageBase64.value))
        }
    }
    //viewModel.uploadBook(navData.copy(imageUrl = imageBase64.value))

}

private fun saveBookToFirestore(
    firestore: FirebaseFirestore,
    book: Book,
    onSaved: () -> Unit,
    onError: (String) -> Unit
) {
    val db = firestore.collection(POSTS)
    val key = book.key.ifEmpty { db.document().id }
    db.document(key)
        .set(book.copy(key = key))
        .addOnSuccessListener { onSaved() }
        .addOnFailureListener { onError(it.message ?: "Error") }
    Log.d("MyLog", "saveBookToFirestore: $book")
}

private fun imageToBase64(
    uri: Uri,
    contentResolver: ContentResolver
): String {
    val inputStream = contentResolver.openInputStream(uri)

    val bytes = inputStream?.readBytes()
    return bytes?.let {
        Base64.encodeToString(it, Base64.DEFAULT)
    } ?: ""
}

@Preview(showBackground = true)
@Composable
fun AddBookScreenPreview() {
    AddBookScreen(
    )
}