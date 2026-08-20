package com.kodex.guide.presentation.myPosts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kodex.guide.domain.model.Book
import com.kodex.guide.ui.theme.Orange
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostsScreen(
    viewModel: MyPostsViewModel = hiltViewModel(),
    onEditClick: (Book) -> Unit,
    onAddClick: () -> Unit,
    onBack: () -> Unit
) {
    val posts by viewModel.myPosts.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is MyPostsEvent.Toast -> {/* можно показать Snackbar */}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои объявления") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        // FAB добавляется здесь
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClick() },
                containerColor = Orange.copy(alpha = 0.6F),
                contentColor = Color.White,

                ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить объявление"
                )
            }
        },
    ) { padding ->
        if (posts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "У вас пока нет объявлений",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = onAddClick) {
                        Text("Создать первое")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(posts, key = { it.key }) { book ->
                    MyPostCard(
                        book = book,
                        onEdit = { onEditClick(book) },
                        onUpload = { viewModel.upload(book) },
                        onDelete = { viewModel.delete(book) }
                    )
                }
            }
        }
    }
}

@Composable
fun MyPostCard(
    book: Book,
    onEdit: () -> Unit,
    onUpload: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = book.title.ifEmpty { "Без названия" },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(isUploaded = book.isUploaded)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = book.description.take(100) + if (book.description.length > 100) "..." else "",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Ред.")
                }
                if (!book.isUploaded) {
                    Button(
                        onClick = onUpload,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Publish, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Опубл.")
                    }
                } else {
                    Spacer(Modifier.weight(1f))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, tint = Color.Red, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(isUploaded: Boolean) {
    val bg = if (isUploaded) Color(0xFF10B981).copy(alpha = 0.15f)
    else Color(0xFFF59E0B).copy(alpha = 0.15f)
    val text = if (isUploaded) "Опубликован" else "Черновик"
    val color = if (isUploaded) Color(0xFF10B981) else Color(0xFFF59E0B)

    Surface(shape = MaterialTheme.shapes.small, color = bg) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = color,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}