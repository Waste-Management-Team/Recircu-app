package com.godzuche.recircu.feature.seller.buyers_ads.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godzuche.recircu.AppMainViewModel
import com.godzuche.recircu.core.designsystem.icon.RecircuIcons
import com.godzuche.recircu.feature.google_maps.presentation.MapsRoute

@Composable
fun BuyersAdsRoute(
    appMainViewModel: AppMainViewModel,
    requestFineLocationPermission: () -> Unit,
    navigateUp: () -> Unit,
    buyersAdsViewModel: BuyersAdsViewModel = hiltViewModel()
) {
    // Todo: remove the requestFineLocationPermission parameter and use the getLocation() function in
    //  appMainViewModel or better still create a separate function to request the permission in the viewModel
    val buyersAdsState by buyersAdsViewModel.state.collectAsStateWithLifecycle()

    BuyersAdsScreen(
        uiState = buyersAdsState,
        onBackPress = navigateUp,
        requestFineLocationPermission = requestFineLocationPermission,
        toggleAdsViewMode = {
            // Try to get last location when switching to Map view and request fine location permission if needed
            if (buyersAdsState.isMapView.not()) appMainViewModel.getLastLocation()
            buyersAdsViewModel.setViewType(buyersAdsState.isMapView.not())
        },
        onSearchQueryChange = buyersAdsViewModel::onSearchQueryChange,
        onSearch = buyersAdsViewModel::search,
        clearSearchQuery = buyersAdsViewModel::clearSearchQueryText
    )
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class
)
@Composable
fun BuyersAdsScreen(
    uiState: BuyersAdsState,
    onBackPress: () -> Unit,
    toggleAdsViewMode: () -> Unit,
    requestFineLocationPermission: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    clearSearchQuery: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyGridState = rememberLazyGridState()

    val active = uiState.searchQuery.isNotEmpty()

    val keyboardController = LocalSoftwareKeyboardController.current
    Column {
        TopAppBar(
            title = {
                /*Text(
                    stringResource(
                        destination.toTitleTextId() ?: R.string.empty_string
                    ),
                    style = MaterialTheme.typography.headlineMedium
                )*/
            },
            navigationIcon = {
                IconButton(onClick = onBackPress) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
//            scrollBehavior = scrollBehavior,
            actions = {
                IconButton(
                    onClick = toggleAdsViewMode
                ) {
                    Icon(
                        imageVector = if (uiState.isMapView) {
                            RecircuIcons.Map
                        } else {
                            RecircuIcons.List
                        },
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = null
                    )
                }
            }
        )
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = { newQueryText ->
                onSearchQueryChange.invoke(newQueryText)
            },
            onSearch = { query ->
                onSearch.invoke(query)
                keyboardController?.hide()
            },
            active = active,
            onActiveChange = {
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = uiState.searchQuery.isNotEmpty(),
                    enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                    exit = shrinkOut(shrinkTowards = Alignment.Center) + fadeOut()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear",
                        modifier = Modifier.clickable {
                            clearSearchQuery.invoke()
                        }
                    )
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
            placeholder = {
                Text(text = "Search buyers")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
//                    .navigationBarsPadding()
        ) {
        }
        Spacer(modifier = Modifier.height(8.dp))
        when (uiState.adsViewMode) {
            BuyersAdsView.LIST -> {
                LazyVerticalGrid(
                    state = lazyGridState,
                    columns = GridCells.Adaptive(160.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .then(modifier),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                    }
                }
            }
            BuyersAdsView.MAP -> {
                MapsRoute(
                    navigateBack = {},
                    requestFineLocationPermission = requestFineLocationPermission
                )
            }
        }
    }
}
