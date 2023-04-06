package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.godzuche.recircu.feature.authentication.SellerSignUpRoute
import com.google.accompanist.navigation.animation.composable

const val sellerSignUpRoute = "seller_sign_up_route"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.sellerSignUpScreen() {
    composable(
        route = sellerSignUpRoute
    ) {
        SellerSignUpRoute()
    }
}