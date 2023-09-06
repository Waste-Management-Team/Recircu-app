package com.godzuche.recircu.feature.authentication.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.godzuche.recircu.navigation.sellerSignUpScreen
import com.google.android.gms.auth.api.identity.SignInClient

const val sellerAuthGraphRoute = "seller_auth_graph"

fun NavController.navigateToSellerAuth(navOptions: NavOptions? = null) {
    this.navigate(sellerAuthGraphRoute, navOptions)
}

fun NavGraphBuilder.sellerAuthGraph(
    oneTapClient: SignInClient,
    navigateToHome: () -> Unit
) {
    navigation(
        route = sellerAuthGraphRoute,
        startDestination = sellerSignInRoute
    ) {
        sellerSignInScreen(
            oneTapClient = oneTapClient,
            navigateToHome = navigateToHome
        )
        sellerSignUpScreen()
    }
}