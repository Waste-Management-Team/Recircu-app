package com.godzuche.recircu.features.seller.seller_profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.godzuche.recircu.core.ui.components.RecircuAnimatedCircle
import com.godzuche.recircu.features.seller.seller_dashboard.HomeSection
import com.godzuche.recircu.features.seller.seller_dashboard.WasteType

@Composable
fun SellerProfileRoute(modifier: Modifier = Modifier) {
    SellerProfileScreen(
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun SellerProfileScreen(
    modifier: Modifier = Modifier
) {
    val lazyGridState = rememberLazyGridState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        state = lazyGridState,
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            ProfileHeader()
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Balance()
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            HomeSection(
                title = "Statistics"
            ) {
                FlowRow(
                    maxItemsInEachRow = 2,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Rank(
                        modifier = Modifier.weight(1f),
                        screenWidth = screenWidth
                    )
                    Xp(
                        modifier = Modifier.weight(1f),
                        screenWidth = screenWidth
                    )
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            val totalWasteRecycled = 20
            val recycledWasteStats = listOf<Triple<WasteType, Float, Color>>(
                Triple(WasteType.Plastic, 0.35f, Color.Cyan),
                Triple(WasteType.Metal, 0.2f, Color.Blue),
                Triple(WasteType.Paper, 0.25f, Color.Yellow),
                Triple(WasteType.Glass, 0.07f, Color.Green),
                Triple(WasteType.Others, 0.13f, Color.Gray),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedCircularProportion(
                    items = recycledWasteStats,
                    screenWidth = screenWidth,
                    modifier = Modifier.weight(1f)
                )
                WasteProportionsBreakdown(
                    recycledWasteStats = recycledWasteStats,
                    totalWasteRecycled = totalWasteRecycled,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            HomeSection(
                title = "Achievements"
            ) {

            }
        }
    }
}

@Composable
fun WasteProportionsBreakdown(
    recycledWasteStats: List<Triple<WasteType, Float, Color>>,
    totalWasteRecycled: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        recycledWasteStats.forEach {
            val proportion = it.second
            val amount = proportion * totalWasteRecycled
            val percent = proportion * 100
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(18.dp)
                            .background(it.third)
                    )
                    Text(
                        text = stringResource(id = it.first.titleId),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(
                    text = amount.toString() + "kg",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "$percent %",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
fun AnimatedCircularProportion(
    items: List<Triple<WasteType, Float, Color>>,
    screenWidth: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        RecircuAnimatedCircle(
            items = items,
            modifier = Modifier.size(((screenWidth - 32.dp - 8.dp) / 2))
        )
        Text(
            text = "20kg",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun Xp(
    screenWidth: Dp,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(0.6f)
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Bolt,
            contentDescription = null,
            tint = Color.Yellow
        )
        Column {
            Text(
                text = "105",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Total XP",
                color = MaterialTheme.colorScheme.outline.copy(0.6f),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = 0.6f,
                modifier = Modifier.width(
                    ((screenWidth - 40.dp) / 2) - 16.dp - 32.dp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "105/150",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun Rank(
    screenWidth: Dp,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Diamond,
            contentDescription = null,
            tint = Color.Cyan
        )
        Column {
            Text(
                text = "Diamond",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Current Rank",
                color = MaterialTheme.colorScheme.outline.copy(0.6f),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = 0.6f,
                modifier = Modifier.width(
                    ((screenWidth - 40.dp) / 2) - 16.dp - 32.dp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "60%",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun Balance() {
    Surface(
        color = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .height(72.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "N 12,000",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Balance",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Divider(
                modifier = Modifier
                    .height(56.dp)
                    .width(1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "105",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Points",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Divider(
                modifier = Modifier
                    .height(56.dp)
                    .width(1.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "59 Rec",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Recircoin",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ProfileImage(modifier)
        Spacer(modifier = Modifier.height(16.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
            Text(
                text = "God'swill Jonathan",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = "godswill.jonathan@ust.edu.ng",
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            Text(
                text = "0 Following",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { }
            )
            Text(
                text = "0 Followers",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { }
            )
        }
    }
}

@Composable
private fun ProfileImage(modifier: Modifier) {
    val context = LocalContext.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
/*        *//*     imageUri?.let {
                 if (Build.VERSION.SDK_INT < 28) {
                     bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                 } else {
                     val source = ImageDecoder.createSource(context.contentResolver, it)
                     bitmap.value = ImageDecoder.decodeBitmap(source)
                 }
                 *//**//*bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(156.dp)
                        .clip(CircleShape)
                )
            }*//**//*
        }*/
        val imageRequest = ImageRequest.Builder(context)
            .data(/*imageUri ?: */com.godzuche.recircu.R.drawable.avatar_12)
            .size(Size.ORIGINAL)
            .crossfade(true)
            .build()
        /*val painter = rememberAsyncImagePainter(
            model = model,
            contentScale = ContentScale.Crop
        )*/
        AsyncImage(
            model = imageRequest,
            placeholder = painterResource(id = com.godzuche.recircu.R.drawable.ic_launcher_foreground),
            contentDescription = "Profile picture",
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.High,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
    }

}