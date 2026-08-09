package com.kodex.guide.presentation.home

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kodex.bookmarketcompose.R
import com.kodex.guide.domain.model.User
import com.kodex.guide.domain.model.UserRole
import com.kodex.guide.ui.theme.DrawerColorBlue


@Composable
fun DrawerHeader(
    email: String,
    role: UserRole = UserRole.ANONYMOUS,
    userName: String? = null ) {

    Column(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(DrawerColorBlue),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            modifier = Modifier
                .size(90.dp)
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
        // ✅ Имя пользователя (показываем, только если задано)
        if (!userName.isNullOrEmpty()) {
            Text(
                text = userName,
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // ✅ Email или Anonymous
        Text(
            text = email.ifEmpty { "Anonymous" },
            color = Color.Black,
            fontSize = 16.sp
        )

        // ✅ Роль
        Text(
            text = roleLabel(role),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
       /* Text(text =
            email.orEmpty().ifEmpty { "Anonymous" },
            color = Color.Black,
            fontSize = 16.sp)*/


    }
}
/** Человекочитаемая подпись роли */
private fun roleLabel(role: UserRole): String = when (role) {
    UserRole.ANONYMOUS -> "Гость"
    UserRole.USER -> "Пользователь"
    UserRole.BUSINESS -> "Бизнес"
    UserRole.PREMIUM -> "Премиум"
    UserRole.ADMIN -> "Администратор"
}
@Composable
@Preview(showBackground = true)
fun ShowDrawerHeder(){
    DrawerHeader("email")
}
