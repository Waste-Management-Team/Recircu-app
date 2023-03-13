package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.godzuche.recircu.features.getting_started.GetStartedRoute
import com.google.accompanist.navigation.animation.composable

const val gettingStartedRoute = "getting_started_route"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.gettingStartedScreen(
    navigateToDashboard: () -> Unit
) {
    composable(gettingStartedRoute) {
        GetStartedRoute(navigateToDashboard = navigateToDashboard)
    }
}