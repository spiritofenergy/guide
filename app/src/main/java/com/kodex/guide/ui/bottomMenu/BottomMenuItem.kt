package com.kodex.guide.ui.bottomMenu

import com.kodex.bookmarketcompose.R


sealed class BottomMenuItem(
    val route: String,
    val title: String,
    val iconId: Int
) {
    object Home: BottomMenuItem(
        route = "home",
        title = "Home",
        iconId = R.drawable.ic_home
    )
    object Favorite: BottomMenuItem(
        route = "favorite",
        title = "Favorite",
        iconId = R.drawable.ic_favorite
    )
    object Settings: BottomMenuItem(
        route = "setting",
        title = "Setting",
        iconId = R.drawable.ic_settings
    )
}