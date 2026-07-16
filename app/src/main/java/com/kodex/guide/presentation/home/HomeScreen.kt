package com.kodex.guide.presentation.home

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.kodex.guide.domain.model.Book
import com.kodex.guide.ui.dialods.FilterDialog
import com.kodex.guide.ui.dialods.MyDialog
import com.kodex.bookmarketcompose.R
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.presentation.room.RoomFavoriteViewModel
import com.kodex.guide.domain.model.BookCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModelT: RoomFavoriteViewModel = hiltViewModel(),
   // onTrackClick: (Book) -> Unit = {},


    viewModel: HomeViewModel = hiltViewModel(),
    navData: NavRoutes.HomeDataObject,
    onBookEditClick: (Book) -> Unit,
    onBookClick: (Book) -> Unit,
    book: Book = Book(),
    onAdminClick: () -> Unit,
    onLoginClick: () -> Unit,
    onAddBookClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSavedRoomClick: () -> Unit,
) {

    val book = viewModel.postList.collectAsState(initial = emptyList())
    val showDialog = remember { mutableStateOf(false) }


    // val booksFirebaseRemoteDataSource: BooksFirebaseRemoteDataSource
    val context = LocalContext.current
    val categoryList = stringArrayResource(id = R.array.category_array)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val showDeleteDialog = remember { mutableStateOf(false) }
    val isAuthorState = remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    val books = viewModel.books.collectAsLazyPagingItems()
    val trackList = viewModel.postList.collectAsState(initial = emptyList())

    val booksRoomList = MutableStateFlow<List<Book>>(emptyList())

    val state = rememberPullToRefreshState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                Log.d("MyLog", "refreshBooks")
                viewModel.getSettings()
                Log.d("MyLog", "getSettings MenuScreen")

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
            if (uiState is HomeViewModel.MainUiState.Error) {
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    LaunchedEffect(books.loadState.refresh) {
        if (books.loadState.refresh is LoadState.Error) {
            val errorMessage = (books.loadState.refresh as LoadState.Error).error.message
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getAllSavedBooks().collect { booksRoom ->
            booksRoomList.value = booksRoom
            Log.d("SavedBooks", "Найдено ${booksRoom.size} сохраненных книг")
            booksRoom.forEach { book ->
                Log.d("SavedBooks", "Книга: ${book.title}, Избранная: ${book.isFavorite}")
            }
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
                        if (categoryIndex == BookCategories.FAVORITES) {
                            viewModel.selectedBottomItemState.intValue =
                                BottomMenuItem.Saved.titleId
                            Log.d("MyLog", "onCategoryClick FAVORITES")
                            //savedInstanceState.value = BottomMenuItem.Favorite.titleId
                            coroutineScope.launch { drawerState.close() }
                        } else {
                            viewModel.getAllBooksFromCategory(categoryIndex)
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
                    onSavedRoomClick = {
                        onSavedRoomClick()
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
                        viewModel.categoryState.value,
                        onSearch = { searchText ->
                            viewModel.searchBook(searchText)
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
                        onSavedRoomClick = {
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Saved.titleId
                          // viewModel.getAllBooksFromCategory(BookCategories.FAVORITES)
                          //  viewModel.onFavesClick(Book())
                          //  books.refresh()
                            onSavedRoomClick()
                        },
                        onHomeClick = {
                            // получаем список с иыентификатором и
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId
                            viewModel.getAllBooksFromCategory(category = BookCategories.ALL)
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
/*
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
                }*/
                MyDialog(
                    showDialog = showDeleteDialog.value,
                    onDismiss = {
                        showDeleteDialog.value = false
                    },

                    title = stringResource(id = R.string.attention),
                    message = stringResource(id = R.string.want_to_delete_this_message),
                    onConfirm = {
                        showDeleteDialog.value = false
                        viewModel.deleteBook()
                    },
                )

                PullToRefreshBox(
                    isRefreshing = books.loadState.refresh is LoadState.Loading,
                    onRefresh = {
                      //  refreshBooks(books, viewModel)
                    },
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
                    /* if (books.loadState.refresh is LoadState.Loading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }*/
                    if (books.itemCount == 0)
                        LazyColumn(
                            Modifier
                                .fillMaxSize()
                                .padding( 2.dp))
                        {
                            items(book.value) { book ->
                                    BookListItemUi(
                                        titleIndex = viewModel.categoryState.value.id,
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
                                        onSavedRoomClick = {
                                           // viewModel.onFavesClick(
                                            //    book, )
                                            viewModel.insertPost(book)

                                                if (!book.isFavorite) {
                                            Toast.makeText(
                                                context,
                                                R.string.added_to_memory,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                R.string.deleted_from_memory,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        }
                                    )

                                    Spacer(Modifier.padding(5.dp))

                            }
                        }else {
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(if (viewModel.showTabOneOrTo.value == true) 1 else 2),
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                items(count = books.itemCount) { index ->
                                    val book = books[index]
                                    if (book != null) {
                                        BookListItemUi(
                                            titleIndex = viewModel.categoryState.value.id,
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
                                            onSavedRoomClick = {
                                              //  viewModel.onFavesClick(book)
                                              //  Log.d("MyLog", "book.key ${book.key}")
                                               // Save in Room
                                                viewModel.insertPost(book)
                                                if (!book.isFavorite) {
                                                    Toast.makeText(
                                                        context,
                                                        R.string.added_to_memory,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        R.string.deleted_from_memory,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                FilterDialog(
                    showDialog = showFilterDialog,
                    onConfirm = {
                        showFilterDialog = false
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

