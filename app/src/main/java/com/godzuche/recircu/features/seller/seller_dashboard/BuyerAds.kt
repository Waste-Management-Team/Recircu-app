package com.godzuche.recircu.features.seller.seller_dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godzuche.recircu.R
import com.godzuche.recircu.core.ui.theme.fontFamily

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BuyerAdsSection(
    modifier: Modifier = Modifier,
    buyerAds: List<BuyerAd>,
    filters: List<WasteType>,
    navigateToBuyerDetail: (BuyerAd) -> Unit
) {
    val filteredAds =
        buyerAds.filter { buyerAd ->
            buyerAd.wasteType.titleId in filters.map { it.titleId }
        }

    val ads =
        if (filters.isEmpty()) buyerAds else filteredAds

    HomeSection(
        title = stringResource(R.string.buyers),
        actionText = stringResource(R.string.see_all),
        onActionClicked = {},
    ) {
        AnimatedContent(targetState = ads.isEmpty()) { isEmpty ->
            if (isEmpty) {
                EmptyBuyersAds()
            } else {
                BuyersAds(
                    buyerAds = ads,
                    navigateToBuyerDetail = navigateToBuyerDetail,
                    modifier = modifier
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BuyersAds(
    buyerAds: List<BuyerAd>,
    navigateToBuyerDetail: (BuyerAd) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        items(buyerAds, key = { it.buyerId }) {
            BuyerAdItem(
                buyerAd = it,
                modifier = Modifier.animateItemPlacement(),
                onClick = { navigateToBuyerDetail.invoke(it) }
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
            stringResource(R.string.no_buyer_available),
            fontSize = 18.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = fontFamily,
            color = MaterialTheme.colorScheme.outline.copy(0.25f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerAdItem(
    buyerAd: BuyerAd,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = onClick,
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
            Icon(
                painter = painterResource(R.drawable.ellipse),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally),
                tint = MaterialTheme.colorScheme.primaryContainer
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
                stringResource(id = R.string.weight, buyerAd.weight.toString()),
                fontSize = 14.sp,
                lineHeight = 17.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight(400)
            )
        }
    }
}