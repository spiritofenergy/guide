package com.kodex.guide.data.mapper

import com.kodex.guide.domain.model.PersonalData
import kotlin.String

fun PersonalData.toPersonalData(): PersonalData {
    return PersonalData(
        name = name,
        phone = phone,
        lastVisit = lastVisit,
    )
}