package com.kodex.bookmarketcompose.ui.bottomMenu

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun BottomMenu() {
   /* val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favs,
        BottomMenuItem.Setting

      )
    */
    val selectedItem = remember { mutableStateOf("Home") }

    NavigationBar {

    }
}
