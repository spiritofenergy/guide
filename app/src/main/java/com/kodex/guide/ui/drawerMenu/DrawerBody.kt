package com.kodex.guide.ui.drawerMenu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.mainScreen.DrawerListItem
import com.kodex.guide.ui.theme.ButtonColorBlue
import com.kodex.guide.ui.theme.DarkTransparentBlue
import com.kodex.guide.ui.theme.GrayLite
import com.kodex.guide.ui.utils.Categories
import com.kodex.guide.ui.utils.firebase.FireStoreManagerPaging


@Composable
fun DrawerBody(
    onAdmin: (Boolean) -> Unit = {},
    onAdminClick: () -> Unit = {},
    onCategoryClick: (Int) -> Unit = {},
 ) {

    val categoryList = stringArrayResource(id = R.array.category_arrays)
    val isAdminState = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        isAdmin { isAdmin ->
            isAdminState.value = isAdmin
            onAdmin(isAdmin)
        }
    }

    Box(modifier = Modifier.fillMaxSize()
        .background(ButtonColorBlue)) {
        // background first Screen
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.wey),
            contentDescription = "",
            alpha = 0.2f,
            contentScale = ContentScale.Crop
        )
         Column (modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
             Spacer(modifier = Modifier.height(16.dp))

             Box(modifier = Modifier.fillMaxWidth()
                 .height(1.dp).background(GrayLite)
             )

             DrawerListItem(title = stringResource(id = R.string.favorite)) {
              onCategoryClick(Categories.FAVORITES)
             }
             DrawerListItem(title = stringResource(id = R.string.all)) {

             onCategoryClick(Categories.ALL)
             }
             LazyColumn(Modifier.fillMaxWidth()) {
                 itemsIndexed(categoryList){index, title->
                     DrawerListItem(
                         title) {
                         onCategoryClick(index)
                     }
                 }
             }
            if (isAdminState.value)
                Button(
                     onClick = {
                     isAdmin {  }
                     onAdminClick()
               },
                     modifier = Modifier.fillMaxWidth()
                         .padding(5.dp),
                     colors = ButtonDefaults.buttonColors(
                         containerColor = DarkTransparentBlue
                     )
                 ) {
                     Text(text = "Admin panel")
                 }
                 Button(
                     onClick = {
                     isAdmin {  }
                     onAdminClick()
               },
                     modifier = Modifier.fillMaxWidth()
                         .padding(5.dp),
                     colors = ButtonDefaults.buttonColors(
                         containerColor = DarkTransparentBlue
                     )
                 ) {
                     Text(text = "Добавить")
                 }
         }
    }
}

fun isAdmin(onAdmin: (Boolean)-> Unit){
    val uid = Firebase.auth.currentUser!!.uid
    Firebase.firestore.collection("admin")
        .document(uid).get().addOnSuccessListener{
            onAdmin(it.get("isAdmin") as Boolean)
                Log.d("MyLog", "isAdmin: ${it.get("isAdmin")}")
        }
}

/*
@Preview(showBackground = true)
@Composable
fun Preview(){
    DrawerBody()
}
*/
