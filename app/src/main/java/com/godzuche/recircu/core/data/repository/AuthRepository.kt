package com.godzuche.recircu.core.data.repository

import com.godzuche.recircu.core.common.RecircuResult
import com.godzuche.recircu.core.domain.UserData
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(
        email: String,
        password: String
    ): Flow<RecircuResult<AuthResult>>

    fun registerUser(
        email: String,
        password: String
    ): Flow<RecircuResult<AuthResult>>

    suspend fun requestOneTapSignIn(): Flow<RecircuResult<BeginSignInResult>>

    suspend fun googleSignInWithCredential(signInCredential: SignInCredential): Flow<RecircuResult<UserData?>>

    suspend fun signOut(): Flow<RecircuResult<Nothing?>>

    fun getSignedInUser(): Flow<RecircuResult<UserData?>>
}