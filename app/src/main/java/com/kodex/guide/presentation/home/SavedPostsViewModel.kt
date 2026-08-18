package com.kodex.guide.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.usecase.ObserveSavedKeysUseCase
import com.kodex.guide.domain.usecase.ObserveSavedPostsUseCase
import com.kodex.guide.domain.usecase.ToggleSavedPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedPostsViewModel @Inject constructor(
    observeSavedPostsUseCase: ObserveSavedPostsUseCase,
    observeSavedKeysUseCase: ObserveSavedKeysUseCase,
    private val toggleSavedPostUseCase: ToggleSavedPostUseCase
) : ViewModel() {

    val savedPosts: StateFlow<List<Book>> = observeSavedPostsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val savedKeys: StateFlow<Set<String>> = observeSavedKeysUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    private val _events = MutableSharedFlow<SavedUiEvent>()
    val events: SharedFlow<SavedUiEvent> = _events.asSharedFlow()

    fun toggle(book: Book) {
        viewModelScope.launch {
            runCatching {
                toggleSavedPostUseCase(book)
            }
                .onSuccess { isNowSaved ->
                    val message = if (isNowSaved) {
                        "Добавлено в сохраненные"
                    } else {
                        "Удалено из сохраненных"
                    }

                    _events.emit(SavedUiEvent.ShowToast(message))
                }
                .onFailure { error ->
                    _events.emit(
                        SavedUiEvent.ShowToast(
                            error.message ?: "Не удалось изменить сохранение"
                        )
                    )
                }
        }
    }
}