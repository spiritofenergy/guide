package com.kodex.guide.ui.login.sign_up

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.data.NavRoutes
import com.kodex.guide.ui.login.LoginButton

@Composable
fun SignUpScreen(
    viewModel: SingUpViewModel = hiltViewModel(),
    onNavigationToMainScreen: (NavRoutes.MainScreenDataObject) -> Unit
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
        Text(
            text = "Create account",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )


        Spacer(modifier = Modifier.height(40.dp))

            RoundedCornerTextField(
                text = viewModel.nameState.value,
                label = "User nane:",
                isPassword = false
            ) {
                viewModel.nameState.value = it
            }
        Spacer(modifier = Modifier.height(5.dp))

        RoundedCornerTextField(
                text = viewModel.phoneNumberState.value,
                label = "Phone number:",
                isPassword = false
            ) {
                viewModel.phoneNumberState.value = it
            }
        Spacer(modifier = Modifier.height(5.dp))

        RoundedCornerTextField(
                text = viewModel.emailState.value,
                label = "Logon:",
                isPassword = false
            ) {
                viewModel.emailState.value = it
            }
            Spacer(modifier = Modifier.height(5.dp))

                RoundedCornerTextField(
                    text = viewModel.passwordState.value,
                    label = "Password:",
                    isPassword = true
                ) {
                    viewModel.passwordState.value = it
                }

                Spacer(modifier = Modifier.height(5.dp))

            if (viewModel.errorState.value.isNotEmpty()) {
                Text(
                    text = viewModel.errorState.value,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }



            LoginButton(
                text = "Sign Up"
            ) {
                viewModel.signUp(
                    onSignUpSuccess = { navData ->
                        onNavigationToMainScreen(navData)
                    }
                )
            }
       }
  }
@Preview(showBackground = true)
@Composable
fun ShowSingUpLoginScreen(){
   // SingUpLogonScreen()
}