package com.godzuche.recircu.core.firebase.di

import android.content.Context
import com.godzuche.recircu.core.firebase.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesGoogleAuthUiClient(
        @ApplicationContext context: Context
    ) = GoogleAuthUiClient(
        context = context,
        oneTapClient = Identity.getSignInClient(context)
    )
}