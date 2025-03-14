package com.kodex.guide.ui.addscreen

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.ProgressBar
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.addscreen.data.AddScreenObject
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.addscreen.data.IndeterminateCircularIndicator
import com.kodex.guide.ui.addscreen.data.RoundedCornerDropDownMenu
import com.kodex.guide.ui.login.LoginButton
import com.kodex.guide.ui.login.RoundedCornerTextField
import com.kodex.guide.ui.theme.ButtonColor
import kotlin.String

@Preview(showBackground = true)
@Composable
fun AddBookScreen(
    navData: AddScreenObject = AddScreenObject(),
    onSaved: () -> Unit = {}
) {
    val cv = LocalContext.current.contentResolver

    val isUpload = remember {
        mutableStateOf(false)
    }
    var selectedCategory = remember {
        mutableStateOf(navData.category)
    }
    val prise = remember {
        mutableStateOf(navData.price)
    }
    val title = remember {
        mutableStateOf(navData.title)
    }
    val description = remember {
        mutableStateOf(navData.description)
}

val selectedImageUri = remember {
    mutableStateOf<Uri?>(null)
}
    val imageBitMap = remember {
        var bitmap: Bitmap? = null
        try {
            val base64Image = Base64.decode(navData.imageUrl, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(
                base64Image, 0,
                base64Image.size
            )
        }catch (e: IllegalArgumentException){
        }
        mutableStateOf(bitmap)
    }
    val firestore = remember {
        Firebase.firestore
    }

val imageLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri ->
    imageBitMap.value = null
    selectedImageUri.value = uri
}

Image(
    painter = painterResource(id = R.drawable.bereg),
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

        Image(
            painter = rememberAsyncImagePainter(
                model = imageBitMap.value
            ),
            contentDescription = "Logo",
            modifier = Modifier.height(250.dp).padding(bottom = 50.dp)

        )
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerDropDownMenu(selectedCategory.value) { selectedItem ->
            imageLauncher
            selectedCategory.value = selectedItem
        }

        Spacer(modifier = Modifier.height(16.dp))

        RoundedCornerTextField(
            text = title.value,
            label = "Заголовок: "
        ) {
            title.value = it
        }
        Spacer(modifier = Modifier.height(16.dp))

        RoundedCornerTextField(
            text = description.value,
            label = "Краткое описание:",
            maxLines = 5,
            singleLine = false
        ) {
            description.value = it

        }
        Spacer(modifier = Modifier.height(16.dp))

        RoundedCornerTextField(
            text = prise.value,
            label = "Цена :"
        ) {
            prise.value = it

        }

        Spacer(modifier = Modifier.height(16.dp))

        LoginButton(text = "Выбрать фото ") {
            imageLauncher.launch("image/*")
        }
        LoginButton(text = "Сохранить") {
            isUpload.value = true
            saveBookToFireStore(
                firestore,
                Book(
                    key = navData.key,
                    title = title.value,
                    description = description.value,
                    price = prise.value,
                    category = selectedCategory.value,
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
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box{
            if (isUpload.value == true)
                CircularProgressIndicator(
                    modifier = Modifier.height(30.dp),
                    color = ButtonColor
                )
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


private fun saveBookToFireStore(
    firestore: FirebaseFirestore,
    book: Book,
    onSaved: ()-> Unit,
    onError: ()-> Unit
){
    //Куда соханять фото
    val db = firestore.collection("guide_posts")
    val key = if(book.key.isEmpty()) db.document().id else book.key
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




