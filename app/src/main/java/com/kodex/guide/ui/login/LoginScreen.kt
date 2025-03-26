package com.kodex.guide.ui.login

import android.R.attr.strokeWidth
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kodex.bookmarketcompose.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kodex.guide.ui.data.MainScreenDataObject
import com.kodex.guide.ui.mainScreen.MsViewModel
import com.kodex.guide.ui.theme.BoxFilterColor
import com.kodex.guide.ui.theme.ButtonColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.descriptors.PrimitiveKind

@Composable
fun LoginScreen(
    viewModel: LViewModel = hiltViewModel(),
    onNavigationToMainScreen: (MainScreenDataObject)-> Unit
) {

    Image(
        painter = painterResource(
            id = R.drawable.bereg
        ),
        contentDescription = "BG",
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

        /*      Image(painter = painterResource(id = R.drawable.signboard),
                    contentDescription = "Logo",
                    modifier = Modifier.height(250.dp).padding(bottom = 50.dp)

                )*/

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Кучугуры",
            color = Color.Black,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(50.dp))
        RoundedCornerTextField(
            text = viewModel.emailState.value,
            label = "Email:",
            isPassword = false
        ) {
            viewModel.emailState.value = it
        }
        Spacer(modifier = Modifier.height(16.dp))

        RoundedCornerTextField(
            text = viewModel.passwordState.value,
            label = "Password:",
            isPassword = true
        ) {
            viewModel.passwordState.value = it

        }

        Spacer(modifier = Modifier.height(16.dp))
        if (viewModel.errorState.value.isNotBlank()) {
            Text(
                text = viewModel.errorState.value,
                color = Color.Red,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        LoginButton(text = "Вход ") {
            viewModel.signIn(
                onSignInSuccess = {navData->
                    onNavigationToMainScreen(navData)
                }
            )
        }

        LoginButton(text = "Регистрация ") {
            viewModel.signUp(
                onSignUpSuccess = {navData->
                    onNavigationToMainScreen(navData)
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}



