package com.godzuche.recircu.feature.authentication.presentation

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val displayName: String?,
    val email: String?,
    val profilePictureUrl: String?
)