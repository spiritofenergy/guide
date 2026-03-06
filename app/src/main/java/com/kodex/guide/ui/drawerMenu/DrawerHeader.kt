package com.kodex.guide.ui.drawerMenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.theme.ButtonColor
import com.kodex.guide.ui.theme.ButtonColorBlue
import com.kodex.guide.ui.theme.DrawerColorBlue


@Composable
fun DrawerHeader(email: String) {
    Column(
        Modifier.fillMaxWidth()
            .height(200.dp)
            .background(DrawerColorBlue),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            modifier = Modifier.size(90.dp)
                .padding(top = 15.dp),
            painter = painterResource(id = R.drawable.emblem),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.taman_peninsula),
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
@Composable
@Preview(showBackground = true)
fun ShowDrawerHeder(){
    DrawerHeader("email")
}
