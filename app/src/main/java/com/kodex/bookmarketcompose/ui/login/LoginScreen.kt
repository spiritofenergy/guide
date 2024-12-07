package com.kodex.bookmarketcompose.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodex.bookmarketcompose.R

@Composable
fun LoginScreen(){
    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }
            Image(painter = painterResource(
                id = R.drawable.wey),
                contentDescription =  "BG",
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

                Image(painter = painterResource(id = R.drawable.signboard),
                    contentDescription = "Logo",
                    modifier = Modifier.height(250.dp).padding(bottom = 50.dp)

                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Кучугуры",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(40.dp))
                RoundedCornerTextField(
                    text = emailState.value,
                    label = "Email:"
                ) {
                    emailState.value = it
                }
                Spacer(modifier = Modifier.height(16.dp))

                RoundedCornerTextField(
                    text = passwordState.value,
                    label = "Password:"
                ){
                    passwordState.value = it

                }

                Spacer(modifier = Modifier.height(16.dp))

                LoginButton(
                    text = "Sign in "){

                }
                LoginButton(text = "Sign up ") {

                }

            }
        }


    @Preview(showBackground = true)
    @Composable
    fun PreviewLoginScreen() {
        LoginScreen()
    }
