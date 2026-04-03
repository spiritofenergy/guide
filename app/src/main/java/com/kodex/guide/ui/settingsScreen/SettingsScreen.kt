package com.kodex.guide.ui.settingsScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.dialods.AccountDialog
import com.kodex.guide.ui.dialods.MyDialog
import com.kodex.guide.ui.settingsScreen.menuItems.DialogMenuItem
import com.kodex.guide.ui.settingsScreen.menuItems.DropDownMenuItem
import com.kodex.guide.ui.settingsScreen.menuItems.MenuCategoryItem
import com.kodex.guide.ui.theme.ButtonColorBlue


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCloseAccountClick: () -> Unit = {}
) {
    var dialogData by remember { mutableStateOf(AccountDialogData()) }
    val dropDownMenuSelectedOptions = remember { mutableStateListOf(0,0,0) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 55.dp)
    ) {
        MyDialog(
            showDialog = showConfirmDeleteDialog,
            onDismiss = { showConfirmDeleteDialog = false },
            onConfirm = {
                viewModel.deleteAccount(
                    onAccountDeleted = {
                        Toast.makeText(context, R.string.you_account_was_deleted, Toast.LENGTH_LONG).show()
                        viewModel.signOut()
                        onCloseAccountClick()
                    },
                    onAccountDeleteFailure = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    },
                )
            },
            message = context.getString(R.string.deleted_account_massage),
            confirmButtonText = context.getString(R.string.deleted_account_massage)
        )
        AccountDialog(
            dialogData = dialogData,
            onConfirm = { fieldValuesList, dialogType ->
                dialogData = AccountDialogData(showDialog = false)
                when(dialogType){
                    DialogType.PASSWORD -> {
                        viewModel.resetPassword(
                            fieldValuesList[0],
                            onResetPasswordFailure = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                            },
                            onResetPasswordSuccess = {
                                Toast.makeText(
                                    context,
                                    R.string.reset_password_dialog,
                                    Toast.LENGTH_LONG
                                ).show()
                                viewModel.signOut()
                                onCloseAccountClick()
                            }
                        )
                    }
                    DialogType.DELETE_ACCOUNT -> {
                        viewModel.emailToDelete = fieldValuesList[0]
                        viewModel.passwordToDelete = fieldValuesList[1]
                        showConfirmDeleteDialog = true
                    }
                    else -> {
                    }
                }
            },
            onDismiss = {
                dialogData = AccountDialogData(showDialog = false)
            }
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ButtonColorBlue)
                    .padding(10.dp)
            ) {
                Text(
                    text = "   Billy Smack",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "   Email: example@gmail.com")
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "   Address: Moscow, Tverskaya 17")
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "   Telephone:  +79197716667")
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "   Last visit: 12/03/2025 12:30")
            }
        }
        Spacer(modifier = Modifier.height(5.dp))

        Card(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 5.dp)
        ) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .background(ButtonColorBlue)
            ) {
                items(CategoryMenuList.menuItemList) { item ->
                    when (item) {
                        is MenuItem.CategoryItem -> {
                            MenuCategoryItem(item.title)
                            Spacer(modifier = Modifier.height(5.dp))
                        }

                        is MenuItem.DialogItem -> {
                            DialogMenuItem(
                                item,
                                onItemClick = { dialogType, title, labelsArrayId ->
                                    dialogData = AccountDialogData(
                                        title = title,
                                        dialogType = dialogType,
                                        showDialog = true,
                                        fieldLabel = context.resources.getStringArray(labelsArrayId)
                                            .toList()
                                    )
                                }
                            )
                        }

                        is MenuItem.DropDownItem -> {
                            DropDownMenuItem(
                                item,
                                selectedOption = dropDownMenuSelectedOptions[item.menuType.ordinal],
                                onItemClick = { selectedIndex ->
                                    dropDownMenuSelectedOptions[item.menuType.ordinal] = selectedIndex

                                }
                            )
                        }
                    }
                }
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            onClick = {
                onBackClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonColorBlue
            )
        ) {
            Text(text = "Back")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),

            onClick = {
                /*TODO*/
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonColorBlue
            )
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ShowSettingsScreen() {
    SettingsScreen()
}