package com.kodex.guide.data.model

import com.kodex.guide.domain.model.BookCategories
import com.kodex.guide.presentation.castom.FilterData

data class BookFilter(
    var category: BookCategories = BookCategories.ALL,
    var searchText: String = "",
    var filterData: FilterData = FilterData()
)
