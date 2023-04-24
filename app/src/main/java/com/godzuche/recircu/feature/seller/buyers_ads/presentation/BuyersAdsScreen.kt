package com.godzuche.recircu.feature.seller.buyers_ads

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godzuche.recircu.AppMainViewModel
import com.godzuche.recircu.feature.google_maps.presentation.MapsRoute
import com.godzuche.recircu.feature.google_maps.presentation.MapsViewModel

@Composable
fun BuyersAdsRoute(
    appMainViewModel: AppMainViewModel,
    requestFineLocationPermission: () -> Unit,
    navigateUp: () -> Unit,
    mapsViewModel: MapsViewModel = hiltViewModel()
//    viewModel: BuyersAdsViewModel = hiltViewModel()
) {
//    val state by viewModel.state.collectAsStateWithLifecycle()
//    val isMapView by viewModel.isMapView.collectAsStateWithLifecycle()
//    Log.d("BuyersAds C", state.view.name)

    BuyersAdsScreen(
        onBackPress = navigateUp,
        requestFineLocationPermission = requestFineLocationPermission,
        toggleMapView = {
            if (it) {
                Log.d("Location", "toggle map view")
//                mapsViewModel.getLastLocation()
                appMainViewModel.getLastLocation()
            } else Unit
        }
    )
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class
)
@Composable
fun BuyersAdsScreen(
//    state: BuyersAdsState,
//    isMapView: Boolean,
    onBackPress: () -> Unit,
    toggleMapView: (Boolean) -> Unit,
    requestFineLocationPermission: () -> Unit,
    modifier: Modifier = Modifier,
    buyersAdsViewModel: BuyersAdsViewModel = hiltViewModel()
) {
    val lazyGridState = rememberLazyGridState()
    val state by buyersAdsViewModel.state.collectAsStateWithLifecycle()

//    Log.d("BuyersAds", state.view.toString())

    var query by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(false)
    }

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
                    onClick = {
                        /*if (buyersAdsState.view == BuyersAdsView.LIST) {
                            buyersAdsViewModel.setViewType(isMapView = true)
                        } else {
                            buyersAdsViewModel.setViewType(isMapView = false)
                        }*/
                        buyersAdsViewModel.setViewType(!state.isMapView)
                        Log.d("BuyersAds TB", state.view.name)
                        toggleMapView.invoke(state.isMapView)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Map,
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
            query = query,
            onQueryChange = {
                query = it
                active = it.isNotEmpty()
            },
            onSearch = {
                active = it.isNotEmpty()
                keyboardController?.hide()
            },
            active = active,
            onActiveChange = {
            },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            },
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear",
                    modifier = Modifier.clickable {
                        query = ""
                        active = query.isNotEmpty()
                    }
                )
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
//        when (state.view) {
        /*BuyersAdsView.LIST -> {
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
                navigateBack = {}
            )
        }*/
        if (state.isMapView) {
            MapsRoute(
                navigateBack = {},
                requestFineLocationPermission = requestFineLocationPermission
            )
        } else {
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
//        }
    }
}
