package com.kodex.guide.ui.mainScreen

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.bottomMenu.BottomMenu
import com.kodex.guide.ui.bottomMenu.BottomMenuItem
import com.kodex.guide.ui.dialods.FilterDialog
import com.kodex.guide.ui.dialods.MyDialog
import com.kodex.guide.ui.drawerMenu.DrawerBody
import com.kodex.guide.ui.drawerMenu.DrawerHeader
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.data.NavRoutes
import com.kodex.guide.ui.theme.ButtonColor
import com.kodex.guide.ui.theme.ButtonColorBlue
import com.kodex.guide.ui.utils.Categories
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    navData: NavRoutes.MainScreenDataObject,
    onBookEditClick: (Book) -> Unit,
    onBookClick: (Book) -> Unit,
    onAdminClick: () -> Unit,
    onLoginClick: () -> Unit,
    onAddBookClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val context = LocalContext.current
    val categoryList = stringArrayResource(id = R.array.category_array)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val showDeleteDialog = remember { mutableStateOf(false) }
    val isAuthorState = remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    val books = viewModel.books.collectAsLazyPagingItems()
    val state = rememberPullToRefreshState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshBooks(books, viewModel)
               Log.d("MyLog", "refreshBooks" )
                viewModel.getSettings()
                Log.d("MyLog", "getSettings MenuScreen" )

            }
        }
        lifecycleOwner.lifecycle.removeObserver(observer)
          onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.isAdmin { isAdmin ->
            viewModel.isAdminState.value = isAdmin
        }
    }
    /*
        LaunchedEffect(Unit) {
            viewModel.isUserRegistered { isRegister ->
                viewModel.isRegisterState.value = isRegister
            }
        }*/

    LaunchedEffect(Unit) {
        viewModel.uiState.collect { uiState ->
            if (uiState is MainScreenViewModel.MainUiState.Error) {
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
    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
            Column(modifier = Modifier.fillMaxWidth(if (!isLandscape) 0.7f else 0.3f)) {
                if (!isLandscape) {
                    DrawerHeader(navData.email)
                }
                DrawerBody(
                    onAdminClick = onAdminClick,
                    onAddBookClick = onAddBookClick,
                    onAdmin = { isAdmin ->
                        viewModel.isAdminState.value = isAdmin
                    },
                    onCategoryClick = { categoryIndex ->
                        if (categoryIndex == Categories.FAVORITES) {
                            viewModel.selectedBottomItemState.intValue =
                                BottomMenuItem.Faves.titleId
                            Log.d("MyLog", "onCategoryClick FAVORITES")
                            //savedInstanceState.value = BottomMenuItem.Favorite.titleId
                            coroutineScope.launch { drawerState.close() }
                        } else {
                            viewModel.getAllBooksFromCategory(categoryIndex)
                            refreshBooks(books, viewModel)
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId
                            Log.d("MyLog", "categoryIndex: $categoryIndex")
                            coroutineScope.launch { drawerState.close() }
                        }
                    },

                    onLoginClick = {
                        onLoginClick()
                        coroutineScope.launch { drawerState.close() }
                    },

                    onSettingsClick = {
                        onSettingsClick()
                        coroutineScope.launch { drawerState.close() }
                    },

                    )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (!isLandscape)
                    MainTopBar(
                        viewModel.categoryState.intValue,
                        onSearch = { searchText ->
                            viewModel.searchBook(searchText)
                            refreshBooks(books, viewModel)
                        },
                        onFilter = {
                            showFilterDialog = true
                        },
                        onTab = {
                            viewModel.showTabOneOrTo.value = !viewModel.showTabOneOrTo.value
                        },
                        onMenu = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
            },
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (!isLandscape)
                    BottomMenu(
                        viewModel.selectedBottomItemState.intValue,
                        onFavesClick = {
                            viewModel.selectedBottomItemState.intValue =
                                BottomMenuItem.Faves.titleId
                            viewModel.onFavesClick(
                                Book(),
                                BottomMenuItem.Faves.titleId,
                                books.itemSnapshotList.items
                            )
                            books.refresh()
                        },
                        onHomeClick = {
                            // получаем список с иыентификатором и
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId
                            viewModel.getAllBooksFromCategory(categoryIndex = Categories.ALL)
                            refreshBooks(books, viewModel)
                        },
                        onSettingsClick = {
                            onSettingsClick()
                            viewModel.selectedBottomItemState.intValue =
                                BottomMenuItem.Settings.titleId

                        }
                    )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                if (books.itemCount == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                   ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(70.dp),
                            color = ButtonColorBlue
                        )
//                        Text(
//                            text = stringResource(id = R.string.empty_list),
//                            color = Color.LightGray
//                        )
                    }
                }
                MyDialog(
                    showDialog = showDeleteDialog.value,
                    onDismiss = {
                        showDeleteDialog.value = false
                    },
                    onConfirm = {
                        showDeleteDialog.value = false
                        viewModel.deleteBook(books.itemSnapshotList.items)
                    },
                    title = stringResource(id = R.string.attention),
                    message = stringResource(id = R.string.want_to_delete_this_message),
                )

                PullToRefreshBox(
                    isRefreshing = books.loadState.refresh is LoadState.Loading,
                    onRefresh = {
                        refreshBooks(books, viewModel)                    },
                    state = state,
                    modifier = Modifier.padding(),
                    indicator = {
                        Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = books.loadState.refresh is LoadState.Loading,
                            containerColor = Color.LightGray,
                            color = Color.White,
                            state = state

                        )
                    }
                ) {
                    if (books.loadState.refresh is LoadState.Loading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(if (viewModel.showTabOneOrTo.value == true) 1 else 2),
                        modifier = Modifier
                            .fillMaxSize()

                    ) {
                        items(count = books.itemCount) { index ->
                            val book = books[index]
                            if (book != null) {
                                BookListItemUi(
                                    titleIndex = viewModel.categoryState.intValue,
                                    viewModel.isAdminState.value,
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
                                    onFavesClick = {
                                        viewModel.onFavesClick(
                                            book, viewModel.selectedBottomItemState.intValue,
                                            books.itemSnapshotList.items
                                        )
                                        if (!book.isFavorite) {
                                            Toast.makeText(
                                                context,
                                                R.string.added_to_favorites,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                R.string.deleted_from_favorites,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
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
                        refreshBooks(books, viewModel)
                    },
                    onDismiss = {
                        showFilterDialog = false
                    }
                )
            }
        }
    }
}

private fun refreshBooks(books: LazyPagingItems<Book>, viewModel: MainScreenViewModel){
    viewModel.clearTempBookList()
    books.refresh()
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

