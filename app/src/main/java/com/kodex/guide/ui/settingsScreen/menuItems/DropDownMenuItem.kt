package com.kodex.guide.ui.settingsScreen.menuItems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kodex.guide.ui.settingsScreen.MenuItem

@Composable
fun DropDownMenuItem(
    item: MenuItem.DropDownItem,
    selectedOption: Int,
    onItemClick: (Int)-> Unit,

    ) {
    val content  = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val option = remember {content.resources.getStringArray(item.arrayId) }

    Column(
        modifier = Modifier.fillMaxWidth().clickable{
            expanded = true
        }
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.fillMaxWidth().padding(start = 16.dp)){
            Text(
                text = item.title + ": ${option[selectedOption]}",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            DropdownMenu(expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }) {
                option.forEachIndexed { index, title ->
                    DropdownMenuItem(text = {
                        Text(text = title)
                    }, onClick = {
                        expanded = false
                        onItemClick(index)
                    })
                }
            }

        }
        }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black)
                .padding(10.dp)
        )
    }



/*

@Composable
@Preview(showBackground = true)
fun ShowMenuUiItem() {
   // MenuUiItem()
}*/
