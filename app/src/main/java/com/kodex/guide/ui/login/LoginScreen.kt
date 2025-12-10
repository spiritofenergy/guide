package com.kodex.guide.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.addscreen.data.AddBookViewModel
import com.kodex.guide.ui.castom.MyDialog
import com.kodex.guide.ui.data.MainScreenDataObject

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigationToMainScreen: (MainScreenDataObject) -> Unit
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
        Text(
            text = "Тамань",
            color = Color.White,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = "Guide",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(40.dp))

        if (viewModel.currentUser.value == null) {
            RoundedCornerTextField(
                text = viewModel.emailState.value,
                label = "Логин:",
                isPassword = false
            ) {
                viewModel.emailState.value = it
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (!viewModel.resetPasswordState.value) {
                RoundedCornerTextField(
                    text = viewModel.passwordState.value,
                    label = "Пароль:",
                    isPassword = true
                ) {
                    viewModel.passwordState.value = it
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
            if (viewModel.errorState.value.isNotEmpty()) {
                Text(
                    text = viewModel.errorState.value,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (!viewModel.resetPasswordState.value) {
                LoginButton(text = "Вход") {
                    viewModel.signIn(
                        onSignInSuccess = { navData ->
                            onNavigationToMainScreen(navData)
                        }
                    )
                }
            }
            LoginButton(
                text = if (viewModel.resetPasswordState.value) {
                    "Восстановить пароль "
                } else {
                    "Авторизация"
                }
            ) {
                viewModel.signUp(
                    onSignUpSuccess = { navData ->
                        onNavigationToMainScreen(navData)
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (!viewModel.resetPasswordState.value) {
                Text(
                    modifier = Modifier.clickable {
                        viewModel.errorState.value = ""
                        viewModel.resetPasswordState.value = true
                    },
                    text = "Напомнить пароль",
                    color = Color.White
                )
            }
        } else {
            Spacer(modifier = Modifier.height(10.dp))
            LoginButton(text = "Вход") {
                onNavigationToMainScreen(
                    MainScreenDataObject(
                        viewModel.currentUser.value!!.uid,
                        viewModel.currentUser.value!!.email!!
                    )
                )
            }
            LoginButton(text = "Выход") {
                viewModel.signOut()
            }
        }
        MyDialog(
            showDialog = viewModel.showResetPasswordDialog.value,
            onDismiss = {
                viewModel.showResetPasswordDialog.value = false
            },
            onConfirm = {
                viewModel.showResetPasswordDialog.value = false
            },
            massage = stringResource(R.string.reset_password_dialog)
        )
    }
}


/*



            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(46.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                     Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Кучугуры",
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(10.dp))

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
                if(errorState.value.isNotBlank()){
                    Text(
                        text = errorState.value,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
                LoginButton(
                    text = "Sign in ",
                  //  color = Color.White,
                    ){
                    signIn(
                        auth,
                        emailState.value,
                        passwordState.value,
                        onSignInSuccess = { navData ->
                            onNavigationToMainScreen(navData)
                        },
                        onSignInFailure = {error->
                            errorState.value = error
                            Log.d("MyTag", "Sign In Failure: $error")
                        }
                    )
                    Log.d("MyTeg", "Press SignIn Button")
                }

                LoginButton(text = "Sign up ") {
                    signUp(
                        auth,
                        emailState.value,
                        passwordState.value,
                        onSignUpSuccess = {navData ->
                            onNavigationToMainScreen(navData)
                        },
                        onSignUpFailure = { error ->
                            Log.d("MyTeg", "Sign Up Failure: $error")
                        }
                    )
                }
            }
        }

fun signUp(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignUpSuccess: (MainScreenDataObject)-> Unit,
    onSignUpFailure: (String)-> Unit
){
    if (email.isBlank()|| password.isBlank()){
        onSignUpFailure("Email snd Password be empty")
        return
    }
    auth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener{task->
            if(task.isSuccessful)onSignUpSuccess
        }
        .addOnFailureListener(){
            onSignUpFailure(it.message ?: "Sign Up Error")
        }

}
fun signIn(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignInSuccess: (MainScreenDataObject)-> Unit,
    onSignInFailure: (String)-> Unit
){
    if (email.isBlank()|| password.isBlank()){
        onSignInFailure("Email snd Password be empty")
        return
    }

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful)
                onSignInSuccess(MainScreenDataObject(
                    task.result.user?.uid!!,
                    task.result.user?.email!!

                ))
        }
        .addOnFailureListener() {
            onSignInFailure(it.message ?: "Sign Up Error")
        }
}

*/
/*   @Preview(showBackground = true)
   @Composable
   fun PreviewLoginScreen() {
       LoginScreen { navData ->
           navController.navigate(navData)
       }
   }
*/