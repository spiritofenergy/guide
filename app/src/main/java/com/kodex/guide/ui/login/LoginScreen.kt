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
import com.kodex.bookmarketcompose.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kodex.guide.ui.data.MainScreenDataObject
import com.kodex.guide.ui.theme.BoxFilterColor
import com.kodex.guide.ui.theme.ButtonColor
import kotlinx.serialization.descriptors.PrimitiveKind

@Composable
fun LoginScreen(
    onNavigationToMainScreen: (MainScreenDataObject)-> Unit
) {
    val errorState = remember {
        mutableStateOf("")
    }
    val isUpload = remember {
        mutableStateOf(false)
   }
    val auth = remember {
        Firebase.auth
    }
    val emailState = remember {
        mutableStateOf("test@mail.ru")
    }

  /*  val context = Context.
    val text = "Пора покормить кота!"
    val duration = Toast.LENGTH_SHORT
*/
    val passwordState = remember {
        mutableStateOf("test01")
    }
            Image(painter = painterResource(
                id = R.drawable.bereg),
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

                Spacer(modifier = Modifier.height(50.dp))
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
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                LoginButton(
                    text = "Вход ",
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

                LoginButton(text = "Регистрация ") {
                    isUpload.value = true
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
                Spacer(modifier = Modifier.height(20.dp))
                Box{
                    if (isUpload.value == true)
                        CircularProgressIndicator(
                        modifier = Modifier.height(30.dp),
                        color = ButtonColor
                    )
                }
            }
        }



fun signUp(
  //  isUpload: Boolean = true,
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
   // isUpload

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


