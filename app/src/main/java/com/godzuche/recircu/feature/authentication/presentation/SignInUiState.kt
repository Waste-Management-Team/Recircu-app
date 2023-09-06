package com.godzuche.recircu.feature.authentication.presentation

import com.godzuche.recircu.core.domain.UserData

sealed interface SignInUiState {
    object NotLoading : SignInUiState
    object Loading : SignInUiState
    data class OneTapUi<T>(val data: T) : SignInUiState
    data class Success(val data: UserData?) : SignInUiState
    data class Error(val exception: Throwable? = null) : SignInUiState
}