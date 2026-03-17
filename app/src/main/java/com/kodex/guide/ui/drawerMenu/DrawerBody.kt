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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CrueltyFree
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Input
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
import com.kodex.guide.ui.mainScreen.MainScreenViewModel
import com.kodex.guide.ui.theme.ButtonColorBlue
import com.kodex.guide.ui.theme.GrayLite
import com.kodex.guide.ui.utils.Categories
import kotlinx.coroutines.launch


@Composable
fun DrawerBody(
    viewModel: MainScreenViewModel = hiltViewModel(),
    onFavesClick: ()-> Unit,
    onHomeClick: ()-> Unit,
    onAnimalsClick: ()-> Unit,
    onPlantsClick: ()-> Unit,
    onWorkClick: ()-> Unit,
    onServicesClick: ()-> Unit,
    onReal_estateClick: ()-> Unit,
    onEntertainmentsClick: ()-> Unit,
    onMiscellaneousClick: ()-> Unit,
    onAddBookClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onAdmin: (Boolean) -> Unit = {},
    onAdminClick: () -> Unit = {},
    onCategoryClick: (Int) -> Unit = {}
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
                     onCategoryClick(Categories.ANIMALS)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.Celebration,
                 text = categoryList[1],
                 onItemClick = {
                     onCategoryClick(Categories.PLANTS)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.CleaningServices,
                 text = categoryList[2],
                 onItemClick = {
                     onCategoryClick(Categories.WORK)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.MiscellaneousServices,
                 text = categoryList[3],
                 onItemClick = {
                     onCategoryClick(Categories.SERVICES)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.AddHomeWork,
                 text = categoryList[4],
                 onItemClick = {
                     onCategoryClick(Categories.REAL_ESTATE)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.Agriculture,
                 text = categoryList[5],
                 onItemClick = {
                     onCategoryClick(Categories.AUTO)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.ElectricalServices,
                 text = categoryList[6],
                 onItemClick = {
                     onCategoryClick(Categories.ELECTRONICS)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.AutoAwesome,
                 text = categoryList[7],
                 onItemClick = {
                     onCategoryClick(Categories.ENTERTAINMENTS)
                     coroutineScope.launch { drawerState.close() }
                 }
             )
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.Dialpad,
                 text = categoryList[8],
                 onItemClick = {
                     onCategoryClick(Categories.MISCELLANEOUS)
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
                        onAddBookClick()
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
                 iconDrawableId = Icons.Default.Input,
                 text = categoryAdmin[2],
                 onItemClick = {
                     onLoginClick()
                     coroutineScope.launch { drawerState.close() }
                 }
             )
           /*  DrawerMenuItem(
                 iconDrawableId = Icons.Default.Map,
                 text = categoryAdmin[3],
                 onItemClick = {
                    /// onAddBookClick()
                     coroutineScope.launch { driverState.close() }
                 }
             )*/
             DrawerMenuItem(
                 iconDrawableId = Icons.Default.Settings,
                 text = categoryAdmin[4],
                 onItemClick = {
                    // onMapClick()
                     coroutineScope.launch { drawerState.close() }
                 }
             )

            /*if (viewModel.isAdminState.value) Button(
                     onClick = {
                     viewModel.isAdmin {  }
                     onAdminClick()
               },
                     modifier = Modifier.fillMaxWidth()
                         .padding(5.dp),
                     colors = ButtonDefaults.buttonColors(
                         containerColor = DarkTransparentBlue
                     )
                 ) {
                     Text(text = "Admin panel")
                 }*/
                 /*Button(
                     onClick = {
                     viewModel.isAdmin {  }
                     onAdminClick()
               },
                     modifier = Modifier.fillMaxWidth()
                         .padding(5.dp),
                     colors = ButtonDefaults.buttonColors(
                         containerColor = DarkTransparentBlue
                     )
                 ) {
                     Text(text = "Добавить")
                 }*/
         }
    }
}




@Preview(showBackground = true)
@Composable
fun Preview(){
    DrawerBody(
        onFavesClick = {},
        onHomeClick = {},
        onAnimalsClick = {},
        onPlantsClick = {},
        onWorkClick = {},
        onServicesClick = {},
        onReal_estateClick = {},
        onEntertainmentsClick = {},
        onMiscellaneousClick = {},
        onAddBookClick = {},
        onLoginClick = {},
        onAdmin = {},
        onAdminClick = {},
        onCategoryClick = {}
    )
}

