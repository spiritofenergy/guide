package com.kodex.guide.ui.addscreen

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.addscreen.data.RoundedCornerDropDownMenu
import com.kodex.guide.ui.login.LoginButton
import com.kodex.guide.ui.login.RoundedCornerTextField
import kotlin.String

@Preview(showBackground = true)
@Composable
fun AddBookScreen(
    onSaved: () -> Unit = {}
) {
    val cv = LocalContext.current
        .contentResolver
    var selectedCategory = "Bestsellers"

    val prise = remember {
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
        RoundedCornerDropDownMenu { selectedItem ->
        imageLauncher
        selectedCategory = selectedItem
        }

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
            text = prise.value,
            label = "Prise:"
        ){
            prise.value = it

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
                    prise = prise.value,
                    category = selectedCategory,
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
           /* Для сохранения текскта в Storage
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
            )*/

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
         /*   safeBookToFireStore(
                firestore,
                url.toString(),
                book,
                onSaved = {
                        onSaved()
                },
                onError = {
                        onError()
                }
            )*/
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

  /*  firestore: FirebaseFirestore,
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

}*/


