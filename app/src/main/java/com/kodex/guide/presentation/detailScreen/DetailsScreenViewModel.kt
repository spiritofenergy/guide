package com.kodex.guide.presentation.detailScreen

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.data.repository.BooksRepo_Impl
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.presentation.detailScreen.states.DetailsUiState
import com.kodex.guide.presentation.events.DetailUiEvents
import com.kodex.guide.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging,
    val booksRepo: BooksRepo_Impl
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

  /*    val ratingState = mutableStateOf("0")
    // val commentState = mutableStateOf(emptyList<RatingData>())
     val ratingDataState = mutableStateOf<RatingData?>(RatingData())
*/
  private fun insertRating(ratingData: RatingData, bookId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = booksRepo.submitUserRating(ratingData, bookId)
            result.fold(
                onSuccess = {
                    _uiState.value = uiState.value.copy(
                        showRateDialog = false
                    )
                },
                onFailure = { error ->

                }
            )
        }
    }

  private fun getBookComments(bookId: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = booksRepo.getBookComments(bookId)
        result.fold(
            onSuccess = { commentsList ->
                _uiState.value = uiState.value.copy(
                    comments = commentsList
                )
            },
            onFailure = { error ->

            }
        )
    }

    private fun getUserRating(bookId: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = booksRepo.getUserRating(bookId)
        result.fold(
            onSuccess = { rData ->
                _uiState.value = uiState.value.copy(
                    showRateDialog = true,
                    ratingData =  rData?: RatingData()
                )
            },
            onFailure = { error ->

            }
        )
    }

    fun onEvent(event: DetailUiEvents.DetailUiEvent) {
        when (event) {
            is DetailUiEvents.DetailUiEvent.CommentDialogEvent -> {
                _uiState.value = uiState.value.copy(
                    showCommentDialog = event.show,
                    ratingDataToShow = event.ratingData ?: RatingData()
                )
            }
            is DetailUiEvents.DetailUiEvent.ShowUserRatingDialogEvent -> {
                getUserRating(event.bookId)
            }
            is DetailUiEvents.DetailUiEvent.HideUserRatingDialog -> {
                _uiState.value = uiState.value.copy(
                    showRateDialog =true)
            }
            is DetailUiEvents.DetailUiEvent.InsertRatingDialogEvent -> {
                insertRating(event.ratingData, event.bookId)
            }
            is DetailUiEvents.DetailUiEvent.GetCommentsEvent -> {
                getBookComments(event.bookId)
            }
        }
    }

    // Выносим логику шаринга в отдельную функцию
    suspend fun sharePlace(
        context: Context,
        place: NavRoutes.ParallaxNavObject,
        coroutineScope: CoroutineScope
    ) {
        withContext(Dispatchers.IO) {
            val shareText = buildString {
                appendLine(place.title)
                appendLine()
                appendLine("⭐️ Рейтинг: ${place.ratingsList.average().format(1)}/5")
                appendLine("📍 Адрес: ${place.address}")
                appendLine("📞 Телефон: ${place.price}")
                appendLine("🕐 Режим работы: ${place.telephone}")
                if (place.title.isNotEmpty()) {
                    appendLine("🌐 Сайт: ${place.title}")
                }
                appendLine()
                appendLine(place.description)
                appendLine()
                appendLine("Поделиться из приложения Guide Кучугуры")
            }

            withContext(Dispatchers.Main) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                context.startActivity(
                    Intent.createChooser(shareIntent, "Поделиться местом")
                )
            }
        }
    }

    // Функция для форматирования числа
    fun Double.format(digits: Int) = "%.${digits}f".format(this)


}