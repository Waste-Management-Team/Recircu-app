package com.godzuche.recircu.feature.seller.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godzuche.recircu.R
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.SellerHomeUiState
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.SellerHomeViewModel
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.WasteType

@Composable
fun WasteTypeRoute(
    navigateToWasteDetails: (WasteType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SellerHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    WasteTypeScreen(
        uiState = state,
        navigateToWasteDetails = navigateToWasteDetails,
        modifier = modifier
    )
}

@Composable
fun WasteTypeScreen(
    uiState: SellerHomeUiState,
    navigateToWasteDetails: (WasteType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyGridState = rememberLazyGridState()
    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Adaptive(166.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            WasteTypeHeading()
        }
        wasteTypesGrid(
            wasteTypes = uiState.wasteTypes,
            navigateToWasteDetails = navigateToWasteDetails
        )
    }
}

fun LazyGridScope.wasteTypesGrid(
    wasteTypes: MutableSet<WasteType>,
    navigateToWasteDetails: (WasteType) -> Unit,
    modifier: Modifier = Modifier
) {
    items(items = wasteTypes.toList()) { wasteType ->
        WasteTypesGridItem(
            wasteType = wasteType,
            navigateToWasteDetails = navigateToWasteDetails
        )
    }
}

@Composable
fun WasteTypeHeading() {
    Column {
        Text(
            stringResource(R.string.waste_type),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.paddingFromBaseline(bottom = 8.dp),
        )
        Text(
            stringResource(R.string.what_type_of_waste_to_dispose),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WasteTypePreview() {
    MaterialTheme {
        WasteTypeScreen(
            navigateToWasteDetails = {},
            uiState = SellerHomeUiState()
        )
    }
}