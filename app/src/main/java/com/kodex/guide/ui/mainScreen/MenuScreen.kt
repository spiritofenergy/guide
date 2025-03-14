package com.kodex.guide.ui.mainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.addscreen.data.Favorite
import com.kodex.guide.ui.bottomMenu.BottomMenu
import com.kodex.guide.ui.bottomMenu.BottomMenuItem
import com.kodex.guide.ui.data.MainScreenDataObject
import com.kodex.guide.ui.drawerMenu.DrawerBody
import com.kodex.guide.ui.drawerMenu.DrawerHeader
import kotlinx.coroutines.launch


@Composable
fun MenuScreen(
    viewModel: MsViewModel = hiltViewModel(),
    navData: MainScreenDataObject,
    onBookEdinClick: (Book)-> Unit,
    onBookClick: (Book) -> Unit,
    onAdminClick: () -> Unit,
) {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val coroutineScope = rememberCoroutineScope()

        val selectedBottomItemState = remember {
        mutableStateOf(BottomMenuItem.Home.title)
    }
    val isAdminState = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.getAllBooks()
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
             Column (Modifier.fillMaxWidth(0.7f)){
                DrawerHeader(navData.email)
                 DrawerBody (
                     onAdmin = { isAdmin ->
                        isAdminState.value = isAdmin
                     },
                     onFavesClick = {
                         selectedBottomItemState.value = BottomMenuItem.Faves.title
                         viewModel.getAllFavesBook ()
                         coroutineScope.launch{
                             drawerState.close()
                         }
                     },
                     onAdminClick = {
                         coroutineScope.launch{
                             drawerState.close()
                         }
                         onAdminClick()
                     },

                        onCategoryClick = { category ->
                            viewModel.getBooksFromCategory(category)
                            coroutineScope.launch{
                                drawerState.close()
                            }
                        }
                 )
             }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomMenu(
                    selectedBottomItemState.value,
                    onFavesClick = {
                        selectedBottomItemState.value = BottomMenuItem.Faves.title
                        viewModel.getAllFavesBook ()
                    },
                    onHomeClick = {
                        selectedBottomItemState.value = BottomMenuItem.Home.title
                        viewModel.getAllBooks()
                    }
                )
            }
        ) {paddingValues ->
            LazyVerticalGrid(columns = GridCells.Fixed(1),
                modifier = Modifier.fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(viewModel.bookListState.value){ book ->
                    BookListItemUi(
                        isAdminState.value,
                        book,
                        onBookClick = {
                            onBookClick(book)
                        },
                        onEditClick = {
                            onBookEdinClick(it)
                        },
                        onFavClick = {
                            viewModel.onFavesClick(book, selectedBottomItemState.value)
                        }
                    )
                }
            }
        }
    }
}







