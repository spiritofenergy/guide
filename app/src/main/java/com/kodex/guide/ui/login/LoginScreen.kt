package com.kodex.guide.ui.login

import android.util.Log
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.data.MainScreenDataObject

@Composable
fun LoginScreen(
    onNavigationToMainScreen: (MainScreenDataObject)-> Unit
) {
    val errorState = remember {
        mutableStateOf("")
    }
    val auth = remember {
        Firebase.auth
    }
    val emailState = remember {
        mutableStateOf("nigmatullov@mail.ru")
    }
    val passwordState = remember {
        mutableStateOf("Faxer56!")
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


 /*   @Preview(showBackground = true)
    @Composable
    fun PreviewLoginScreen() {
        LoginScreen { navData ->
            navController.navigate(navData)
        }
    }
*/