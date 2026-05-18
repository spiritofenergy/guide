package com.kodex.guide.data.mapper

import com.kodex.guide.data.model.PersonalDataDTO
import com.kodex.guide.domain.model.PersonalData
import kotlin.String

fun PersonalDataDTO.toPersonalData(): PersonalData {
    return PersonalData(
        name = name,
        phone = phone,
        lastVisit = lastVisit,
    )
}
fun PersonalData.toDTO(): PersonalDataDTO {
    return PersonalDataDTO(
        name = name,
        phone = phone,
        lastVisit = lastVisit,
    )
}