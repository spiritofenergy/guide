package com.kodex.guide.data.mapper

import com.kodex.guide.data.model.AddressDataDTO
import com.kodex.guide.domain.model.AddressData

fun AddressDataDTO.toAddressData(): AddressData {
    return AddressData(
        city = city,
        street = street,
        flat = flat,
        postCode = postCode
    )
}

fun AddressData.toDTO(): AddressDataDTO {
    return AddressDataDTO(
        city = city,
        street = street,
        flat = flat,
        postCode = postCode
    )
}