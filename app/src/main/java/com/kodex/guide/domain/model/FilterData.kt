package com.kodex.guide.domain.model

import com.kodex.guide.data.source.remote.FirebaseConst

data class FilterData(
    val minPrise: Int = 0,
    val maxPrise: Int = 0,
    val filterType: FilterType = FilterType.TITLE

)