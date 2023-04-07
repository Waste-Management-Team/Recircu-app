package com.godzuche.recircu.core.domain.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(
        email: String,
        password: String
    ): Flow<com.godzuche.recircu.core.common.Result<AuthResult>>

    fun registerUser(
        email: String,
        password: String
    ): Flow<com.godzuche.recircu.core.common.Result<AuthResult>>

    fun googleSignIn(credential: AuthCredential): Flow<com.godzuche.recircu.core.common.Result<AuthResult>>
}