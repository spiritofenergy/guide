package com.kodex.guide.presentation.castom

import com.kodex.guide.utils.FirebaseConst

data class FilterData(
    val minPrise: Int = 0,
    val maxPrise: Int = 0,
    val filterType: String = FirebaseConst.TITLE

)
