package com.kodex.guide.ui.mainScreen

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kodex.bookmarketcompose.R

import com.kodex.guide.ui.theme.DarkBlue
import com.kodex.guide.ui.theme.Purple80
import com.kodex.guide.ui.theme.PurpleGrey80
import com.kodex.guide.ui.utils.Categories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    titleIndex: Int,
    onSearch: (String) -> Unit,
    onFilter: () -> Unit
) {
    var targetState by remember { mutableStateOf(false) }
    var queryText by remember { mutableStateOf("") }
    var expandedState by remember { mutableStateOf(false) }


    Crossfade(targetState) { target ->
        if (target) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                inputField = {
                    SearchBarDefaults.InputField(
                        colors = TextFieldDefaults.colors(
                            focusedTrailingIconColor = DarkBlue,
                            unfocusedTrailingIconColor = DarkBlue
                        ),
                        query = queryText,
                        placeholder = {
                            Text(text = "Search...")
                        },
                        onQueryChange = { text ->
                            queryText = text
                           // Log.d("MyLog", "Query onQueryChange text: $text")
                        },
                        onSearch = {text ->
                            onSearch(text)
                            Log.d("MyLog", "Query onSearch text: $text")


                        },
                        expanded = false,
                        onExpandedChange = { exp ->
                          //  expandedState = exp
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    expandedState = false
                                    targetState = false
                                    queryText = ""
                                    onSearch("")
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
                expanded = expandedState,
                onExpandedChange = { expo ->
                    expandedState = expo
                },
                content = {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(25) {
                            Text(
                                text = "Сообщение № $it",
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            )
        } else {
            TopAppBar(
                title = {
                    Text( text = when (titleIndex){
                        Categories.FAVORITES -> stringResource(id = R.string.favorite)
                        Categories.ALL -> stringResource(id = R.string.all)
                        else -> stringArrayResource(id = R.array.category_arrays)[titleIndex]
                    } )
                },

                actions = {
                    IconButton(onClick = {
                        targetState = true
                    }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }

                    IconButton(onClick = {
                        onFilter()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = "Filter"
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurpleGrey80,
                    titleContentColor = DarkBlue,
                    actionIconContentColor = DarkBlue
                )
            )
        }
    }

}