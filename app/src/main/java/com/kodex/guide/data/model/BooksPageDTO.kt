package com.kodex.guide.data.model

import com.kodex.guide.domain.model.Book

data class BooksPageDTO(
    val books: List<BookDTO>,
    val listItemId: String?,

    ) {
}