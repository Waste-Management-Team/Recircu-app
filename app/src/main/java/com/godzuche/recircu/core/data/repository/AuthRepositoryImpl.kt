package com.godzuche.recircu.core.data.repository

import android.util.Log
import com.godzuche.recircu.BuildConfig
import com.godzuche.recircu.core.common.RecircuResult
import com.godzuche.recircu.core.domain.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val oneTapClient: SignInClient
) : AuthRepository {
    override fun loginUser(
        email: String,
        password: String
    ): Flow<RecircuResult<AuthResult>> {
        return flow {
            emit(RecircuResult.Loading)
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            emit(RecircuResult.Success(authResult))
        }.catch {
            emit(RecircuResult.Error(it))
        }
    }

    override fun registerUser(
        email: String,
        password: String
    ): Flow<RecircuResult<AuthResult>> {
        return flow {
            emit(RecircuResult.Loading)
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            emit(RecircuResult.Success(authResult))
        }.catch {
            emit(RecircuResult.Error(it))
        }
    }

    override suspend fun requestOneTapSignIn(): Flow<RecircuResult<BeginSignInResult>> {
        return flow {
            try {
                emit(RecircuResult.Loading)
                val beginSignInResult = oneTapClient.beginSignIn(buildSignInRequest()).await()
                emit(RecircuResult.Success(data = beginSignInResult))
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                emit(RecircuResult.Error(exception = e))
                // No saved credentials found. Try launching the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                try {
                    emit(RecircuResult.Loading)
                    val beginSignUpResult = oneTapClient.beginSignIn(buildSignUpRequest()).await()
                    emit(RecircuResult.Success(data = beginSignUpResult))
                } catch (e: Exception) {
                    e.printStackTrace()
                    if (e is CancellationException) throw e
                    emit(RecircuResult.Error(exception = e))
                }
            }
        }
    }

    override suspend fun googleSignInWithCredential(signInCredential: SignInCredential): Flow<RecircuResult<UserData?>> {
        return flow {
            try {
                emit(RecircuResult.Loading)
                val googleIdToken = signInCredential.googleIdToken
                val username = signInCredential.id
                val password = signInCredential.password
                val firebaseUser = when {
                    googleIdToken != null -> {
                        val googleCredentials =
                            GoogleAuthProvider.getCredential(googleIdToken, null)
                        auth.signInWithCredential(googleCredentials).await().user
                    }

                    password != null -> {
                        // Can be used with the username to authenticate with a backend
                        null
                    }

                    else -> {
                        // Should not happen
                        null
                    }
                }
                emit(
                    RecircuResult.Success(
                        data = firebaseUser?.run {
                            UserData(
                                userId = uid,
                                displayName = displayName,
                                email = email,
                                profilePictureUrl = photoUrl?.toString()
                            )
                        }
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                var msg = ""
                if (e is ApiException) {
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            Log.d(TAG, "One-tap dialog was closed.")
                            // Todo: Temporary disable the One Tap sign-in UI
                        }

                        CommonStatusCodes.NETWORK_ERROR -> {
                            Log.d(TAG, "One-tap encountered a network error.")
                        }

                        CommonStatusCodes.INTERNAL_ERROR -> {
                            msg = "Oops! An error occurred! Please check internet connection"
                            Log.d(TAG, msg)
                        }

                        else -> Log.d(TAG, "One-tap encountered an else api error.")
                    }
                } else Log.d(TAG, "One-tap encountered a not_api error.")
                emit(RecircuResult.Error(exception = e))
            }
        }
    }

    override suspend fun signOut(): Flow<RecircuResult<Nothing?>> {
        return flow {
            emit(RecircuResult.Loading)
            try {
                oneTapClient.signOut().await()
                auth.signOut()
                emit(RecircuResult.Success(data = null))
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                emit(RecircuResult.Error(exception = e))
            }
        }
    }

    override fun getSignedInUser(): Flow<RecircuResult<UserData?>> {
        return flow {
            emit(RecircuResult.Loading)
            auth.currentUser?.run {
                emit(
                    RecircuResult.Success(
                        data = UserData(
                            userId = uid,
                            displayName = displayName,
                            email = email,
                            profilePictureUrl = photoUrl?.toString()
                        )
                    )
                )
            } ?: emit(RecircuResult.Success(data = null))
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(true)
                    .setServerClientId(BuildConfig.CLIENT_ID)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun buildSignUpRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.CLIENT_ID)
                    .build()
            )
            .build()
    }

}