package com.kodex.guide.presentation.myPosts

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.model.BookCategories
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostEditorScreen(
    viewModel: MyPostsViewModel = hiltViewModel(),
    bookKey: String,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    val editPost by viewModel.editPost.collectAsState()

    LaunchedEffect(bookKey) {
        viewModel.loadForEdit(bookKey)
    }
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            if (event is MyPostsEvent.Toast && event.message in listOf(
                    "Сохранено на устройстве",
                    "Опубликовано"
                )
            ) onSaved()
        }
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(BookCategories.ALL) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var existingImageUrl by remember { mutableStateOf("") }

    // Подставляем данные при редактировании
    LaunchedEffect(editPost) {
        editPost?.let { b ->
            title = b.title
            description = b.description
            price = b.price.toString()
            telephone = b.telephone
            village = b.village
            category = b.categoryIndex
            existingImageUrl = b.imageUrl
        }
    }

    val pickImage = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    fun buildBook(): Book = (editPost ?: Book()).copy(
        title = title,
        description = description,
        price = price.toIntOrNull() ?: 0,
        telephone = telephone,
        village = village,
        categoryIndex = category
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (bookKey.isEmpty()) "Новое объявление" else "Редактирование")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- Фото ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                when {
                    imageUri != null -> AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    existingImageUrl.isNotEmpty() -> AsyncImage(
                        model = existingImageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    else -> Text("Фото не выбрано", color = Color.Gray)
                }
            }
            OutlinedButton(
                onClick = { pickImage.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Выбрать фото")
            }

            // --- Поля ---
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Заголовок") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 5
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.filter { c -> c.isDigit() } },
                    label = { Text("Цена") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = telephone,
                    onValueChange = { telephone = it },
                    label = { Text("Телефон") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
            OutlinedTextField(
                value = village,
                onValueChange = { village = it },
                label = { Text("Населенный пункт") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // --- Кнопки ---
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { viewModel.save(buildBook(), imageUri, publish = false) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить черновик")
            }
            Button(
                onClick = { viewModel.save(buildBook(), imageUri, publish = true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить и опубликовать")
            }
        }
    }
}