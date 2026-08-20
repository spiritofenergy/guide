package com.kodex.guide.presentation.myPosts

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.data.images.BitmapEncoder
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.tarif.AuthStateProvider
import com.kodex.guide.domain.usecase.DeleteMyPostUseCase
import com.kodex.guide.domain.usecase.GetMyPostUseCase
import com.kodex.guide.domain.usecase.ObserveMyPostsUseCase
import com.kodex.guide.domain.usecase.SaveDraftUseCase
import com.kodex.guide.domain.usecase.UploadMyPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MyPostsEvent {
    data class Toast(val message: String) : MyPostsEvent
}

@HiltViewModel
class MyPostsViewModel @Inject constructor(
    private val observeMyPosts: ObserveMyPostsUseCase,
    private val getMyPost: GetMyPostUseCase,
    private val saveDraft: SaveDraftUseCase,
    private val uploadMyPost: UploadMyPostUseCase,
    private val deleteMyPost: DeleteMyPostUseCase,
    private val bitmapEncoder: BitmapEncoder,
    private val authStateProvider: AuthStateProvider
) : ViewModel() {

    private val _myPosts = MutableStateFlow<List<Book>>(emptyList())
    val myPosts: StateFlow<List<Book>> = _myPosts.asStateFlow()

    private val _editPost = MutableStateFlow<Book?>(null)
    val editPost: StateFlow<Book?> = _editPost.asStateFlow()

    private val _events = MutableSharedFlow<MyPostsEvent>()
    val events: SharedFlow<MyPostsEvent> = _events.asSharedFlow()

    init {
        val uid = authStateProvider.currentUser()?.uid
        if (uid != null) {
            observeMyPosts(uid)
                .onEach { _myPosts.value = it }
                .launchIn(viewModelScope)
        }
    }

    fun loadForEdit(key: String) {
        if (key.isEmpty()) return
        viewModelScope.launch {
            getMyPost(key).onSuccess { _editPost.value = it }
        }
    }

    // ✅ сохранить черновик (и сразу опубликовать, если publish = true)
    fun save(book: Book, uri: Uri? = null, publish: Boolean = false) = viewModelScope.launch {
        val withImage = book.copy(
            imageUrl = uri?.let { bitmapEncoder.imageToBase64(it) } ?: book.imageUrl
        )
        saveDraft(withImage)
            .onSuccess { saved ->
                _events.emit(MyPostsEvent.Toast("Сохранено на устройстве"))
                if (publish) upload(saved)
            }
            .onFailure { _events.emit(MyPostsEvent.Toast(it.message ?: "Ошибка сохранения")) }
    }

    fun upload(book: Book) = viewModelScope.launch {
        uploadMyPost(book)
            .onSuccess { _events.emit(MyPostsEvent.Toast("Опубликовано")) }
            .onFailure { _events.emit(MyPostsEvent.Toast("Ошибка публикации: ${it.message}")) }
    }

    fun delete(book: Book) = viewModelScope.launch {
        deleteMyPost(book)
            .onSuccess { _events.emit(MyPostsEvent.Toast("Удалено")) }
            .onFailure { _events.emit(MyPostsEvent.Toast(it.message ?: "Ошибка удаления")) }
    }
}