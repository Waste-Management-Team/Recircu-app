package com.godzuche.recircu.features.seller.seller_dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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

@Composable
fun BuyerAdsSection(
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
                stringResource(R.string.buyers),
                fontSize = 20.sp,
                lineHeight = 25.sp,
                fontFamily = fontFamily,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                stringResource(R.string.see_all),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
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
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerAdItem(
    buyerAd: BuyerAd,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = {
        },
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