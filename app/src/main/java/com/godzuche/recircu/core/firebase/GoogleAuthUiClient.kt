package com.godzuche.recircu.core.firebase

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.godzuche.recircu.BuildConfig
import com.godzuche.recircu.feature.authentication.presentation.SignInResult
import com.godzuche.recircu.feature.authentication.presentation.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val TAG = "GoogleAuthUiClient"

//Todo: Create an abstraction of this class using interface
class GoogleAuthUiClient @Inject constructor(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): OneTapSignInRespose {
        val result = try {
            val signInResult = oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
            OneTapSignInRespose.Success(
                data = signInResult?.pendingIntent?.intentSender
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
//            Log.d(TAG, "Can't open One Tap UI: ${e.message}")
//            null
            OneTapSignInRespose.Failure(e)
            try {
                val signUpResult = oneTapClient.beginSignIn(
                    buildSignUpRequest()
                ).await()
                OneTapSignInRespose.Success(
                    data = signUpResult?.pendingIntent?.intentSender
                )
            } catch (e: Exception) {
                OneTapSignInRespose.Failure(e)
            }
        }
        return result
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
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
                    CommonStatusCodes.NETWORK_ERROR -> {
                        Log.d(TAG, "One-tap encountered a network error.")
                    }
                    CommonStatusCodes.INTERNAL_ERROR -> {
                        msg = "Oops! An error occurred! Please check internet connection"
                    }
                    else -> Log.d(TAG, "One-tap encountered an else api error.")
                }
            } else Log.d(TAG, "One-tap encountered an else not_api error.")
            SignInResult(
                data = null,
                errorMessage = it.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (it: Exception) {
            it.printStackTrace()
            if (it is CancellationException) throw it

        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
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

sealed interface OneTapSignInRespose {
    data class Success(val data: IntentSender?) : OneTapSignInRespose
    data class Failure(val e: Exception) : OneTapSignInRespose
}