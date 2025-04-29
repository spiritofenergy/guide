package com.kodex.guide.ui.addscreen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.addscreen.data.AddBookViewModel
import com.kodex.guide.ui.addscreen.data.AddScreenObject
import com.kodex.guide.ui.addscreen.data.RoundedCornerDropDownMenu
import com.kodex.guide.ui.login.LoginButton
import com.kodex.guide.ui.login.RoundedCornerTextField
import com.kodex.guide.ui.mainScreen.MainScreenViewModel
import com.kodex.guide.ui.theme.ButtonColor
import com.kodex.guide.ui.utils.ImageUtils
import com.kodex.guide.ui.utils.firebase.IS_BASE_64
import com.kodex.guide.ui.utils.toBitmap

@Composable
fun AddBookScreen(
    navData: AddScreenObject = AddScreenObject(),
    onSaved: () -> Unit = {},
    viewModel: AddBookViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val isUpload = remember {
        mutableStateOf(false)
    }
    var navImageUrl = remember { mutableStateOf(navData.imageUrl) }
    val imageBase64 = remember { (mutableStateOf(if (IS_BASE_64) navData.imageUrl else "")) }
    val firestore = remember { Firebase.firestore }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (IS_BASE_64) {
            imageBase64.value = uri?.let {
                ImageUtils.imageToBase64(uri, context.contentResolver)
            } ?: ""
        } else {
            navImageUrl.value = ""
            viewModel.selectedImageUri.value = uri
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setDefaultData(navData)
        viewModel.uiState.collect { state ->
            when (state) {
                is MainScreenViewModel.MainUiState.Loading -> {
                    viewModel.showLoadingIndicator.value = true
                }

                is MainScreenViewModel.MainUiState.Success -> {
                    onSaved()
                }

                is MainScreenViewModel.MainUiState.Error -> {
                    viewModel.showLoadingIndicator.value = false
                    Toast.makeText(context, "Error: ${state.massage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Image(
        painter = painterResource(id = R.drawable.bereg),
        contentDescription = "BG",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 40.dp, end = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = rememberAsyncImagePainter(
                // model = imageBitMap.value
                // model = imageBase64.value.toBitmap()

                model = if (imageBase64.value.isNotEmpty()) {
                    imageBase64.value.toBitmap()
                } else {
                    navImageUrl.value.ifEmpty { viewModel.selectedImageUri.value }

                }
            ),
            contentDescription = "Logo",
            modifier = Modifier
                .height(450.dp)
                .width(500.dp),
         //   verticalArrangement = Arrangement.Center,
          //  horizontalAlignment = Alignment.CenterHorizontally

        )
        RoundedCornerDropDownMenu(viewModel.selectedCategory.intValue) { selectedItemIndex ->
            imageLauncher
            viewModel.selectedCategory.intValue = selectedItemIndex
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = viewModel.title.value,
            label = "Заголовок: "
        ) {
            viewModel.title.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = viewModel.description.value,
            label = "Краткое описание:",
            maxLines = 5,
            singleLine = false
        ) {
            viewModel.description.value = it

        }
        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = viewModel.prise.value,
            label = "Цена :"
        ) {
            viewModel.prise.value = it

        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = viewModel.telephone.value,
            label = "Телефон:"
        ) {
            viewModel.telephone.value = it

        }

        Spacer(modifier = Modifier.height(10.dp))

        LoginButton(text = "Выбрать фото ") {
            imageLauncher.launch("image/*")
        }
        LoginButton(text = "Сохранить", viewModel.showLoadingIndicator.value) {
            viewModel.showLoadingIndicator.value = true
            viewModel.uploadBook(navData.copy(imageUrl = imageBase64.value))
            /*   isUpload.value = true
               saveBookToFireStore(
                   firestore,
                   Book(
                       key = navData.key,
                       title = title.value,
                       description = description.value,
                       price = prise.value,
                       categoryIndex = selectedCategory.value,
                       imageUrl = if (selectedImageUri.value !== null)
                           imageToBase64(
                               selectedImageUri.value!!,
                               cv
                           ) else navData.imageUrl
                   ),
                   onSaved = {
                       onSaved()
                       Log.d("MyLog", "OnSave")
                   },
                   onError = {
                   }
               )*/
        }
        Spacer(modifier = Modifier.height(15.dp))
        Box {
            if (isUpload.value == true)
                CircularProgressIndicator(
                    modifier = Modifier.height(30.dp),
                    color = ButtonColor
                )
        }
    }
}








