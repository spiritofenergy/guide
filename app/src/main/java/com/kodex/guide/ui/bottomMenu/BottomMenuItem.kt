package com.kodex.guide.ui.bottomMenu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.ui.input.key.Key.Companion.Bookmark
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
        titleId = R.string.faves,
        iconId = R.drawable.bookmark
    )
    object Settings: BottomMenuItem(
        route = "setting",
        titleId = R.string.settings,
        iconId = R.drawable.ic_settings
    )
}