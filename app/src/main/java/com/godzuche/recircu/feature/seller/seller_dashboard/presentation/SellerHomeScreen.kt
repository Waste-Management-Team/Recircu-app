package com.godzuche.recircu.feature.seller.seller_dashboard.presentation

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godzuche.recircu.R
import com.godzuche.recircu.core.designsystem.components.HomeTopAppBar
import com.godzuche.recircu.core.designsystem.icon.RecircuIcon
import com.godzuche.recircu.core.designsystem.icon.RecircuIcons
import com.godzuche.recircu.core.designsystem.theme.RecircuTheme
import com.godzuche.recircu.core.designsystem.theme.fontFamily
import com.godzuche.recircu.core.firebase.GoogleAuthUiClientImpl
import com.godzuche.recircu.core.ui.removeWidthConstraint
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SellerHomeRoute(
    modifier: Modifier = Modifier,
    navigateToBuyer: (BuyerAd) -> Unit,
    navigateToBuyersAds: () -> Unit,
    viewModel: SellerHomeViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val googleAuthUiClientImpl = GoogleAuthUiClientImpl(
        auth = Firebase.auth,
        oneTapClient = Identity.getSignInClient(context)
    )

    viewModel.onGetCurrentUser(googleAuthUiClientImpl.getSignedInUser())

    SellerHomeScreen(
        userState = userState,
        uiState = state,
        filterBuyersAds = viewModel::filterBuyersAds,
        navigateToBuyer = navigateToBuyer,
        navigateToBuyersAds = navigateToBuyersAds,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerHomeScreen(
    userState: UserState,
    uiState: SellerHomeUiState,
    filterBuyersAds: (WasteType, Boolean) -> Unit,
    navigateToBuyer: (BuyerAd) -> Unit,
    navigateToBuyersAds: () -> Unit,
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
            HomeSection(
                title = stringResource(R.string.green_tip_of_the_day)
            ) {
                TipOfTheDay()
            }
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
                filters = uiState.filter,
                navigateToBuyerDetail = navigateToBuyer,
                onSeeAllClicked = navigateToBuyersAds
            )
        }
    }
}

@Composable
fun TipOfTheDay() {
    var isTipExpanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Do not dispose plastics by burning as they produce harmful products and " +
                        "also emit CFCs which depletes the ozone",
                overflow = TextOverflow.Ellipsis,
                maxLines = if (isTipExpanded) Int.MAX_VALUE else 2,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
            )
            IconButton(
                onClick = {
                    isTipExpanded = !isTipExpanded
                }
            ) {
                Icon(
                    imageVector = if (isTipExpanded) {
                        RecircuIcons.ExpandLess
                    } else {
                        RecircuIcons.ExpandMore
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun HomeSection(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClicked: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
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
                title,
                fontSize = 20.sp,
                lineHeight = 25.sp,
                fontFamily = fontFamily,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            if (actionText != null && onActionClicked != null) {
                Text(
                    actionText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
//                        .padding(4.dp)
                        .clickable {
                            onActionClicked.invoke()
                        }
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

sealed class WasteType(
    val icon: RecircuIcon,
    @StringRes val titleId: Int,
    var isSelected: Boolean = false,
    var value: Double,
    var valueChange: Double
) {
    object Plastic : WasteType(
        icon = RecircuIcon.PainterResourceIcon(RecircuIcons.Plastic),
        titleId = R.string.plastic,
        value = 40.00,
        valueChange = +5.13
    )

    object Metal : WasteType(
        icon = RecircuIcon.PainterResourceIcon(RecircuIcons.Metal),
        titleId = R.string.metal,
        value = 200.00,
        valueChange = +2.16
    )

    object Paper : WasteType(
        icon = RecircuIcon.PainterResourceIcon(RecircuIcons.Metal),
        titleId = R.string.paper,
        value = 0.57,
        valueChange = +0.74
    )

    object Glass : WasteType(
        icon = RecircuIcon.PainterResourceIcon(RecircuIcons.Metal),
        titleId = R.string.glass,
        value = 120.23,
        valueChange = -0.24
    )

    object Others : WasteType(
        icon = RecircuIcon.PainterResourceIcon(RecircuIcons.Metal),
        titleId = R.string.others,
        value = 0.00,
        valueChange = 0.00
    )
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    RecircuTheme {
        SellerHomeScreen(uiState = SellerHomeUiState(),
            userState = UserState.Success(user = User("God'swill", "", "", "")),
            filterBuyersAds = { a, b -> },
            navigateToBuyer = {},
            navigateToBuyersAds = {}
        )
    }
}