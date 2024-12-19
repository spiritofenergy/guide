package com.kodex.guide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kodex.guide.ui.addscreen.AddBookScreen
import com.kodex.guide.ui.addscreen.data.AddScreenObject
import com.kodex.guide.ui.mainScreen.MenuScreen
import com.kodex.guide.ui.data.LoginScreenObject
import com.kodex.guide.ui.data.MainScreenDataObject
import com.kodex.guide.ui.login.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = LoginScreenObject
            ) {

                composable<LoginScreenObject> {
                    LoginScreen() { navData ->
                        navController.navigate(navData)
                    }
                }
                composable<MainScreenDataObject> { navEntry ->
                    val navData = navEntry.toRoute<MainScreenDataObject>()
                    MenuScreen(navData){
                        navController.navigate(AddScreenObject)
                    }
                }
                composable<AddScreenObject>{ navEntry ->
                    AddBookScreen()
                }
            }
        }
    }
}


/*

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookMarketComposeTheme {
        LoginScreen { navData ->
            navController.navigate(navData)
        }
    }
}*/
