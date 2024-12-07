package com.kodex.bookmarketcompose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodex.bookmarketcompose.R
import com.kodex.bookmarketcompose.ui.theme.GrayLite



@Composable
fun DrawerBody() {
    // ClickableText() { }: (@Composable () -> Unit)? = null) {}
    val categoriesList = listOf(
        "Favorites",
        "Fantasy",
        "Drama",
        "Bestseller"
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.bottle),
            contentDescription = "",
            alpha = 0.2f,
            contentScale = ContentScale.Crop
        )
         Column (modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Categories",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                    )
             Spacer(modifier = Modifier.height(16.dp))
             Box(modifier = Modifier.fillMaxWidth()
                 .height(1.dp).background(GrayLite)
             )
             LazyColumn(Modifier.fillMaxSize()) {
                    items(categoriesList){item->
                        Column(
                            Modifier.fillMaxWidth()
                                .clickable{}
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = item,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth()

                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(GrayLite)
                            )
                        }

                    }
             }
         }
    }
}


        /*Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.bottle),
            contentDescription = "",
            alpha = 0.2f,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Categories",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold

            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(1.dp).background(GrayLite)
            )
            LazyColumn(Modifier.fillMaxSize()) {
                items(categoriesList) { item ->

                    Column(Modifier
                        .fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(GrayLite)
                        )
                    }

                }

            }
        }*/

