package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.godzuche.recircu.features.seller.seller_profile.SellerProfileRoute
import com.google.accompanist.navigation.animation.composable


const val sellerProfileRoute = "seller_profile_route"

fun NavController.navigateToSellerProfile(navOptions: NavOptions? = null) {
    this.navigate(sellerProfileRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.sellerProfileScreen() {
    composable(sellerProfileRoute) {
        SellerProfileRoute()
    }
}