package com.godzuche.recircu.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.godzuche.recircu.core.firebase.GoogleAuthUiClient

const val sellerAuthGraphRoute = "seller_auth_graph"

fun NavController.navigateToSellerAuth(navOptions: NavOptions? = null) {
    this.navigate(sellerAuthGraphRoute, navOptions)
}

fun NavGraphBuilder.sellerAuthGraph(
    googleAuthUiClient: GoogleAuthUiClient,
    navigateToHome: () -> Unit
) {
    navigation(
        route = sellerAuthGraphRoute,
        startDestination = sellerSignInRoute
    ) {
        sellerSignInScreen(
            googleAuthUiClient = googleAuthUiClient,
            navigateToHome = navigateToHome
        )
        sellerSignUpScreen()
    }
}