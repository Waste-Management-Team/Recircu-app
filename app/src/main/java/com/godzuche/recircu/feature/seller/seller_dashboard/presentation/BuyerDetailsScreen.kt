package com.godzuche.recircu.feature.seller.seller_dashboard.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.godzuche.recircu.core.designsystem.components.RecircuButton
import com.godzuche.recircu.core.designsystem.components.RecircuRatingBar
import com.godzuche.recircu.core.designsystem.icon.RecircuIcons

@Composable
fun BuyerDetailsRoute(
    onBookBuyerClick: () -> Unit
) {
    BuyerDetailsScreen(
        onBookBuyerClick = onBookBuyerClick
    )
}

@Composable
fun BuyerDetailsScreen(
    onBookBuyerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyGridState = rememberLazyGridState()
    var ratingState by remember {
        mutableStateOf(4)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            state = lazyGridState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, true),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                // Todo: pass rate star size
                BuyerDetailsHeader(
                    ratingState = ratingState,
                    onRatingChange = {
                        ratingState = it
                    }
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                BuyerAddress()
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                ActiveDays()
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                TimeSlot()
            }
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
            // Actor victor smoch hahahahahaha
        }
        RecircuButton(
            onClick = onBookBuyerClick,
            label = "Book buyer now",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun TimeSlot() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Select a preferred time",
            style = MaterialTheme.typography.titleMedium
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..30 step 7).forEach {
                item {
                    DayItem(day = it)
                }
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            listOf(9, 2, 5).forEach {
                item {
                    TimeItem(
                        time = "$it:00",
                        period = "AM"
                    )
                }
            }
        }

        /*Text(
            text = "Nearest dates: 23 March",
        )*/

    }
}

@Composable
fun TimeItem(
    time: String,
    period: String,
    modifier: Modifier = Modifier
) {
    var isSelected1 by remember {
        mutableStateOf(false)
    }
    Surface(
        onClick = {
            isSelected1 = !isSelected1
        },
        selected = isSelected1,
        color = if (isSelected1) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        shape = RoundedCornerShape(5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.WbSunny,
                contentDescription = null
            )
            Text(
                text = time,
            )
            Text(
                text = period,
            )
        }
    }
}

@Composable
fun DayItem(
    day: Int
) {
    var isSelected by remember {
        mutableStateOf(false)
    }

    val height by animateDpAsState(
        targetValue = if (isSelected) {
            24.dp
        } else {
            16.dp
        }
    )
    Surface(
        onClick = {
            isSelected = !isSelected
        },
        selected = isSelected,
        shape = RoundedCornerShape(5.dp),
//        modifier = Modifier.height(height),
        border = BorderStroke(
            width = if (isSelected) {
                2.dp
            } else 1.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = height)
        ) {
            Text(
                text = "WED",
            )
            Text(
                text = "Mar $day",
            )
        }
    }
}

@Composable
fun ActiveDays() {
    Text(
        text = buildAnnotatedString {
            append("Active Days: ")
            addStyle(
                SpanStyle(
                    fontWeight = FontWeight.SemiBold
                ),
                0,
                "Active Days: ".length,
            )
            pushStyle(
                SpanStyle(fontWeight = FontWeight.Normal)
            )
            append("Wednesdays")
            toAnnotatedString()
        },
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun BuyerAddress() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "7 Abel Jumbo Street, Mile 2 Diobu, Port Harcourt",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.medium
        ) {
            Text(
                text = "12km away",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun BuyerDetailsHeader(
    ratingState: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Image(
            painter = painterResource(RecircuIcons.Avatar12),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "Destiny Ed",
            style = MaterialTheme.typography.titleLarge
        )
        RecircuRatingBar(
            rating = ratingState,
            onRatingChange = onRatingChange,
            enabled = false
        )
    }
}