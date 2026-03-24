package com.kodex.guide

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kodex.guide.ui.addscreen.AddBookScreen
 import com.kodex.guide.ui.adminPanel.AdminPanelScreen
import com.kodex.guide.ui.adminPanel.ModerationScreen
import com.kodex.guide.ui.commentsScreen.CommentsScreen
import com.kodex.guide.ui.mainScreen.MenuScreen
import com.kodex.guide.ui.detailScreen.DetailScreen
import com.kodex.guide.ui.login.LoginScreen
import com.kodex.guide.ui.data.NavRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = NavRoutes.LoginScreenObject
            ) {

                composable<NavRoutes.LoginScreenObject> {
                    LoginScreen() { navData ->
                        navController.navigate(navData)
                    }
                }
                composable<NavRoutes.MainScreenDataObject> { navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.MainScreenDataObject>()
                    MenuScreen(
                        navData = navData,
                        onBookClick = { bk ->
                            navController.navigate(
                                NavRoutes.DetailNavObject(
                                    bookId = bk.key,
                                    title = bk.title,
                                    description = bk.description,
                                    price = bk.price.toString(),
                                    telephone = bk.telephone,
                                    categoryIndex = bk.categoryIndex,
                                    imageUrl = bk.imageUrl,
                                    ratingsList = bk.ratingsList,
                                )
                            )
                        },
                        onBookEditClick = { book->
                            navController.navigate(NavRoutes.AddScreenObject(
                                key = book.key,
                                title = book.title,
                                description = book.description,
                                price = book.price,
                                categoryIndex = book.categoryIndex,
                                imageUrl = book.imageUrl,
                                )
                            )
                        },
                        onAdminClick = {
                            navController.navigate(NavRoutes.AdminPanelNavObject)
                        },
                        onLoginClick = {
                            navController.navigate(NavRoutes.LoginScreenObject)
                        },
                        onAddBookClick = {
                            navController.navigate(NavRoutes.AddScreenObject())
                        }
                    )

                }
                composable<NavRoutes.AddScreenObject>{ navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.AddScreenObject>()
                    AddBookScreen(
                        navData = navData,
                        onSaved = {
                            navController.popBackStack()
                        }
                    )
                }
                composable<NavRoutes.DetailNavObject> { navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.DetailNavObject>()
                    DetailScreen(
                        onCommentsClick = { commentsNavData ->
                            navController.navigate(commentsNavData)
                    },
                        navObject = navData)
                }

                composable<NavRoutes.AdminPanelNavObject> {
                    AdminPanelScreen (
                        onAddBookClick = {
                            navController.navigate(NavRoutes.AddScreenObject())
                        },
                        onModerationClick = {
                            navController.navigate(NavRoutes.ModerationNavObject)
                        }
                    )
                }
                composable<NavRoutes.ModerationNavObject> {
                    ModerationScreen()
                }
                composable<NavRoutes.CommentsNavData> {navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.CommentsNavData>()
                    CommentsScreen(
                        navObject = navData)
                }
            }
        }
    }
}


