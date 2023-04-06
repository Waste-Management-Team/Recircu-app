package com.godzuche.recircu.feature.authentication

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godzuche.recircu.BuildConfig
import com.godzuche.recircu.R
import com.godzuche.recircu.core.designsystem.components.RecircuButton
import com.godzuche.recircu.core.designsystem.icon.RecircuIcons
import com.godzuche.recircu.feature.authentication.presentation.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@Composable
fun SellerSignInRoute(onSignIn: () -> Unit) {
    SignInScreen(
        onSignIn = onSignIn
    )
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignIn: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lazyGridState = rememberLazyGridState()

    val googleSignInLauncher =
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
        }
    val googleSignInState by viewModel.googleSignInState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = googleSignInState) {
        scope.launch {
            if (googleSignInState.success != null) {
                Toast.makeText(context, "Sign in success", Toast.LENGTH_LONG).show()
                onSignIn.invoke()
                viewModel.setGoogleSignInState(null)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (googleSignInState.loading) {
            CircularProgressIndicator()
        }
    }

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
                onClick = onSignIn,
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
                onClick = {
                    val gso = GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                    )
                        .requestEmail()
                        .requestIdToken(BuildConfig.CLIENT_ID)
                        .build()

                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    googleSignInLauncher.launch(googleSignInClient.signInIntent)
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
                icon = Icons.Filled.Login

            )
        }
    }
}

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        modifier = modifier
    ) {
        val iconWidth = 24.dp
        Icon(
            painterResource(id = RecircuIcons.Google24),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            text = "Sign in with Google",
            modifier = Modifier
                .weight(1f)
                .offset(x = -iconWidth / 2),
            color = Color.Black,
            textAlign = TextAlign.Center
        )

    }
}

@Composable
fun AuthTextButton(
    @StringRes text: Int,
    @StringRes action: Int,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append(
                        stringResource(text)
                    )
                }
                append(" ")
                withStyle(
                    SpanStyle(color = MaterialTheme.colorScheme.primary)
                ) {
                    append(stringResource(action))
                }
            }
            )
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        }
    }
}