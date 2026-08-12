package com.kodex.guide.presentation.home

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.compose.collectAsLazyPagingItems
import com.kodex.guide.domain.model.Book
import com.kodex.guide.ui.dialods.FilterDialog
import com.kodex.guide.ui.dialods.MyDialog
import com.kodex.bookmarketcompose.R
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.domain.model.BookCategories
import com.kodex.guide.domain.model.UserRole
import com.kodex.guide.ui.theme.GreenSea
import com.kodex.guide.ui.theme.Orange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navData: NavRoutes.HomeDataObject,
    onBookEditClick: (Book) -> Unit,
    onBookClick: (Book) -> Unit,
    book: Book = Book(),
    onAdminClick: () -> Unit,
    onAnonymousClick: () -> Unit,
    onLoginClick: () -> Unit,
    onAddBookClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onRegistrationNeeded: () -> Unit,
    onEnter: () -> Unit,

    ) {
    // Подписываемся на роль пользователя
    val userRole by viewModel.userRole
    val showAuthDialog by viewModel.showAuthDialog
    val isLogoutDialog by viewModel.isLogoutDialog
    val headerUser by viewModel.headerUser
    val showPaymentSheet = viewModel.showPaymentSheet


    val context = LocalContext.current
    val categoryList = stringArrayResource(id = R.array.category_array)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val showDeleteDialog = remember { mutableStateOf(false) }
    val isAuthorState = remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    val books = viewModel.books.collectAsLazyPagingItems()
    val roomList = viewModel.postList.collectAsState(initial = emptyList())

    val state = rememberPullToRefreshState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val lifecycleOwner = LocalLifecycleOwner.current

    // ✅ Подписка на события авторизации
    LaunchedEffect(Unit) {
        viewModel.authEvents.collect { event ->
            when (event) {
                is AuthEvent.ShowLoginDialog -> {
                    viewModel.showAuthDialog.value = true
                    viewModel.isLogoutDialog.value = false
                }

                is AuthEvent.ShowLogoutDialog -> {
                    viewModel.showAuthDialog.value = true
                    viewModel.isLogoutDialog.value = true
                }

                is AuthEvent.NavigateToRegistration -> {
                    onRegistrationNeeded()
                }

                is AuthEvent.NavigateToSignIn -> {  // ✅ НОВОЕ
                    onLoginClick()                  // SignIn
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.isAdmin { isAdmin ->
            viewModel.isAdminState.value = isAdmin
        }
    }

    LaunchedEffect(Unit) {
        viewModel.isUserRegistered { isRegister ->
            viewModel.isRegisterState.value = isRegister
        }
    }

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
            Column(modifier = Modifier.fillMaxWidth(if (!isLandscape) 0.7f else 0.3f)) {
                if (!isLandscape) {
                    DrawerHeader(user = headerUser)   // ✅ передаём весь User?

                }
                DrawerBody(
                    onAdminClick = onAdminClick,
                    onAddBookClick = onAddBookClick,
                    onAdmin = { isAdmin ->
                        viewModel.isAdminState.value = isAdmin
                    },
                    onCategoryClick = { categoryIndex ->
                        if (categoryIndex == BookCategories.SAVED) {
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

                    onAnonymousClick = {
                        onAnonymousClick()
                        coroutineScope.launch { drawerState.close() }
                    },

                    onSettingsClick = {
                        onSettingsClick()
                        coroutineScope.launch { drawerState.close() }
                    },

                    onRegistrationNeeded = {
                        onRegistrationNeeded()
                        coroutineScope.launch { drawerState.close() }
                    },
                    onEnter = {
                        onEnter()
                        coroutineScope.launch { drawerState.close() }
                    },
                )
            }
        }
    ) {
        Scaffold(
            // FAB добавляется здесь
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onAddBookClick() },
                    containerColor = Orange,
                    contentColor = Color.White,

                    ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Добавить объявление"
                    )
                }
            },
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
                        onCategoryClick = {
                            viewModel.getAllBooksFromCategory(category = BookCategories.SAVED)
                            viewModel.selectedBottomItemState.intValue =
                                BottomMenuItem.Saved.titleId


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
                    // В начале загрузка из базы данных Room
                    if (books.itemCount == 0)
                        LazyColumn(
                            Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                        )
                        {
                            items(roomList.value) { book ->
                                BookListItemUi(
                                    heightValue = if (viewModel.showTabOneOrTo.value == true) 1 else 2,
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
                        } else {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(if (viewModel.showTabOneOrTo.value == true) 1 else 2),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(count = books.itemCount) { index ->
                                val book = books[index]
                                if (book != null) {
                                    BookListItemUi(
                                        heightValue = if (viewModel.showTabOneOrTo.value == true) 1 else 2,
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
            // ✅ Диалог «Ваш тариф»
            if (showAuthDialog) {
                val currentRole = userRole
                val nextRole = viewModel.getNextRole(currentRole)
                val nextRoleName =
                    if (nextRole != null) viewModel.getRoleDisplayName(nextRole) else null

                AlertDialog(
                    onDismissRequest = { viewModel.dismissAuthDialog() },
                    title = {
                        Text(
                            text = "Ваш тариф",
                            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                        )
                    },
                    text = {
                        Column {
                            Text(
                                text = "Текущий тариф: ${viewModel.getRoleDisplayName(currentRole)}",
                                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (nextRoleName != null) {
                                Text(
                                    text = "Доступно повышение до: $nextRoleName",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (nextRole == UserRole.BUSINESS || nextRole == UserRole.PREMIUM)
                                        "Для перехода требуется оплата"
                                    else
                                        "Для перехода требуется регистрация",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            } else {
                                Text(
                                    text = "У вас максимальный тариф",
                                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                        }
                    },
                    // ✅ Все кнопки теперь в одном Column — ничего не наезжает друг на друга
                    confirmButton = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)          // ✅ отступ снизу диалога
                        ) {
                            // ===== 1. Зелёная кнопка — следующий тариф по цепочке =====
                            if (nextRole != null && nextRoleName != null) {
                                Button(
                                    onClick = {
                                        viewModel.dismissAuthDialog()
                                        viewModel.requestUpgrade(currentRole) {
                                            onRegistrationNeeded()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                        containerColor = GreenSea
                                    )
                                ) {
                                    Text("Перейти на «$nextRoleName»")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // ===== 2. Малиновая кнопка — сразу на Премиум =====
                            if (nextRole != null && nextRole != UserRole.PREMIUM) {
                                Button(
                                    onClick = {
                                        viewModel.dismissAuthDialog()
                                        viewModel.requestPremiumUpgrade {
                                            onRegistrationNeeded()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF9C27B0)   // малиновый
                                    )
                                ) {
                                    Text("Перейти на «Премиум»")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // ✅ Отступ перед нижним рядом
                            Spacer(modifier = Modifier.height(4.dp))

                            // ===== 3. Нижний ряд: «Гость» СЛЕВА, затем «Отмена» =====
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // ✅ «Гость» — слева (только для авторизованных тарифов)
                                if (currentRole == UserRole.USER ||
                                    currentRole == UserRole.BUSINESS ||
                                    currentRole == UserRole.PREMIUM
                                ) {
                                    TextButton(
                                        onClick = {
                                            viewModel.dismissAuthDialog()
                                            viewModel.logout()   // выход → ANONYMOUS
                                        }
                                    ) {
                                        Text("Гость")
                                    }
                                    Spacer(modifier = Modifier.width(105.dp))   // ✅ небольшой отступ
                                }

                                TextButton(onClick = { viewModel.dismissAuthDialog() }) {
                                    Text("Отмена")
                                }
                            }
                        }
                    },
                    // ✅ dismissButton больше не нужен — всё внутри confirmButton
                    dismissButton = {}
                )
            }

            // Обёртка для отображения bottom sheet
            if (showPaymentSheet.value) {
                PaymentBottomSheet(
                    viewModel = viewModel,
                    onDismiss = { showPaymentSheet.value = false },
                    onPaymentSuccess = {
                        // Можно показать Toast или обновить UI
                        Toast.makeText(context, "Роль обновлена!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}


