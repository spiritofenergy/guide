package com.kodex.guide.ui.mainScreen

import android.R.id.content
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.bottomMenu.BottomMenu
import com.kodex.guide.ui.bottomMenu.BottomMenuItem
import com.kodex.guide.ui.castom.FilterDialog
import com.kodex.guide.ui.castom.MyDialog
import com.kodex.guide.ui.data.MainScreenDataObject
import com.kodex.guide.ui.drawerMenu.DrawerBody
import com.kodex.guide.ui.drawerMenu.DrawerHeader
import com.kodex.guide.ui.utils.Categories
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.mainScreen.MainTopAppBar
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    navData: MainScreenDataObject,
    onBookEditClick: (Book) -> Unit,
    onBookClick: (Book) -> Unit,
    onAdminClick: () -> Unit,
) {
    val context = LocalContext.current

    val drawerState = rememberDrawerState(DrawerValue.Open)
    val coroutineScope = rememberCoroutineScope()
    val showDeleteDialog = remember {mutableStateOf(false) }
    val isAuthorState = remember {mutableStateOf(false)}
    var showFilterDialog by remember {mutableStateOf(false)}

    val books = viewModel.books.collectAsLazyPagingItems()

    val isAdminState = remember {
        mutableStateOf(false)
    }
 /*   val drawerState = rememberDrawerState(DrawerValue.Open)
    val coroutineScope = rememberCoroutineScope()
    val booksListState = remember {
        mutableStateOf(emptyList<Book>())
    }*/
    LaunchedEffect(Unit) {
        viewModel.uiState.collect{uiState->
            if (uiState is MainScreenViewModel.MainUiState.Error){
                Toast.makeText(context, uiState.massage, Toast.LENGTH_SHORT).show()
            }
        }
    }
    LaunchedEffect(books.loadState.refresh) {
        if (books.loadState.refresh is LoadState.Error) {
            val errorMassage = (books.loadState.refresh as LoadState.Error).error.message
            Toast.makeText(context, errorMassage, Toast.LENGTH_SHORT).show()
        }
    }
        /* LaunchedEffect(Unit) {
             val db = Firebase.firestore
             getAllBooks(db) { books ->
                 booksListState.value = books
             }
         }
     */
        /*
        ModalNavigationDrawer(
            drawerState = drawerState,
            modifier = Modifier.fillMaxWidth(),
            drawerContent = {
                Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                    DrawerHeader(navData.email)
                    DrawerBody(
                        onAdmin = { isAdmin ->
                            isAdminState.value = isAdmin
                        },
                        onAdminClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            onAdminClick()
                            //  viewModel.getBooksFromCategory(Categories.FAVORITES)
                        },
                        onCategoryClick = { categoryIndex ->
                            if (categoryIndex == Categories.FANTASY) {
                                viewModel.selectedBottomItemState.intValue =
                                    BottomMenuItem.Faves.titleId
                            } else {
                                viewModel.selectedBottomItemState.intValue =
                                    BottomMenuItem.Home.titleId
                            }
                            viewModel.getBooksFromCategory(categoryIndex)
                            books.refresh()
                            viewModel.selectedBottomItemState.intValue =
                                BottomMenuItem.Faves.titleId
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    MainTopAppBar(viewModel.categoryState.intValue,
                        onSearch = { searchText ->
                            viewModel.searchBook(searchText)
                            books.refresh()
                        },
                        onFilter = {
                            showFilterDialog = true
                        })
                },
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomMenu(
                        viewModel.selectedBottomItemState.intValue,
                        onFavesClick = {
                            viewModel.selectedBottomItemState.intValue =
                                BottomMenuItem.Faves.titleId
                            viewModel.getBooksFromCategory(Categories.FANTASY)
                            books.refresh()
                        },
                        onHomeClick = {
                            // получаем список с иыентификатором и
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId
                            viewModel.getBooksFromCategory(Categories.ALL)
                            books.refresh()
                        }
                    )
                }
            ) { paddingValues ->

                if (books.itemCount == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.empty_list),
                            color = Color.LightGray
                        )
                    }
                }
                MyDialog(
                    showDialog = showDeleteDialog.value,
                    onDismiss = {
                        showDeleteDialog.value = false
                    },
                    title = "Внимание!",
                    massage = "Вы действительно хотите удалить это сообщение?",
                    onConfirm = {
                        showDeleteDialog.value = false
                        viewModel.deleteBook(books.itemSnapshotList.items)
                    }
                )
                *//*  if (books.loadState.refresh is LoadState.Loading) {
                  Box(
                      modifier = Modifier.fillMaxSize(),
                      contentAlignment = Alignment.Center
                  ) {
                      CircularProgressIndicator(
                          modifier = Modifier.size(30.dp)
                      )
                  }
              }*//*
                PullToRefreshBox(
                    isRefreshing = books.loadState.refresh is LoadState.Loading,
                    onRefresh = {
                        books.refresh()
                    },
                    modifier = Modifier.padding(paddingValues)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(count = books.itemCount) { index ->
                            val book = books[index]
                            if (book != null) {
                                BookListItemUi(
                                    titleIndex = viewModel.categoryState.intValue,
                                    isAdminState.value,
                                    book,
                                    onBookClick = { bk ->
                                        onBookClick(bk)
                                    },
                                    onEditClick = {
                                        onBookEditClick(it)
                                    },
                                    onDeleteClick = { bookToDelete ->
                                        showDeleteDialog.value = true
                                        viewModel.bookToDelete = bookToDelete
                                    },
                                    onFavClick = {
                                        viewModel.onFavesClick(
                                            book,
                                            viewModel.selectedBottomItemState.intValue,
                                            books.itemSnapshotList.items
                                        )

                                    }
                                )
                            }
                        }
                    }
                }

                FilterDialog(
                    showDialog = showFilterDialog,
                    onConfirm = {
                        showFilterDialog = false
                        //books.refresh()
                    },
                    onDismiss = {
                        showFilterDialog = false
                    }
                )
            }
        }
    }*/
        ModalNavigationDrawer(
            drawerState = drawerState,
            modifier = Modifier.fillMaxWidth(),
            drawerContent = {
                Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                    DrawerHeader(navData.email)
                    DrawerBody(
                        onAdmin = { isAdmin ->
                            isAdminState.value = isAdmin
                        },
                        onAdminClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            onAdminClick()
                            //  viewModel.getBooksFromCategory(Categories.FAVORITES)
                        },
                        onCategoryClick = { categoryIndex ->
                            if (categoryIndex == Categories.FANTASY) {
                                viewModel.selectedBottomItemState.intValue =
                                    BottomMenuItem.Faves.titleId
                            } else {
                                viewModel.selectedBottomItemState.intValue =
                                    BottomMenuItem.Home.titleId
                            }
                            viewModel.getBooksFromCategory(categoryIndex)
                            books.refresh()
                            viewModel.selectedBottomItemState.intValue =
                                BottomMenuItem.Faves.titleId
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    MainTopAppBar(viewModel.categoryState.intValue,
                        onSearch = { searchText ->
                            viewModel.searchBook(searchText)
                            books.refresh()
                        },
                        onFilter = {
                            showFilterDialog = true
                        })
                },
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomMenu(
                        viewModel.selectedBottomItemState.intValue,
                        onFavesClick = {
                            viewModel.selectedBottomItemState.intValue =
                                BottomMenuItem.Faves.titleId
                            viewModel.getBooksFromCategory(Categories.FANTASY)
                            books.refresh()
                        },
                        onHomeClick = {
                            // получаем список с иыентификатором и
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId
                            viewModel.getBooksFromCategory(Categories.ALL)
                            books.refresh()
                        }
                    )
                }
            ) { paddingValues ->

                if (books.itemCount == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.empty_list),
                            color = Color.LightGray
                        )
                    }
                }
                MyDialog(
                    showDialog = showDeleteDialog.value,
                    onDismiss = {
                        showDeleteDialog.value = false
                    },
                    title = "Внимание!",
                    massage = "Вы действительно хотите удалить это сообщение?",
                    onConfirm = {
                        showDeleteDialog.value = false
                        viewModel.deleteBook(books.itemSnapshotList.items)
                    }
                )
                /*  if (books.loadState.refresh is LoadState.Loading) {
                          Box(
                              modifier = Modifier.fillMaxSize(),
                              contentAlignment = Alignment.Center
                          ) {
                              CircularProgressIndicator(
                                  modifier = Modifier.size(30.dp)
                              )
                          }
                      }*/
                PullToRefreshBox(
                    isRefreshing = books.loadState.refresh is LoadState.Loading,
                    onRefresh = {
                        books.refresh()
                    },
                    modifier = Modifier.padding(paddingValues)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(count = books.itemCount) { index ->
                            val book = books[index]
                            if (book != null) {
                                BookListItemUi(
                                    titleIndex = viewModel.categoryState.intValue,
                                    isAdminState.value,
                                    book,
                                    onBookClick = { bk ->
                                        onBookClick(bk)
                                    },
                                    onEditClick = {
                                        onBookEditClick(it)
                                    },
                                    onDeleteClick = { bookToDelete ->
                                        showDeleteDialog.value = true
                                        viewModel.bookToDelete = bookToDelete
                                    },
                                    onFavClick = {
                                        viewModel.onFavesClick(
                                            book,
                                            viewModel.selectedBottomItemState.intValue,
                                            books.itemSnapshotList.items
                                        )

                                    }
                                )
                            }
                        }
                    }
                }

                FilterDialog(
                    showDialog = showFilterDialog,
                    onConfirm = {
                        showFilterDialog = false
                        //books.refresh()
                    },
                    onDismiss = {
                        showFilterDialog = false
                    }
                )
            }
        }
    }



/*
 private fun getAllBooks (
     db: FirebaseFirestore,
     onBooks: (List<Book>)-> Unit
 ){
     db.collection("guide_posts")
    // db.collection("imajes")
    // db.collection("users")
         .get()
         .addOnSuccessListener { task ->
            // onBooks(task.toObjects(Book::class.java))
             val bookList = task.toObjects(Book::class.java)
             onBooks(bookList)
         }
         .addOnFailureListener{

     }*/

