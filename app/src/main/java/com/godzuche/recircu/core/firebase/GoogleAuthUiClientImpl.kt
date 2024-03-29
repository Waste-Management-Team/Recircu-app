package com.godzuche.recircu.core.firebase
/*

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.godzuche.recircu.BuildConfig
import com.godzuche.recircu.feature.authentication.presentation.SignInResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val TAG = "GoogleAuthUiClient"

class GoogleAuthUiClientImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val oneTapClient: SignInClient
) : GoogleAuthUiClient {

    override suspend fun signIn(): OneTapSignInResponse {
        val result = try {
            val signInResult = oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
            OneTapSignInResponse.Success(
                data = signInResult.pendingIntent.intentSender
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
//            Log.d(TAG, "Can't open One Tap UI: ${e.message}")
//            null
            OneTapSignInResponse.Failure(e)
            // No saved credentials found. Try launching the One Tap sign-up flow, or
            // do nothing and continue presenting the signed-out UI.
            try {
                val signUpResult = oneTapClient.beginSignIn(
                    buildSignUpRequest()
                ).await()
                OneTapSignInResponse.Success(
                    data = signUpResult?.pendingIntent?.intentSender
                )
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                OneTapSignInResponse.Failure(e)
            }
        }
        return result
    }

    override suspend fun signInWithIntent(intent: Intent): SignInResult {
        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleIdToken = credential.googleIdToken
            val username = credential.id
            val password = credential.password
            val user = when {
                googleIdToken != null -> {
                    Log.d(TAG, "Got ID token.")
                    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
                    auth.signInWithCredential(googleCredentials).await().user
                }
                password != null -> {
                    //not needed
                    null
                }
                else -> {
                    // Shouldn't happen.
                    Log.d(TAG, "No ID token or password!")
                    null
                }
            }
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        displayName = displayName,
                        email = email,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (it: Exception) {
            it.printStackTrace()
            if (it is CancellationException) throw it
            var msg = ""
            if (it is ApiException) {
                when (it.statusCode) {
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
            SignInResult(
                data = null,
                errorMessage = it.message
            )
        }
    }

    override suspend fun signOut(): AuthResult {
        return try {
            oneTapClient.signOut().await()
            auth.signOut()
            AuthResult.Success
        } catch (it: Exception) {
            it.printStackTrace()
            if (it is CancellationException) throw it
            AuthResult.Failure(it)
        }
    }

    override fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            displayName = displayName,
            email = email,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
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
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.CLIENT_ID)
                    .build()
            )
            .build()
    }
}

sealed interface OneTapSignInResponse {
    data class Success(val data: IntentSender?) : OneTapSignInResponse
    data class Failure(val e: Exception) : OneTapSignInResponse
}

sealed interface AuthResult {
    object Success : AuthResult
    data class Failure(val e: Exception) : AuthResult
}*/
