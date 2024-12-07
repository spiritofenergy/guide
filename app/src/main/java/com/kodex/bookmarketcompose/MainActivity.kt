package com.kodex.bookmarketcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kodex.bookmarketcompose.ui.MenuScreen
import com.kodex.bookmarketcompose.ui.login.LoginScreen
import com.kodex.bookmarketcompose.ui.theme.BookMarketComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookMarketComposeTheme {
                //MenuScreen()
                LoginScreen()
                   // LoginScreen()
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookMarketComposeTheme {
        LoginScreen()
    }
}