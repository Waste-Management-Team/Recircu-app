package com.godzuche.recircu.feature.authentication.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.recircu.UserAuthState
import com.godzuche.recircu.core.common.RecircuResult
import com.godzuche.recircu.core.data.repository.AuthRepository
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<SignInUiState>(SignInUiState.NotLoading)
    val authState = _authState.asStateFlow()

    private val _userState = MutableStateFlow<UserAuthState>(UserAuthState.Loading)
    val userState = _userState.asStateFlow()

    fun requestOneTapSignIn() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.requestOneTapSignIn().onEach { result ->
                when (result) {
                    is RecircuResult.Loading -> {
                        _authState.update {
                            SignInUiState.Loading
                        }
                    }

                    is RecircuResult.Error -> {
                        _authState.update {
                            SignInUiState.Error(result.exception)
                        }
                    }

                    is RecircuResult.Success -> {
                        _authState.update {
                            SignInUiState.OneTapUi(
                                data = result.data
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun googleSignIn(signInCredential: SignInCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.googleSignInWithCredential(signInCredential = signInCredential)
                .onEach { result ->
                    when (result) {
                        is RecircuResult.Loading -> {
                            _authState.update {
                                SignInUiState.Loading
                            }
                        }

                        is RecircuResult.Error -> {
                            Log.d("Google Sign-in", result.exception?.message ?: "Unknown error")
                            _authState.update {
                                SignInUiState.Error(exception = result.exception)
                            }
                        }

                        is RecircuResult.Success -> {
                            _authState.update {
                                SignInUiState.Success(data = result.data)
                            }
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getSignedInUser().onEach { result ->
                when (result) {
                    is RecircuResult.Loading -> {
                        _userState.update {
                            UserAuthState.Loading
                        }
                    }

                    is RecircuResult.Success -> {
                        result.data?.let {
                            _userState.update {
                                UserAuthState.SignedIn(userData = result.data)
                            }
                        } ?: _userState.update {
                            UserAuthState.NotSignedIn
                        }
                    }

                    else -> Unit
                }
            }.launchIn(viewModelScope)
        }
    }

    fun resetState() {
        _authState.update { SignInUiState.NotLoading }
    }
}