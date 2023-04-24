package com.godzuche.recircu.feature.seller.seller_account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.godzuche.recircu.R
import com.godzuche.recircu.core.designsystem.components.DetailsTextField
import com.godzuche.recircu.core.firebase.GoogleAuthUiClientImpl
import com.godzuche.recircu.feature.seller.seller_profile.presentation.ProfileImage
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SellerAccountRoute() {
    SellerAccountScreen()
}

@Preview(showBackground = true)
@Composable
fun SellerAccountScreen(
    modifier: Modifier = Modifier
) {
    val lazyGridState = rememberLazyGridState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val context = LocalContext.current
    val googleAuthUiClientImpl = GoogleAuthUiClientImpl(
        oneTapClient = Identity.getSignInClient(context),
        auth = Firebase.auth
    )

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        state = lazyGridState,
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            ProfileImage(photo = googleAuthUiClientImpl.getSignedInUser()?.profilePictureUrl)
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            DetailsTextField(
                label = stringResource(R.string.first_name),
                value = "God'swill",
                onValueChange = { },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    autoCorrect = false
                ),
                singleLine = true
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            DetailsTextField(
                label = stringResource(R.string.last_name),
                value = "Jonathan",
                onValueChange = { },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    autoCorrect = false
                ),
                singleLine = true
            )
        }
    }
}