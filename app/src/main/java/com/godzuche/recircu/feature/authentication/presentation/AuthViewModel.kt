/*
package com.godzuche.recircu.feature.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.recircu.core.util.Result
import com.godzuche.recircu.core.data.repository.AuthRepository
import com.godzuche.recircu.feature.authentication.presentation.SignInState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    private val _googleState = MutableStateFlow(GoogleSignInState())
    val googleSignInState = _googleState.asStateFlow()

    fun googleSignIn(credential: AuthCredential) {
        viewModelScope.launch {
            authRepository.googleSignIn(credential).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _googleState.update {
                            GoogleSignInState(success = result.data)
                        }
                    }
                    is Result.Loading -> {
                        _googleState.update {
                            GoogleSignInState(loading = true)
                        }
                    }
                    is Result.Error -> {
                        _googleState.update {
                            GoogleSignInState(error = result.exception?.message.toString())
                        }
                    }
                }
            }
        }
    }

    fun loginUser(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            authRepository.loginUser(email, password).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _signInState.send(
                            SignInState(isSuccess = "Sign in success!")
                        )
                    }
                    is Result.Loading -> {
                        _signInState.send(
                            SignInState(isLoading = true)
                        )
                    }
                    is Result.Error -> {
                        _signInState.send(
                            SignInState(isError = result.exception?.message.toString())
                        )
                    }
                }
            }
        }
    }

    fun setGoogleSignInState(authResult: AuthResult?) {
        _googleState.update {
            it.copy(
                success = authResult
            )
        }
    }
}*/
