package com.godzuche.recircu.features.seller.seller_explore

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.godzuche.recircu.R

@Composable
fun SellerExploreRoute(modifier: Modifier = Modifier) {
    SellerExploreScreen()
}

@Preview(showBackground = true)
@Composable
fun SellerExploreScreen(
    modifier: Modifier = Modifier
) {
    val lazyGridState = rememberLazyGridState()

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
                state = rememberLazyListState(),
                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = spotlights,
                    key = { it.title }
                ) {
                    Spotlights(
                        spotlight = it,
                        spotlights = spotlights
                    )
                }
            }
        }
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            var exploreTab by remember {
                mutableStateOf(ExploreTab.EVENT)
            }
            ExploreTabRow(
                selectedTab = exploreTab,
                onTabSelected = {
                    exploreTab = it
                }
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
    val width = LocalConfiguration.current.screenWidthDp.dp - 32.dp
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
            Text(
                text = "$itemPosition/$numberOfSpotlights",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
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