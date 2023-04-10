package com.godzuche.recircu.core.firebase

import android.content.Intent
import com.godzuche.recircu.feature.authentication.presentation.SignInResult
import com.godzuche.recircu.feature.authentication.presentation.UserData

interface GoogleAuthUiClient {
    suspend fun signIn(): OneTapSignInRespose
    suspend fun signInWithIntent(intent: Intent): SignInResult
    suspend fun signOut()
    fun getSignedInUser(): UserData?
}