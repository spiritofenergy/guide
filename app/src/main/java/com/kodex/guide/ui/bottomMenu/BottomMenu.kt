package com.kodex.guide.ui.bottomMenu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kodex.guide.ui.theme.PurpleGrey80

@Composable
fun BottomMenu(
    selectedItem: Int,
    onFavesClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
) {
    val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Faves,
        BottomMenuItem.Settings

    )


    NavigationBar(
        containerColor = PurpleGrey80
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item.titleId,
                onClick = {
                    when (item.titleId) {
                        BottomMenuItem.Home.titleId -> onHomeClick()
                        BottomMenuItem.Faves.titleId -> onFavesClick()
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.titleId)
                    )
                },
                /* colors = NavigationDrawerItemDefaults.colors(
                     selectedIconColor = DarkBlue,
                     selectedTextColor = DarkBlue,
                     unselectedIconColor = DarkBlue,
                     unselectedBadgeColor = DarkBlue

                 )*/

            )


        }


    }
}
