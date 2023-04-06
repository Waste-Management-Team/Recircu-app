package com.godzuche.recircu.core.firebase

import android.content.Context
import com.google.android.gms.auth.api.identity.SignInClient
import javax.inject.Inject

class GoogleAuthUiClient @Inject constructor(
    private val context: Context,
    private val oneTapClient: SignInClient,
) {
}