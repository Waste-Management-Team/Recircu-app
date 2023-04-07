package com.godzuche.recircu.feature.authentication.presentation

/*data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)*/


data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
