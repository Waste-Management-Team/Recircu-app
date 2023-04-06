package com.godzuche.recircu.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.godzuche.recircu.feature.authentication.UserSelectionRoute
import com.google.accompanist.navigation.animation.composable

const val userSelectionRoute = "user_selection_route"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.userSelectionScreen(
    onSellerClick: () -> Unit
){
    composable(
        route = userSelectionRoute,
    ) {
        UserSelectionRoute(
            onSellerClick = onSellerClick
        )
    }
}