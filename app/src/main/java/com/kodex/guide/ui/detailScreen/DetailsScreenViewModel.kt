package com.kodex.guide.ui.detailScreen

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodex.guide.data.repository.BooksRepo_Impl
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.presentation.navigation.NavRoutes
import com.kodex.guide.utils.firebase.FireStoreManagerPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging,
    val booksRepo: BooksRepo_Impl
) : ViewModel() {
  //  val ratingState = mutableStateOf("0")
    val commentState = mutableStateOf(emptyList<RatingData>())
    val ratingDataState = mutableStateOf<RatingData?>(RatingData())

    fun insertRating(ratingData: RatingData, bookId: String) {
      //  booksRepo.insertUserRating(ratingData, bookId)
    }
    fun getBookComments(bookId: String) = viewModelScope.launch{
      //  commentState.value = fireStoreManager.getBookComments(bookId)
    }
    fun getUserRating(bookId: String) = viewModelScope.launch {
       // ratingDataState.value = fireStoreManager.getUserRating(bookId)

    }

    // Выносим логику шаринга в отдельную функцию
    suspend fun sharePlace(
        context: Context,
        place: NavRoutes.ParallaxNavObject,
        coroutineScope: kotlinx.coroutines.CoroutineScope
    ) {
        withContext(Dispatchers.IO) {
            val shareText = buildString {
                appendLine(place.title)
                appendLine()
                appendLine("⭐️ Рейтинг: ${place.ratingsList.average().format(1)}/5")
                appendLine("📍 Адрес: ${place.address}")
                appendLine("📞 Телефон: ${place.telephone}")
                appendLine("🕐 Режим работы: ${place.telephone}")
                if (place.title.isNotEmpty()) {
                    appendLine("🌐 Сайт: ${place.title}")
                }
                appendLine()
                appendLine(place.description)
                appendLine()
                appendLine("Поделиться из приложения Искра Кучугуры")
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