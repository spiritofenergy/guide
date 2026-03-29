package com.kodex.guide.ui.castom

import com.kodex.guide.ui.utils.FirebaseConst

data class FilterData(
    val minPrise: Int = 0,
    val maxPrise: Int = 0,
    val filterType: String = FirebaseConst.TITLE

)
