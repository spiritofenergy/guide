package com.kodex.guide.ui.bottomMenu

import com.kodex.bookmarketcompose.R


sealed class BottomMenuItem(
    val route: String,
    val titleId: Int,
    val iconId: Int
) {
    object Home: BottomMenuItem(
        route = "home",
        titleId = R.string.home,
        iconId = R.drawable.ic_home
    )
    object Faves: BottomMenuItem(
        route = "favorite",
        titleId = R.string.favorite,
        iconId = R.drawable.ic_favorite
    )
    object Settings: BottomMenuItem(
        route = "setting",
        titleId = R.string.settings,
        iconId = R.drawable.ic_settings
    )
}