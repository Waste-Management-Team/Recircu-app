package com.example.recircu.features.seller.seller_dashboard

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recircu.R
import com.example.recircu.core.ui.components.HomeTopAppBar
import com.example.recircu.core.ui.icon.RecircuIcon
import com.example.recircu.core.ui.icon.RecircuIcons
import com.example.recircu.core.ui.removeWidthConstraint
import com.example.recircu.core.ui.theme.RecircuTheme

@Composable
fun SellerHomeRoute(
    modifier: Modifier = Modifier,
    viewModel: SellerHomeViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    SellerHomeScreen(
        userState = userState,
        uiState = state,
        filterBuyersAds = viewModel::filterBuyersAds,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerHomeScreen(
    userState: UserState,
    uiState: SellerHomeUiState,
    filterBuyersAds: (WasteType, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            HomeTopAppBar(scrollBehavior = null, userState = userState)
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            AccountBalance()
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            WasteFilterRow(
                modifier = Modifier.removeWidthConstraint(16.dp),
                wasteTypes = uiState.wasteTypes,
                filterBuyersAds = filterBuyersAds
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            BuyerAdsSection(
                modifier = Modifier.removeWidthConstraint(16.dp),
                buyerAds = uiState.buyerAds,
                filters = uiState.filter
            )
        }
    }
}

sealed class WasteType(
    val icon: RecircuIcon,
    @StringRes val titleId: Int,
    var isSelected: Boolean = false
) {
    object Plastic : WasteType(
        icon = RecircuIcon.PainterResourceIcon(RecircuIcons.Plastic),
        titleId = R.string.plastic
    )

    object Metal : WasteType(
        icon = RecircuIcon.PainterResourceIcon(RecircuIcons.Metal),
        titleId = R.string.metal
    )

    object Paper : WasteType(
        icon = RecircuIcon.PainterResourceIcon(RecircuIcons.Metal),
        titleId = R.string.paper
    )

    object Glass : WasteType(
        icon = RecircuIcon.PainterResourceIcon(RecircuIcons.Metal),
        titleId = R.string.glass
    )

    object Others : WasteType(
        icon = RecircuIcon.PainterResourceIcon(RecircuIcons.Metal),
        titleId = R.string.others
    )
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    RecircuTheme {
        SellerHomeScreen(uiState = SellerHomeUiState(),
            userState = UserState.Success(user = User("", "Jonah", "")),
            filterBuyersAds = { a, b -> }
        )
    }
}