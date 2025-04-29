package com.kodex.guide.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.castom.MyDialog
import com.kodex.guide.ui.data.MainScreenDataObject

@Composable
fun LoginScreen(

    viewModel: LViewModel = hiltViewModel(),
    onNavigationToMainScreen: (MainScreenDataObject)-> Unit
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getAccountState()
        viewModel.getEmail()
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveLastEmail()
            viewModel.passwordState.value = ""
        }
    }

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
            text = "Тамань",
            color = Color.White,
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = "Guide",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(50.dp))

        if(viewModel.currentUser.value == null) {
            RoundedCornerTextField(
                text = viewModel.emailState.value,
                label = "Email:",
                isPassword = false
            ) {
                viewModel.emailState.value = it
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (!viewModel.resetPasswordState.value) {
            RoundedCornerTextField(
                text = viewModel.passwordState.value,
                label = "Password:",
                isPassword = true
            ) {
                viewModel.passwordState.value = it
            }
            Spacer(modifier = Modifier.height(16.dp))
            }
            if (viewModel.errorState.value.isNotBlank()) {
                Text(
                    text = viewModel.errorState.value,
                    color = Color.Red,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (!viewModel.resetPasswordState.value) {
                LoginButton(text = "Вход ") {
                    viewModel.signIn(
                        onSignInSuccess = { navData ->
                            onNavigationToMainScreen(navData)
                        }
                    )
                }
            }
            LoginButton(text = if (viewModel.resetPasswordState.value){
                "Reset Password"

            } else {
                "SignUp"
            }
            ) {
                viewModel.signUp(
                    onSignUpSuccess = { navData ->
                        onNavigationToMainScreen(navData)
                       // viewModel.successState.value = true
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (!viewModel.resetPasswordState.value){
                Text(
                    modifier = Modifier.clickable{
                        viewModel.errorState.value = ""
                        viewModel.resetPasswordState.value = true
                    },
                     text = "Forget Password",
                     color = Color.White
                )
            }
        }else{
            LoginButton(text = "Enter ") {
                onNavigationToMainScreen(
                    MainScreenDataObject(
                        viewModel.currentUser.value!!.uid,
                        viewModel.currentUser.value!!.email!!
                    )
                )
            }


            LoginButton(text = "SignOut ") {
                viewModel.signOut()
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        MyDialog(showDialog = viewModel.showResetPasswordDialog.value,
            onDismiss = {
                viewModel.showResetPasswordDialog.value = false
            },
            onConfirm = {
                viewModel.showResetPasswordDialog.value = false
            },
            massage = stringResource(R.string.reset_password_dialog   )
        )
    }
}


