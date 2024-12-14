package com.kodex.guide.ui.drawerMenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.theme.ButtonColor


@Composable
fun DrawerHeader(email: String) {
    Column(
        Modifier.fillMaxWidth()
            .height(170.dp)
            .background(ButtonColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

    ) {
        Image(
            modifier = Modifier.size(90.dp),
            painter = painterResource(id = R.drawable.signboard),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "поселок Кучугуры",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            email,
            color = Color.Green,
            fontSize = 16.sp)
        /*Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(1.dp).background(GrayLite)
        )*/

    }
}
