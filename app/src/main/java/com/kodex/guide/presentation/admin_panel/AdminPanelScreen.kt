package com.kodex.guide.presentation.admin_panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kodex.guide.presentation.login.LoginButton

@Preview(showBackground = true)
@Composable
fun AdminPanelScreen(
    onAddBookClick: () -> Unit = {},
    onModerationClick: () -> Unit = {},
    //navData: AdminPanelNavObject
) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoginButton(text =
            "Add new book "
        ){
            onAddBookClick()

        }
        LoginButton(text =
            "Comment moderation "
        ) {
            onModerationClick()

        }
    }
}