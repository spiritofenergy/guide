package com.kodex.bookmarketcompose.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kodex.bookmarketcompose.ui.bottomMenu.BottomMenu


 @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
 @Composable
fun MenuScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
             Column (Modifier.fillMaxWidth(0.7f)){
                DrawerHeader()
                DrawerBody()  }

        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomMenu()}
        ) {

        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

        MenuScreen()

}