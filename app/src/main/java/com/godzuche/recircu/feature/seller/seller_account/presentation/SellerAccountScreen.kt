package com.godzuche.recircu.feature.seller.seller_account.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godzuche.recircu.AppMainViewModel
import com.godzuche.recircu.R
import com.godzuche.recircu.UserAuthState
import com.godzuche.recircu.core.designsystem.components.DetailsTextField
import com.godzuche.recircu.feature.seller.seller_profile.presentation.ProfileImage

@Composable
fun SellerAccountRoute(
    appMainViewModel: AppMainViewModel,
) {
    val userAuthState by appMainViewModel.userAuthState.collectAsStateWithLifecycle()

    SellerAccountScreen(
        userAuthState = userAuthState,
    )
}

@Composable
fun SellerAccountScreen(
    userAuthState: UserAuthState,
    modifier: Modifier = Modifier,
) {
    val lazyGridState = rememberLazyGridState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val context = LocalContext.current

    when (userAuthState) {
        is UserAuthState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is UserAuthState.SignedIn -> {
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
                    ProfileImage(photo = userAuthState.userData.profilePictureUrl)
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

        else -> Unit
    }

}