package com.godzuche.recircu.feature.authentication.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.godzuche.recircu.navigation.userSelectionRoute
import com.godzuche.recircu.navigation.userSelectionScreen

const val authGraphRoute = "auth_route"

fun NavController.navigateToAuth(navOptions: NavOptions? = null) {
    this.navigate(authGraphRoute, navOptions)
}

fun NavGraphBuilder.authGraph(
    onSellerClick: () -> Unit,
    nestedGraph: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = authGraphRoute,
        startDestination = userSelectionRoute
    ) {
        userSelectionScreen(
            onSellerClick = onSellerClick
        )
        nestedGraph()
    }
}