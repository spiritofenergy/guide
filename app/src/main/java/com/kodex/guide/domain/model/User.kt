package com.kodex.guide.domain.model


 data class User(
     val userName: String? = null,
     val uid: String,
     val email: String?,
     val role: UserRole = UserRole.USER,
     val isAnonymous: Boolean = false,
     val isRegistered: Boolean = false,

     )

