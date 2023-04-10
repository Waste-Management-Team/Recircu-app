package com.godzuche.recircu.feature.authentication.presentation

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.godzuche.recircu.core.firebase.GoogleAuthUiClientImpl
import com.godzuche.recircu.core.firebase.OneTapSignInRespose
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun SellerSignInRoute(
    navigateToHome: () -> Unit,
    signInViewModel: SignInViewModel = hiltViewModel()
) {
    val state by signInViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthUiClient = GoogleAuthUiClientImpl(
        context = context,
        auth = Firebase.auth,
        oneTapClient = Identity.getSignInClient(context)
    )

    val googleSignLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = activityResult.data ?: return@launch
                    )
                    signInViewModel.onSignInResult(signInResult)
                }
            }
        }
    )

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful!",
                Toast.LENGTH_LONG
            ).show()
            navigateToHome.invoke()
        }
    }

    SignInScreen(
        onGoogleSignInClick = {
            coroutineScope.launch {
                val signInIntentSender =
                    when (val oneTapSignInRespose = googleAuthUiClient.signIn()) {
                        is OneTapSignInRespose.Success -> oneTapSignInRespose.data
                        is OneTapSignInRespose.Failure -> {
                            Toast.makeText(
                                context,
                                getOneTapAuthErrorText(oneTapSignInRespose.e),
                                Toast.LENGTH_LONG
                            ).show()
                            null
                        }
                    }
                googleSignLauncher.launch(
                    IntentSenderRequest.Builder(
                        signInIntentSender ?: return@launch
                    ).build()
                )
            }
        },
        onSignInClick = {
            // Sign in with email and password
            navigateToHome.invoke()
        }
    )
}

fun getOneTapAuthErrorText(e: Exception): String {
    return if (e is ApiException) {
        when (e.status.statusCode) {
            CommonStatusCodes.NETWORK_ERROR -> "Network Error: Please check your internet connection"
            CommonStatusCodes.INTERNAL_ERROR -> "Oops! An error occurred! Please check internet connection"
            else -> e.message ?: "Oops! An error occurred!"
        }
    } else e.message ?: "Oops! An error occurred!"
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onGoogleSignInClick: () -> Unit,
    onSignInClick: () -> Unit
//    viewModel: AuthViewModel = hiltViewModel()
) {
    val lazyGridState = rememberLazyGridState()


/*    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (googleSignInState.loading) {
            CircularProgressIndicator()
        }
    }*/

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(1),
        modifier = Modifier.fillMaxSize(),
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
}