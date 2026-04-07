package com.kodex.guide.ui.settingsScreen

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import com.kodex.guide.ui.settingsScreen.data.AddressData
import com.kodex.guide.ui.settingsScreen.data.PersonalData
import com.kodex.guide.ui.settingsScreen.data.UserSettingsData
import java.nio.file.Files.size


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCloseAccountClick: () -> Unit = {}
) {
    var dialogData by remember { mutableStateOf(AccountDialogData()) }
    val dropDownMenuSelectedOptions = remember { mutableStateListOf(0, 0, 0) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getSettings(onSettingsLoaded = { settingsData ->
            dropDownMenuSelectedOptions[0] = settingsData.imageFormat
            dropDownMenuSelectedOptions[1] = settingsData.quality
            dropDownMenuSelectedOptions[2] = settingsData.size

             }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 55.dp)
    ) {
        MyDialog(
            showDialog = showConfirmDeleteDialog,
            onDismiss = {
                showConfirmDeleteDialog = false
            },
            onConfirm = {
                viewModel.deleteAccount(
                    onAccountDeleted = {
                        Toast.makeText(context, R.string.you_account_was_deleted, Toast.LENGTH_LONG)
                            .show()
                        viewModel.signOut()
                        onCloseAccountClick()
                    },
                    onAccountDeleteFailure = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                )
            },

            title = stringResource(R.string.attention),
            message = stringResource(R.string.deleted_account_massage),
            confirmButtonText = stringResource(R.string.delete)
        )
        AccountDialog(
            dialogData = dialogData,
            onConfirm = { fieldValuesList, dialogType ->
                dialogData = AccountDialogData(showDialog = false)
                when (dialogType) {
                    DialogType.PERSONAL_DATA -> {
                        viewModel.newPersonalData = PersonalData(
                            name = fieldValuesList[0],
                            phone = fieldValuesList[1]
                        )
                    }

                    DialogType.ADDRESS -> {
                        viewModel.newAddressData = AddressData(
                            city = fieldValuesList[0],
                            street = fieldValuesList[1],
                            flat = fieldValuesList[2],
                            postCode = fieldValuesList[3]
                        )
                    }

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
                        if (fieldValuesList[0].isEmpty() || fieldValuesList[1].isEmpty()) {
                            Toast.makeText(
                                context, R.string.the_fields_on_may_be_empty,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            viewModel.emailToDelete = fieldValuesList[0]
                            viewModel.passwordToDelete = fieldValuesList[1]
                            showConfirmDeleteDialog = true
                        }
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
                    text = viewModel.personalData.value.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "   Email: example@gmail.com")
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "   Address: ${viewModel.addressData.value.toStringAddress()}")
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "   Tel:  ${viewModel.personalData.value.phone}")
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
                                            .toList(),
                                        fieldValues = when (dialogType) {
                                            DialogType.PERSONAL_DATA -> {
                                                listOf(
                                                    viewModel.oldPersonalData.name,
                                                    viewModel.oldPersonalData.phone
                                                )
                                            }

                                            DialogType.ADDRESS -> {
                                                listOf(
                                                    viewModel.oldAddressData.city,
                                                    viewModel.oldAddressData.street,
                                                    viewModel.oldAddressData.flat,
                                                    viewModel.oldAddressData.postCode
                                                )
                                            }
                                            DialogType.PASSWORD -> {
                                                // Для пароля - два пустых поля (email и пароль)
                                                listOf("", "")
                                            }
                                            DialogType.DELETE_ACCOUNT -> {
                                                // Для удаления аккаунта - два пустых поля (email и пароль)
                                                listOf("", "")
                                            }
                                            else -> {
                                                emptyList()
                                            }
                                        }
                                    )
                                }
                            )
                        }

                        is MenuItem.DropDownItem -> {
                            DropDownMenuItem(
                                item,
                                selectedOption = dropDownMenuSelectedOptions[item.menuType.ordinal],
                                onItemClick = { selectedIndex ->
                                    dropDownMenuSelectedOptions[item.menuType.ordinal] =
                                        selectedIndex

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
               viewModel.newUserSettingsData = UserSettingsData(
                   imageFormat = dropDownMenuSelectedOptions[0],
                   quality = dropDownMenuSelectedOptions[1],
                   size = dropDownMenuSelectedOptions[2]
               )
                viewModel.saveSettings()
                Log.d("MyLog", "saveSettings(): ")

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonColorBlue
            )
        ) {
            Text(text = "Save")
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
@Preview(showBackground = true)
fun ShowSettingsScreen() {
    SettingsScreen()
}