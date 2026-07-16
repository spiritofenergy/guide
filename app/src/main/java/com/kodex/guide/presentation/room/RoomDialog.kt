package com.kodex.guide.presentation.room

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.kodex.bookmarketcompose.R
import com.kodex.guide.presentation.settingsScreen.TimeUtils
import kotlin.text.ifEmpty


@Composable
fun RoomDialog(
    title: String,
    showDialog: Boolean,
    dialogType: DialogType = DialogType.SAVE,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var postName by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                postName = ""
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDismiss()
                        postName = ""
                    }
                ) {
                    Text(text = stringResource(R.string.cansel))
                }
                Button(
                    onClick = {
                        onConfirm(
                            postName.ifEmpty {
                                "Track_${TimeUtils.getDateAndTime()}"
                            })
                        postName = ""
                    }
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            },
            title = {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 20.sp
                )
            },
            text = {
                if (dialogType == DialogType.SAVE){
                    TextField(
                        value = postName,
                        onValueChange = { text ->
                            postName = text
                        },
                        label = {
                            Text(text = stringResource(R.string.enter_track_name))
                        }
                    )
                }else {
                    Text(text = stringResource(R.string.delete_dialog_massage))
                }
            }
        )
    }
}
enum class DialogType {
    DELETE, SAVE
}