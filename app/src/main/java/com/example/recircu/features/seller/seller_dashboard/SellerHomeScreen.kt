package com.example.recircu.features.seller.seller_dashboard

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recircu.R
import com.example.recircu.core.ui.components.HomeTopAppBar
import com.example.recircu.core.ui.icon.RecircuIcon
import com.example.recircu.core.ui.icon.RecircuIcons
import com.example.recircu.core.ui.removeWidthConstraint
import com.example.recircu.core.ui.theme.RecircuTheme
import com.example.recircu.core.ui.theme.fontFamily

@Composable
fun SellerHomeRoute(modifier: Modifier = Modifier) {
    SellerHomeScreen(modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: SellerHomeViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val res = LocalContext.current.resources
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
                wasteTypes = state.wasteTypes,
                filterBuyersAds = viewModel::filterBuyersAds
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            BuyersSection(
                modifier = Modifier.removeWidthConstraint(16.dp),
                buyerAds = state.buyerAds,
                filters = state.filter
            )
        }
    }
}

@Composable
fun BuyersSection(
    modifier: Modifier = Modifier,
    buyerAds: List<BuyerAd>,
    filters: List<WasteType>
) {
    val filteredAds =
        buyerAds.filter { buyerAd ->
            buyerAd.wasteType.titleId in filters.map { it.titleId }
        }

    val ads =
        if (filters.isEmpty()) buyerAds else filteredAds
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "Buyers",
                fontSize = 20.sp,
                lineHeight = 25.sp,
                fontFamily = fontFamily,
                modifier = Modifier.align(Alignment.CenterStart),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "See All",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF00821D),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (ads.isEmpty()) {
            EmptyBuyersAds()
        } else {
            BuyersAds(
                buyerAds = ads
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BuyersAds(
    buyerAds: List<BuyerAd>
) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(buyerAds, key = { it.buyerId }) {
            BuyerAdItem(
                buyerAd = it,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerAdItem(
    buyerAd: BuyerAd,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = { },
        colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFE2FFE9)),
        border = BorderStroke(1.dp, Color(0xFF00801C)),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .heightIn(209.dp)
                .widthIn(135.dp)
                .padding(
                    start = 12.dp,
                    top = 16.dp,
                    end = 12.dp,
                    bottom = 32.dp
                )
        ) {
            Image(
                painter = painterResource(R.drawable.ellipse),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(54.dp))
            Text(
                stringResource(buyerAd.wasteType.titleId),
                fontSize = 18.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamily
            )
            Text(
                buyerAd.weight.toString().plus("kg"),
                fontSize = 14.sp,
                lineHeight = 17.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight(400)
            )
        }
    }
}

@Composable
fun EmptyBuyersAds() {
    Column(
        modifier = Modifier
            .heightIn(209.dp)
            .fillMaxWidth()
            .padding(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 32.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "No buyer available",
            fontSize = 18.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = fontFamily,
            color = Color(0xFFA0A0A0)
        )
    }
}

@Composable
fun WasteFilterRow(
    modifier: Modifier = Modifier,
    wasteTypes: MutableSet<WasteType>,
    filterBuyersAds: (WasteType, Boolean) -> Unit
) {
    LazyRow(
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        items(items = wasteTypes.toList(), key = { it.titleId.hashCode() }) {
            var isSelected by rememberSaveable {
                mutableStateOf(it.isSelected)
            }
            val surfaceColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFF00821D) else Color(0xFFC1C1C1)
            )
            WasteFilterElement(
                wasteType = it,
                isSelected, surfaceColor
            ) { wasteType ->
                filterBuyersAds.invoke(wasteType, wasteType.isSelected)
                isSelected = !isSelected
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteFilterElement(
    wasteType: WasteType,
    isSelected: Boolean,
    color: Color,
    onSelect: (WasteType) -> Unit
) {
    Surface(
        selected = isSelected,
        onClick = { onSelect.invoke(wasteType) },
        shape = RoundedCornerShape(5.dp),
        color = color,
        contentColor = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .heightIn(min = 67.dp)
                .widthIn(min = 91.dp)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            when (wasteType.icon) {
                is RecircuIcon.PainterResourceIcon -> {
                    Image(painter = painterResource(wasteType.icon.id), contentDescription = null)
                }
                is RecircuIcon.ImageVectorIcon -> {
                    Image(imageVector = wasteType.icon.imageVector, contentDescription = null)
                }
            }
            Text(
                stringResource(wasteType.titleId),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                fontFamily = fontFamily,
            )
        }
    }
}

@Composable
fun AccountBalance() {
    Surface(
        color = Color(0xFF00801C),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 37.dp, end = 20.dp, bottom = 23.dp)
        ) {
            Text(
                "Balance",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "N12,000",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(19.dp))
            Surface(
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
            }
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
        SellerHomeScreen()
    }
}