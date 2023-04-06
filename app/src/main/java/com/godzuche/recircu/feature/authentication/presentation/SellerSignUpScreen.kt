package com.godzuche.recircu.feature.authentication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SellerSignUpRoute() {
    SignUpScreen()
}

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize())
}