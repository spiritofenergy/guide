package com.kodex.guide.ui.settingsScreen

import com.kodex.guide.domain.model.AddressData
import com.kodex.guide.domain.model.PersonalData
import com.kodex.guide.domain.model.UserSettingsData

fun PersonalData.upToDate(oldPersonalData: PersonalData): Boolean {
    return (this.name == oldPersonalData.name && this.phone == oldPersonalData.phone
            ) || this.name.isEmpty() && this.phone.isEmpty()
}

fun AddressData.upToDate(oldAddressData: AddressData): Boolean {
    return (
               this.city == oldAddressData.city
            && this.street == oldAddressData.street
            && this.flat == oldAddressData.flat
            && this.postCode == oldAddressData.postCode

         )  || this.city.isEmpty()
            || this.street.isEmpty()
            || this.flat.isEmpty()
            || this.postCode.isEmpty()
}
fun UserSettingsData.upToDate(oldData: UserSettingsData): Boolean {
    return (
               this.imageFormat == oldData.imageFormat
            && this.quality == oldData.quality
            && this.size == oldData.size
            )
}
fun AddressData.toStringAddress(): String {
    return this.city + ", " + this.street + ", " + this.flat + ", " + this.postCode
}
