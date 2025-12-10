package com.kodex.guide.ui.addscreen

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.addscreen.data.AddBookViewModel
import com.kodex.guide.ui.addscreen.data.AddScreenObject
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.addscreen.data.RoundedCornerDropDownMenu
import com.kodex.guide.ui.login.LoginButton
import com.kodex.guide.ui.login.RoundedCornerTextField
import com.kodex.guide.ui.mainScreen.MainScreenViewModel
import com.kodex.guide.ui.theme.BoxFilter
import com.kodex.guide.ui.utils.ImageUtils
import com.kodex.guide.ui.utils.firebase.IS_BASE_64
import com.kodex.guide.ui.utils.toBitmap
import kotlin.String
import kotlin.sequences.ifEmpty

@Preview(showBackground = true)
@Composable
fun AddBookScreen(
    navData: AddScreenObject = AddScreenObject(),
    onSaved: () -> Unit = {},
    viewModel: AddBookViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    var selectedCategory = remember {
        mutableIntStateOf(navData.categoryIndex)
    }
    var navImageUrl = remember {
        mutableStateOf(navData.imageUrl)
    }
    val imageBase64 = remember {
        mutableStateOf(if (IS_BASE_64) navData.imageUrl else "")
    }

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
        Spacer(modifier = Modifier.height(40.dp))

        RoundedCornerDropDownMenu(viewModel.selectedCategory.value) { selectedItemIndex ->
            imageLauncher
            viewModel.selectedCategory.intValue = selectedItemIndex
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = viewModel.title.value,
            label = "Название:"
        ) {
            viewModel.title.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = viewModel.description.value,
            label = "Краткое описание:",
            singleLine = false,
            maxLines = 5
        ) {
            viewModel.description.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        RoundedCornerTextField(
            text = viewModel.price.value,
            label = "Цена:"
        ) {
            viewModel.price.value = it
        }

        Spacer(modifier = Modifier.height(10.dp))

        LoginButton(text = "Выбрать фото") {
            imageLauncher.launch("image/*")
        }
        LoginButton(text = "Сохранить ") {
            viewModel.uploadBook(navData.copy(imageUrl = imageBase64.value))

        }
    }
}
    /*
    val cv = LocalContext.current
        .contentResolver

    val price = remember {
        mutableStateOf("")
    }
    val title = remember {
        mutableStateOf("")
    }
    val description = remember {
        mutableStateOf("")
}
val selectedImageUri = remember {
    mutableStateOf<Uri?>(null)
}
    val firestore = remember {
        Firebase.firestore
    }
    val storage = remember {
        Firebase.storage
    }
val imageLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri ->
    selectedImageUri.value = uri
}

Image(
    painter = painterResource(id = R.drawable.wey),
    contentDescription =  "BG",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,

        )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(46.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

              Image(painter = rememberAsyncImagePainter(model = selectedImageUri.value),
                   contentDescription = "Logo",
                  modifier = Modifier.height(250.dp).padding(bottom = 50.dp)

              )
        Spacer(modifier = Modifier.height(10.dp))

     *//*   RoundedCornerDropDownMenu (viewModel.selectedCategory.intValue){ selectedItem ->
        imageLauncher
        selectedCategory = selectedItem
        }*//*

        Spacer(modifier = Modifier.height(40.dp))

        RoundedCornerTextField(
            text = title.value,
            label = "Title:"
        ) {
            title.value = it
        }
        Spacer(modifier = Modifier.height(16.dp))

        RoundedCornerTextField(
            text = description.value,
            label = "Description:",
            maxLines = 5,
            singleLine = false
        ){
            description.value = it

        }
        Spacer(modifier = Modifier.height(16.dp))

        RoundedCornerTextField(
            text = price.value,
            label = "Prise:"
        ){
            price.value = it

        }

        Spacer(modifier = Modifier.height(16.dp))

        LoginButton(text = "Select image "){
            imageLauncher.launch("image/*")
        }
        LoginButton(text = "Save ") {
            safeBookToFireStore(
                firestore,
                Book(
                    title = title.value,
                    description = description.value,
                    price = price.value.toInt(),
                   // categoryIndex = categoryIndex.value,
                    imageUrl = imageToBase64(
                        selectedImageUri.value!!,
                        cv
                    )
                ),
                onSaved = {
                    onSaved()
                    Log.d("MyLog", "OnSave")
                },
                onError = {

                }
            )
           *//* Для сохранения текскта в Storage
           saveBookImage(
                selectedImageUri.value!!,
                storage,
                firestore,
                onSaved = {
                    onSaved()
                    Log.d("MyLog", "onSaved")
                },
                onError = {

                },
                Book(
                    title = title.value,
                    description = description.value,
                    prise = prise.value,
                    category = selectedCategory,
                    key = "key",
                    imageUrl = ""
                )
            )*//*

        }
    }
}

private fun imageToBase64(uri: Uri, contentResolver: ContentResolver): String{
    val inputStream = contentResolver.openInputStream(uri)

    val bytes = inputStream?.readBytes()
    return bytes?.let {
        Base64.encodeToString(it, Base64.DEFAULT)
    }?: ""
}

private fun saveBookImage(
    uri: Uri,
    storage: FirebaseStorage,
    firestore: FirebaseFirestore,
    onSaved: ()-> Unit,
    onError: ()-> Unit,
    book: Book
){
    val timeStamp = System.currentTimeMillis()
    val storageRef = storage.reference
      //  .child("book_images")
        .child("guide_images")
        .child("images_$timeStamp.jpg")
    val uploadTask = storageRef.putFile(uri)
    uploadTask.addOnSuccessListener{
        storageRef.downloadUrl.addOnSuccessListener{ url ->
         *//*   safeBookToFireStore(
                firestore,
                url.toString(),
                book,
                onSaved = {
                        onSaved()
                },
                onError = {
                        onError()
                }
            )*//*
        }
    }
}

private fun safeBookToFireStore(
    firestore: FirebaseFirestore,
    book: Book,
    onSaved: ()-> Unit,
    onError: ()-> Unit
){
    //Куда соханять фото
    val db = firestore.collection("guide_posts")
    val key = db.document().id
    db.document(key)
        .set(
            book.copy(key = key)
        ).addOnSuccessListener{
            onSaved()
        }
        .addOnFailureListener{
            onError()
        }

}

  *//*  firestore: FirebaseFirestore,
    url: String,
    book: Book,
    onSaved: ()-> Unit,
    onError: ()-> Unit
){
    //Куда соханять фото
    val db = firestore.collection("posts")
    val key = db.document().id
    db.document(key)
        .set(
            book.copy(key = key,
                imageUrl = url)
        ).addOnSuccessListener{
            onSaved()
        }
        .addOnFailureListener{
            onError()
        }

}*//*


*/