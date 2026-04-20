package com.kodex.guide

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kodex.guide.ui.settingsScreen.SettingsScreen
import com.kodex.guide.ui.addscreen.AddBookScreen
 import com.kodex.guide.presentation.admin_panel.AdminPanelScreen
import com.kodex.guide.presentation.admin_panel.ModerationScreen
import com.kodex.guide.presentation.comments.CommentsScreen
import com.kodex.guide.ui.mainScreen.MenuScreen
import com.kodex.guide.ui.detailScreen.DetailScreen
import com.kodex.guide.ui.login.LoginScreen
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.ui.login.sign_up.SignUpScreen
import com.kodex.guide.ui.parallaxScreen.ParallaxScreen
import com.kodex.guide.ui.placeScreen.PlaceScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
               startDestination = NavRoutes.LoginNavObject
              /*  startDestination = NavRoutes.MainScreenDataObject(
                    uid = "uid",
                    email = "email"
                )*/
            ) {

                composable<NavRoutes.LoginNavObject> {
                    LoginScreen(
                        onNavigationToMainScreen = { navData ->
                            navController.navigate(navData)
                        },
                        onNavigationToSignUpScreen = { navData ->
                            navController.navigate(navData)
                        }
                    )
                }
                            composable<NavRoutes.SingUpNavObject> {
                                SignUpScreen() { navData ->
                                    navController.navigate(navData)
                                }
                            }

                composable<NavRoutes.HomeDataObject> { navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.HomeDataObject>()
                    MenuScreen(
                        navData = navData,

                        onBookClick = { place ->
                            navController.navigate(
                                NavRoutes.ParallaxNavObject(
                                    bookId = place.key,
                                    title = place.title,
                                    description = place.description,
                                    price = place.price,
                                    categoryIndex = place.categoryIndex,
                                    imageUrl = place.imageUrl,
                                    telephone = place.telephone,
                                    ratingsList = place.ratingsList,


                                    )
                            )
                        },

                        /*  onBookClick = { bk ->
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
                        },*/
                        onBookEditClick = { book ->
                            navController.navigate(
                                NavRoutes.AddScreenObject(
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
                            navController.navigate(NavRoutes.ModerationNavObject)
                        },
                        onLoginClick = {
                            navController.navigate(NavRoutes.LoginNavObject)
                        },
                        onSettingsClick = {
                            navController.navigate(NavRoutes.SettingsNavObject)
                        },
                        onAddBookClick = {
                            navController.navigate(NavRoutes.AddScreenObject())
                        }
                    )
                }
                composable<NavRoutes.AddScreenObject> { navEntry ->
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
                        navObject = navData
                    )
                }

                composable<NavRoutes.PlaceNavObject> { navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.PlaceNavObject>()
                    PlaceScreen(
                        onCommentsClick = { commentsNavData ->
                            navController.navigate(commentsNavData)
                        },
                        navObject = navData,
                        navController = navController
                    )
                }

                composable<NavRoutes.AdminPanelNavObject> {
                    AdminPanelScreen(
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
                composable<NavRoutes.SettingsNavObject> { navEntry ->
                    SettingsScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onCloseAccountClick = {
                            navController.popBackStack(
                                NavRoutes.LoginNavObject,
                                inclusive = false
                            )
                        }
                    )
                }


                composable<NavRoutes.ParallaxNavObject>{navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.ParallaxNavObject>()
                    ParallaxScreen(
                        navObject = navData,
                        onBackPressed = { navController.popBackStack() },
                        onCallTaxi = { _, _ -> /* Позвонить */ },
                        onNavigateToReviews = {}
                    )
                }
                composable<NavRoutes.CommentsNavData> {navEntry ->
                    val navData = navEntry.toRoute<NavRoutes.CommentsNavData>()
                    CommentsScreen(
                        navObject = navData)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.updateLastVisit()
    }
}


