package com.godzuche.recircu.feature.seller.seller_profile.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.godzuche.recircu.AppMainViewModel
import com.godzuche.recircu.feature.seller.seller_profile.presentation.SellerProfileRoute
import com.google.accompanist.navigation.animation.composable


const val sellerProfileGraph = "seller_profile_graph"
const val sellerProfileRoute = "seller_profile_route"

fun NavController.navigateToSellerProfile(navOptions: NavOptions? = null) {
    this.navigate(sellerProfileGraph, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.sellerProfileGraph(
    appMainViewModel: AppMainViewModel,
    navigateToAuthentication: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = sellerProfileGraph,
        startDestination = sellerProfileRoute
    ) {
        composable(sellerProfileRoute) {
            SellerProfileRoute(
                appMainViewModel = appMainViewModel,
                navigateToAuthentication = navigateToAuthentication
            )
        }
        nestedGraphs()
    }
}