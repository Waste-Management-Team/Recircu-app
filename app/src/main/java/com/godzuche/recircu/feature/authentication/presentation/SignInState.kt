package com.godzuche.recircu.feature.authentication.presentation

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)
