package com.godzuche.recircu.feature.seller.seller_explore.presentation

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godzuche.recircu.R
import com.godzuche.recircu.core.designsystem.theme.fontFamily
import com.godzuche.recircu.core.ui.removeWidthConstraint
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.SellerHomeUiState
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.SellerHomeViewModel
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.WasteType

@Composable
fun SellerExploreRoute(
    homeViewModel: SellerHomeViewModel = hiltViewModel()
) {
    val state by homeViewModel.state.collectAsStateWithLifecycle()
    SellerExploreScreen(
        uiState = state
    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun SellerExploreScreen(
    uiState: SellerHomeUiState,
    modifier: Modifier = Modifier
) {
    val lazyGridState = rememberLazyGridState()
    var exploreTab by rememberSaveable {
        mutableStateOf(ExploreTab.EVENT)
    }
    val spotlightsLazyRowState = rememberLazyListState()
    val infiniteList = spotlights.toMutableStateList()
    val snappingLayout =
        remember(spotlightsLazyRowState) {
            SnapLayoutInfoProvider(spotlightsLazyRowState)
        }
    val flingBehavior = rememberSnapFlingBehavior(snappingLayout)
    val wasteTypesLazyListState = rememberLazyListState()

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
            LazyRow(
                state = spotlightsLazyRowState,
                modifier = Modifier
                    .fillMaxWidth()
                    .removeWidthConstraint(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
                flingBehavior = flingBehavior
            ) {
                itemsIndexed(
                    items = infiniteList,
                    key = { index, item ->
                        item.title
                    }
                ) { index, item ->
                    /*val itemIndex = when (index) {
                        0 -> spotlights.size - 1
                        spotlights.size + 1 -> 0
                        else -> {
                            index - 1
                        }
                    }*/
                    /*val realIndex = if (index >= spotlights.size) {
                        index - spotlights.size
                    } else {
                        index
                    }*/
                    /*when (index) {
                        0 -> {
                            Log.d("Spotlight", "index = 0")
                            val lastItem = infiniteList.last()
                            infiniteList.add(0, lastItem)
                            infiniteList.removeLast()
                        }
                        infiniteList.size - 1 -> {
                            Log.d("Spotlight", "index = last")
                            val firstItem = infiniteList.first()
                            infiniteList.removeFirst()
                            infiniteList.add(firstItem)
                        }
                    }*/
                    Spotlights(
                        spotlight = item,
                        spotlights = spotlights
                    )
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            LazyRow(
                state = wasteTypesLazyListState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
                flingBehavior = rememberSnapFlingBehavior(lazyListState = wasteTypesLazyListState),
                modifier = Modifier
                    .fillMaxWidth()
                    .removeWidthConstraint(16.dp)
            ) {
                items(items = uiState.wasteTypes.toList(), key = { it.titleId.hashCode() }) {
                    WasteLiveValues(wasteType = it, onSelect = {})
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            ExploreTabRow(
                selectedTab = exploreTab,
                onTabSelected = {
                    exploreTab = it
                }
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            AnimatedContent(targetState = exploreTab) {
                val msg = when (exploreTab) {
                    ExploreTab.EVENT -> {
                        "Events will appear here"
                    }

                    ExploreTab.DISCOVER -> {
                        "Posts including news will appear here"
                    }

                    ExploreTab.LEARN -> {
                        "Educational section"
                    }
                }
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun WasteLiveValues(
    wasteType: WasteType,
    onSelect: (WasteType) -> Unit
) {
    val value = when {
        wasteType.valueChange > 0 -> {
            Triple(
                "N" + wasteType.value.toString(),
                "+${wasteType.valueChange}%",
                MaterialTheme.colorScheme.primary
            )
        }

        wasteType.valueChange < 0 -> {
            Triple(
                "N" + wasteType.value.toString(),
                "${wasteType.valueChange}0%",
                Color.Red
            )
        }

        else -> {
            Triple(
                "N" + wasteType.value.toString(),
                wasteType.valueChange.toString(),
                MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    Surface(
        onClick = { onSelect.invoke(wasteType) },
        shape = RoundedCornerShape(5.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                stringResource(wasteType.titleId),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                fontFamily = fontFamily,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                value.first,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                fontFamily = fontFamily,
            )
            Text(
                value.second,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                fontFamily = fontFamily,
                color = value.third
            )
        }
    }
}

@Composable
private fun ExploreTabIndicator(
    tabPositions: List<TabPosition>,
    currentExploreTab: ExploreTab,
) {
    val transition = updateTransition(
        currentExploreTab,
        label = "Tab indicator"
    )


    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                // Indicator moves to the right.
                // Low stiffness spring for the left edge so it moves slower than the right edge.
                spring(stiffness = Spring.StiffnessVeryLow)
            } else {
                // Indicator moves to the left.
                // Medium stiffness spring for the left edge so it moves faster than the right edge.
                spring(stiffness = Spring.StiffnessMedium)
            }
        },
        label = "Indicator left"
    ) { exploreTab ->
        tabPositions[exploreTab.ordinal].left
    }
    val indicatorRight by transition.animateDp(
        transitionSpec = {
            if (initialState > targetState) {
                // Indicator moves to the left.
                // Low stiffness spring for the right edge so it moves slower than the left edge.
                spring(stiffness = Spring.StiffnessVeryLow)
            } else {
                // Indicator moves to the right
                // Medium stiffness spring for the right edge so it moves faster than the left edge.
                spring(stiffness = Spring.StiffnessMedium)
            }
        },
        label = "Indicator right"
    ) { exploreTab ->
        tabPositions[exploreTab.ordinal].right
    }
    /*    val color by transition.animateColor(
            label = "Border color"
        ) { page ->
            if (page == TabPage.Home) Purple700 else Green800
        }*/
    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .padding(4.dp)
            .fillMaxSize()
            .border(
                BorderStroke(2.dp, /*color*/ MaterialTheme.colorScheme.primaryContainer),
                RoundedCornerShape(4.dp)
            )
//            .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
    )
}

@Composable
fun ExploreTabRow(
    selectedTab: ExploreTab,
    onTabSelected: (ExploreTab) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        indicator = {
            ExploreTabIndicator(
                tabPositions = it,
                currentExploreTab = selectedTab
            )
        }
    ) {
        val tabs =
            ExploreTab.values()

        tabs.forEach {
            Tab(
                selected = it == selectedTab,
                onClick = {
                    onTabSelected(it)
                },
                text = {
                    Text(text = it.name.uppercase())
                }
            )
        }
    }
}

enum class ExploreTab {
    EVENT,
    DISCOVER,
    LEARN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Spotlights(
    spotlight: Spotlight,
    spotlights: List<Spotlight>
) {
    val width =
        LocalConfiguration.current.screenWidthDp.dp - 32.dp - 12.dp // remove extra 8 Dp so the next card is partly visible
    val itemPosition = spotlights.indexOf(spotlight) + 1
    val numberOfSpotlights = spotlights.size

    Card(
        onClick = {},
    ) {
        Box(
            modifier = Modifier
                .width(width = width)
                .height(220.dp)
        ) {
            Image(
                painter = painterResource(spotlight.backgroundImage),
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize(),
                contentScale = ContentScale.Crop
            )
            SpotlightDescription(
                spotlight = spotlight,
                cardWidth = width
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface.copy(0.5f)
            ) {
                Box(
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = "$itemPosition/$numberOfSpotlights",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier
//                        .align(Alignment.BottomEnd)
//                        .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BoxScope.SpotlightDescription(
    spotlight: Spotlight,
    cardWidth: Dp
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .align(Alignment.TopStart)
            .width(width = (cardWidth / 2 + 16.dp))
            .padding(16.dp)
    ) {
        if (spotlight.isNew) {
            Text(
                text = "NEW",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
        Text(
            text = spotlight.title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
        Text(
            text = spotlight.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            overflow = TextOverflow.Ellipsis
        )
    }
}

data class Spotlight(
    val isNew: Boolean = true,
    val title: String,
    val description: String,
    @DrawableRes val backgroundImage: Int = R.drawable.landfill
)

val spotlights = listOf(
    Spotlight(
        title = "Project 500kg",
        description = "The goal is to recycle 500kg of plastic. Join now!"
    ),
    Spotlight(
        title = "Green 2.0",
        description = "Recycle up to 50kg of waste and get 10 Recircoins! :)"
    ),
)