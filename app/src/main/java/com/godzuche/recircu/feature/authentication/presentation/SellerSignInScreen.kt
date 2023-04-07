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
import com.godzuche.recircu.core.firebase.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@Composable
fun SellerSignInRoute(
    navigateToHome: () -> Unit,
    signInViewModel: SignInViewModel = hiltViewModel()
) {
    val state by signInViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthUiClient = GoogleAuthUiClient(
        context = context,
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
        }
    }

    SignInScreen(
        onGoogleSignInClick = {
            coroutineScope.launch {
                val signInIntentSender = googleAuthUiClient.signIn()
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
                    /*val gso = GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                    )
                        .requestEmail()
                        .requestIdToken(BuildConfig.CLIENT_ID)
                        .build()

                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    googleSignInLauncher.launch(googleSignInClient.signInIntent)*/
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
    /*    val googleSignInLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val result = account.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
                viewModel.googleSignIn(credentials)
            } catch (it: ApiException) {
                print(it)
            }
        }*/
//    val googleSignInState by viewModel.googleSignInState.collectAsStateWithLifecycle()
/*    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = googleSignInState) {
        scope.launch {
            if (googleSignInState.success != null) {
                Toast.makeText(context, "Sign in success", Toast.LENGTH_LONG).show()
                onSignIn.invoke()
                viewModel.setGoogleSignInState(null)
            }
        }
    }*/
}