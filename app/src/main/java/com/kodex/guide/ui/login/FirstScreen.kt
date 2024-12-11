 import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.layout.width
 import androidx.compose.material3.Button
 import androidx.compose.material3.Text
 import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
 import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
 import androidx.compose.ui.text.input.PasswordVisualTransformation
 import androidx.compose.ui.text.input.TextFieldValue
 import androidx.compose.ui.tooling.preview.Preview
 import androidx.compose.ui.unit.dp
 import com.kodex.bookmarketcompose.R
 import com.kodex.guide.ui.theme.BookMarketComposeTheme
 import com.kodex.guide.ui.theme.BoxFilterColor


 @Composable
fun FirstScreen(){
    val emailState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }


    Image(painter = painterResource(
        id = R.drawable.bottle),
        contentDescription =  "BG",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
     Box(modifier = Modifier.fillMaxSize()
         .background(BoxFilterColor))
   /*  Button(
         modifier = Modifier.height(20.dp),
         onClick = TODO(),

     ) { }*/

     Column(
         modifier = Modifier
             .fillMaxSize()
             .padding(46.dp),
         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
     ) {
         var username = TextFieldValue("")
         var password = TextFieldValue("")

         TextField(
             value = username,
             onValueChange = { username = it },
             label = { Text("Логин") },
             modifier = Modifier.width(270.dp),
             placeholder = { Text("Введите логин") }
         )

         Spacer(modifier = Modifier.height(16.dp))

         TextField(
             value = password,
             onValueChange = { password = it },
             label = { Text("Пароль") },
             modifier = Modifier.width(270.dp),
             placeholder = { Text("Введите пароль") },
             visualTransformation = PasswordVisualTransformation()
         )

         Spacer(modifier = Modifier.height(16.dp))

         Button(onClick = { /* Обработка входа */ }) {
             Text("Sign in")
         }
         Button(onClick = { /* Обработка входа */ }) {
             Text("Sign up")
         }
     }
        /*TextField(value = emailState.value, onValueChange = {
            emailState.value = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        val password
        OutlinedTextField(
            value = passwordState,
            onValueChange = { passwordState = it },
            label = { Text("Пароль") },
            modifier = Modifier.width(270.dp),
            placeholder = { Text("Введите пароль") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {

        }
                Spacer(modifier = Modifier.height(10.dp)
                )

*/

}

 @Preview(showBackground = true)
 @Composable
 fun GreetingPreview() {
     BookMarketComposeTheme {
         FirstScreen()
     }
 }

