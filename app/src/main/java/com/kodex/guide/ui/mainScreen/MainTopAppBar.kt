package com.kodex.guide.ui.mainScreen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    var targetState by remember { mutableStateOf(false) }
    var queryText by remember { mutableStateOf("") }
    var expandedState by remember { mutableStateOf(false) }

    Crossfade(targetState) { target ->
        if (target) {
            SearchBar(
                modifier = Modifier.fillMaxSize(),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = queryText,
                        onQueryChange = { text ->
                            queryText = text
                        },
                        onSearch = {

                        },
                        expanded = false,
                        onExpandedChange = { exp ->
                            expandedState = exp
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    expandedState = false
                                    targetState = false
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close"
                                )
                            }
                        }
                    )
                },
                expanded = false,
                onExpandedChange = { exp ->
                    expandedState = exp
                },
                content = {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(5) {
                            Text(
                                text = "Item$it",
                                modifier = Modifier.padding(10.dp)
                            )
                        }


                    }
                }
            )
        } else {
            TopAppBar(
                title = {
                    Text( text = "Fantasy"   )
                },
                actions = {
                    IconButton(onClick = {
                        targetState = true
                    }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = ""
                        )
                    }

                }
            )
        }
    }
}