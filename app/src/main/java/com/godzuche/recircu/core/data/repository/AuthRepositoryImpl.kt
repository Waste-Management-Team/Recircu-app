package com.godzuche.recircu.core.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override fun loginUser(
        email: String,
        password: String
    ): Flow<com.godzuche.recircu.core.common.Result<AuthResult>> {
        return flow {
            emit(com.godzuche.recircu.core.common.Result.Loading)
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(com.godzuche.recircu.core.common.Result.Success(result))
        }.catch {
            emit(com.godzuche.recircu.core.common.Result.Error(it))
        }
    }

    override fun registerUser(
        email: String,
        password: String
    ): Flow<com.godzuche.recircu.core.common.Result<AuthResult>> {
        return flow {
            emit(com.godzuche.recircu.core.common.Result.Loading)
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(com.godzuche.recircu.core.common.Result.Success(result))
        }.catch {
            emit(com.godzuche.recircu.core.common.Result.Error(it))
        }
    }

    override fun googleSignIn(credential: AuthCredential): Flow<com.godzuche.recircu.core.common.Result<AuthResult>> {
        return flow {
            emit(com.godzuche.recircu.core.common.Result.Loading)
            val result = firebaseAuth.signInWithCredential(credential).await()
            emit(com.godzuche.recircu.core.common.Result.Success(result))
        }.catch {
            emit(com.godzuche.recircu.core.common.Result.Error(it))
        }
    }
}