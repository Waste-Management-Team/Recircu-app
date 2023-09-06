package com.godzuche.recircu.feature.authentication.presentation

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godzuche.recircu.R
import com.godzuche.recircu.core.designsystem.components.AuthTextButton
import com.godzuche.recircu.core.designsystem.components.GoogleSignInButton
import com.godzuche.recircu.core.designsystem.components.RecircuButton
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import kotlinx.coroutines.launch

@Composable
fun SellerSignInRoute(
    oneTapClient: SignInClient,
    navigateToHome: () -> Unit,
    signInViewModel: SignInViewModel = hiltViewModel()
) {
    val state by signInViewModel.authState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isLoading = (state is SignInUiState.Loading)
    val isSignInError = (state is SignInUiState.Error)
    val isOneTapUi = (state is SignInUiState.OneTapUi<*>)
    val isSignInSuccessful = state is SignInUiState.Success

    val googleSignLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                coroutineScope.launch {
                    val intent = activityResult.data ?: return@launch
                    val credential = oneTapClient.getSignInCredentialFromIntent(intent)
                    signInViewModel.googleSignIn(signInCredential = credential)
                }
            }
        }
    )

    SignInScreen(
        isLoading = isLoading,
        onGoogleSignInClick = {
            signInViewModel.requestOneTapSignIn()
        },
        onSignInClick = {
            // Todo: Implement sign-in with email and password
            navigateToHome.invoke()
        }
    )

    LaunchedEffect(key1 = isSignInError) {
        if (isSignInError) {
            (state as SignInUiState.Error).exception?.let { error ->
                Toast.makeText(
                    context,
                    error.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    LaunchedEffect(key1 = isOneTapUi) {
        if (isOneTapUi) {
            val signInIntentSender = (state as SignInUiState.OneTapUi<*>).data
            googleSignLauncher.launch(
                IntentSenderRequest.Builder(
                    (signInIntentSender as BeginSignInResult).pendingIntent.intentSender
                ).build()
            )
        }
    }

    LaunchedEffect(key1 = isSignInSuccessful) {
        if (isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful!",
                Toast.LENGTH_LONG
            ).show()
            navigateToHome.invoke()
        }
    }
}

fun Exception.getOneTapAuthErrorText(): String {
    return if (this is ApiException) {
        when (this.status.statusCode) {
            CommonStatusCodes.NETWORK_ERROR -> "Network Error: Please check your internet connection"
            CommonStatusCodes.INTERNAL_ERROR -> "Oops! An error occurred! Please check internet connection"
            else -> this.message ?: "Oops! An error occurred!"
        }
    } else this.message ?: "Oops! An error occurred!"
}

@Composable
fun SignInScreen(
    onGoogleSignInClick: () -> Unit,
    onSignInClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val lazyGridState = rememberLazyGridState()

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(1),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        item {
            Text(
                text = "Welcome back!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        item {
            Text(
                text = "Sign into your account",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
        item {
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Email (Required)")
                }
            )
        }
        item {
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Password (Required)")
                }
            )
        }
        item {
            TextButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentWidth(align = Alignment.End)
            ) {
                Text(text = "Forgot Password?")
            }
        }
        item {
            RecircuButton(
                onClick = onSignInClick,
                label = "Sign in"
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    thickness = Dp.Hairline
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = "OR",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Divider(
                    modifier = Modifier.weight(1f),
                    thickness = Dp.Hairline
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            GoogleSignInButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onGoogleSignInClick.invoke()
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            AuthTextButton(
                text = R.string.dont_have_an_account,
                action = R.string.sign_up,
                onClick = {},
                icon = Icons.Filled.Login,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}