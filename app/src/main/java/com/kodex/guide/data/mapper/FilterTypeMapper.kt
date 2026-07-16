package com.kodex.guide.data.mapper

import com.kodex.guide.data.source.remote.FirebaseConst
import com.kodex.guide.domain.model.FilterType

fun FilterType.toFirebaseFields(): String = when(this) {
    FilterType.TITLE -> FirebaseConst.TITLE
    FilterType.PRICE -> FirebaseConst.PRICE
}