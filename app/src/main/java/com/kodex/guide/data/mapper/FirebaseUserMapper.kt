package com.kodex.guide.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.kodex.guide.data.model.UserDTO
import com.kodex.guide.domain.model.User
import com.kodex.guide.domain.model.UserRole

fun FirebaseUser.toUser(
    role: UserRole = UserRole.USER,
    isRegistered: Boolean = false
): User {
    return User(
        userName = displayName,
        uid = uid,
        email = email ?: "",
        role = role,
        isAnonymous = isAnonymous,
        isRegistered = isRegistered,


    )
}/** UserDTO → доменный User (аналог RatingDataDTO.toRatingData()) */
fun UserDTO.toUser(): User {
    return User(
        uid = uid,
        email = email,
        role = role,
        isAnonymous = isAnonymous,
        isRegistered = isRegistered,
        userName = userName
    )
}

/** доменный User → UserDTO (аналог Book.toBookDTO()) */
fun User.toDTO(): UserDTO {
    return UserDTO(
        uid = uid,
        email = email,
        isAnonymous = isAnonymous,
        isRegistered = isRegistered,
        userName = userName,
        role = role,
        createdAt = FieldValue.serverTimestamp(),
        updatedAt = FieldValue.serverTimestamp()
    )
}

