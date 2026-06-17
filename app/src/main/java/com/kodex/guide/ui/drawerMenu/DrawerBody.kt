package com.kodex.guide.ui.drawerMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddHomeWork
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Attractions
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CrueltyFree
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kodex.bookmarketcompose.R
import com.kodex.guide.presentation.home.HomeViewModel
import com.kodex.guide.ui.theme.ButtonColorBlue
import com.kodex.guide.ui.theme.GrayLite
import com.kodex.guide.domain.model.BookCategories
import kotlinx.coroutines.launch


@Composable
fun DrawerBody(
    viewModel: HomeViewModel = hiltViewModel(),

    onAddBookClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onTrackClick: () -> Unit = {},
    onAdmin: (Boolean) -> Unit = {},
    onAdminClick: () -> Unit = {},
    onCategoryClick: (BookCategories) -> Unit = {}
) {

    val categoryList = stringArrayResource(id = R.array.category_array)
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val categoryAdmin = stringArrayResource(id = R.array.category_admin)

    Box(modifier = Modifier.fillMaxSize()
        .background(ButtonColorBlue)) {
        // background first Screen
         Column (modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
             Spacer(modifier = Modifier.height(16.dp))

             Box(modifier = Modifier
                 .fillMaxWidth()
                 .height(1.dp)
                 .background(GrayLite)
             )
             Spacer(modifier = Modifier.height(16.dp))

             DrawerMenuItem(
                 iconDrawableId = Icons.Default.CrueltyFree,
                 text = categoryList[0],
                 onItemClick = {
                     onCategoryClick(BookCategories.ANIMALS)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.Celebration,
                 text = categoryList[1],
                 onItemClick = {
                     onCategoryClick(BookCategories.PLANTS)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.CleaningServices,
                 text = categoryList[2],
                 onItemClick = {
                     onCategoryClick(BookCategories.WORK)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.MiscellaneousServices,
                 text = categoryList[3],
                 onItemClick = {
                     onCategoryClick(BookCategories.SERVICES)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.AddHomeWork,
                 text = categoryList[4],
                 onItemClick = {
                     onCategoryClick(BookCategories.REAL_ESTATE)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.Agriculture,
                 text = categoryList[5],
                 onItemClick = {
                     onCategoryClick(BookCategories.AUTO)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.ElectricalServices,
                 text = categoryList[6],
                 onItemClick = {
                     onCategoryClick(BookCategories.ELECTRONICS)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.AutoAwesome,
                 text = categoryList[7],
                 onItemClick = {
                     onCategoryClick(BookCategories.ENTERTAINMENTS)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.Dialpad,
                 text = categoryList[8],
                 onItemClick = {
                     onCategoryClick(BookCategories.MISCELLANEOUS)
                     coroutineScope.launch { drawerState.close() }
                 }
             )


             Spacer(modifier = Modifier.height(15.dp))
             Box(modifier = Modifier
                     .fillMaxWidth()
                     .height(1.dp)
                     .background(GrayLite))
             Spacer(modifier = Modifier.height(15.dp))


             if (viewModel.isAdminState.value)
                 DrawerMenuItem(
                     iconDrawableId = Icons.Default.Security,
                     text = categoryAdmin[0],
                     onItemClick = {
                         onAdminClick()
                         coroutineScope.launch { drawerState.close() }
                     }
                 )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.Add,
                 text = categoryAdmin[1],
                 onItemClick = {
                     onAddBookClick()
                     coroutineScope.launch { drawerState.close() }
                 }
             )

             DrawerMenuItem(
                 iconDrawableId = if (viewModel.isAdminState.value) Icons.Default.Login else Icons.Default.Logout,
                 text = if (viewModel.isAdminState.value) categoryAdmin[2] else categoryAdmin[3],
                 onItemClick = {
                     onLoginClick()
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.Settings,
                 text = categoryAdmin[5],
                 onItemClick = {
                     onSettingsClick()
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.Attractions,
                 text = categoryAdmin[5],
                 onItemClick = {
                     onTrackClick()
                     coroutineScope.launch { drawerState.close() }
                 }
             )
         }
    }
}




@Preview(showBackground = true)
@Composable
fun Preview(){
    DrawerBody(
        onAddBookClick = {},
        onLoginClick = {},
        onAdmin = {},
        onAdminClick = {},
        onCategoryClick = {}
    )
}

