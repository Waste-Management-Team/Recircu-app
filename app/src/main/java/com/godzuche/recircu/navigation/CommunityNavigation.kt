package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.godzuche.recircu.features.seller.community.ConnectRoute
import com.google.accompanist.navigation.animation.composable

const val connectRoute = "connect_route"

fun NavController.navigateToCommunity(navOptions: NavOptions? = null) {
    this.navigate(connectRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.connectScreen() {
    composable(route = connectRoute) {
        ConnectRoute()
    }
}