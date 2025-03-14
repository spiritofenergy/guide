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
import com.kodex.guide.ui.detailScreen.DetailNavObject
import com.kodex.guide.ui.detailScreen.DetailScreen
import com.kodex.guide.ui.login.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = LoginScreenObject) {

                composable<LoginScreenObject> {
                    LoginScreen() { navData ->
                        navController.navigate(navData)
                    }
                }
                composable<MainScreenDataObject> { navEntry ->
                    val navData = navEntry.toRoute<MainScreenDataObject>()
                    MenuScreen(
                        navData = navData,
                        onBookClick = { bk->
                            navController.navigate(DetailNavObject(
                                title = bk.title,
                                description = bk.description,
                                price = bk.price,
                                category = bk.category,
                                imageUrl = bk.imageUrl
                            ))
                        },
                        onBookEdinClick = { book->
                            navController.navigate(AddScreenObject(
                                key = book.key,
                                title = book.title,
                                description = book.description,
                                price = book.price,
                                category = book.category,
                                imageUrl = book.imageUrl
                            ))
                        }){
                        navController.navigate(AddScreenObject())
                    }
                }
                composable<AddScreenObject>{ navEntry ->
                    val navData = navEntry.toRoute<AddScreenObject>()
                    AddBookScreen(navData){
                        navController.popBackStack()
                    }
                }
                composable< DetailNavObject>{ navEntry ->
                    val navData = navEntry.toRoute<DetailNavObject>()
                    DetailScreen(navData)

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
