package com.godzuche.recircu.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation

const val sellerAuthGraphRoute = "seller_auth_graph"

fun NavController.navigateToSellerAuth(navOptions: NavOptions? = null) {
    this.navigate(sellerAuthGraphRoute, navOptions)
}

fun NavGraphBuilder.sellerAuthGraph(
    onSignIn: () -> Unit
) {
    navigation(
        route = sellerAuthGraphRoute,
        startDestination = sellerSignInRoute
    ) {
        sellerSignInScreen(onSignIn = onSignIn)
        sellerSignUpScreen()
    }
}