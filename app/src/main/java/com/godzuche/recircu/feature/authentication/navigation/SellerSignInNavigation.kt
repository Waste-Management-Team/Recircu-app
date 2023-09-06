package com.godzuche.recircu.feature.authentication.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.godzuche.recircu.feature.authentication.presentation.SellerSignInRoute
import com.google.accompanist.navigation.animation.composable
import com.google.android.gms.auth.api.identity.SignInClient

const val sellerSignInRoute = "sign_in_route"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.sellerSignInScreen(
    oneTapClient: SignInClient,
    navigateToHome: () -> Unit
) {
    composable(
        route = sellerSignInRoute
    ) {
        SellerSignInRoute(
            oneTapClient = oneTapClient,
            navigateToHome = navigateToHome
        )
    }
}