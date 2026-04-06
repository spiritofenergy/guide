package com.kodex.guide.ui.settingsScreen


data class AccountDialogData(
    val title: String = "",
    val fieldLabel: List<String> = emptyList(),
    val fieldValues: List<String> = emptyList(),
    val showDialog: Boolean = false,
    val dialogType: DialogType? = DialogType.PERSONAL_DATA
    )
enum class DialogType{
    PERSONAL_DATA,
    ADDRESS,
    PASSWORD,
    DELETE_ACCOUNT,
}