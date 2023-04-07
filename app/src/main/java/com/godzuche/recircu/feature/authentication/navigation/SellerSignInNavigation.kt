package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.godzuche.recircu.feature.authentication.presentation.SellerSignInRoute
import com.google.accompanist.navigation.animation.composable

const val sellerSignInRoute = "sign_in_route"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.sellerSignInScreen(
    navigateToHome: () -> Unit
) {
    composable(
        route = sellerSignInRoute
    ) {
        SellerSignInRoute(navigateToHome = navigateToHome)
    }
}