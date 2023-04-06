package com.godzuche.recircu.core.domain.repository

import com.godzuche.recircu.core.util.Result
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(
        email: String,
        password: String
    ): Flow<Result<AuthResult>>

    fun registerUser(
        email: String,
        password: String
    ): Flow<Result<AuthResult>>

    fun googleSignIn(credential: AuthCredential): Flow<Result<AuthResult>>
}