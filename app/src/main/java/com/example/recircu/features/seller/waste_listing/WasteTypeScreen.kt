package com.example.recircu.features.seller.waste_listing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recircu.core.ui.icon.RecircuIcon
import com.example.recircu.core.ui.theme.fontFamily
import com.example.recircu.features.seller.seller_dashboard.WasteType

@Composable
fun WasteTypeRoute(
    modifier: Modifier = Modifier,
    navigateToWasteDetails: (WasteType) -> Unit,
) {
    WasteTypeScreen(
        modifier = modifier,
        navigateToWasteDetails = navigateToWasteDetails
    )
}

@Composable
fun WasteTypeScreen(
    modifier: Modifier = Modifier,
    navigateToWasteDetails: (WasteType) -> Unit,
) {
    val state = rememberLazyGridState()
    LazyVerticalGrid(
        state = state,
        columns = GridCells.Adaptive(166.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            WasteTypeHeading()
        }
        wasteTypesGrid(
            modifier = Modifier.layout { measurable, constraints ->
                val placeable = measurable.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth + 32.dp.roundToPx(),
                    ),
                )
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            },
            navigateToWasteDetails = navigateToWasteDetails
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun LazyGridScope.wasteTypesGrid(
    navigateToWasteDetails: (WasteType) -> Unit,
    modifier: Modifier = Modifier
) {
    val wasteTypes = listOf(
        WasteType.Plastic,
        WasteType.Metal,
        WasteType.Plastic,
        WasteType.Others
    )
    items(items = wasteTypes) { wasteType ->
        Surface(
            onClick = { navigateToWasteDetails.invoke(wasteType) },
            shape = RoundedCornerShape(5.dp),
            color = Color(0xFF00821D),
            contentColor = Color.White,
            modifier = Modifier.padding(bottom = 14.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .heightIn(min = 210.dp)
                    .fillMaxWidth()
            ) {
                when (wasteType.icon) {
                    is RecircuIcon.PainterResourceIcon -> {
                        Image(
                            painter = painterResource(wasteType.icon.id),
                            contentDescription = null,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                    is RecircuIcon.ImageVectorIcon -> {
                        Image(
                            imageVector = wasteType.icon.imageVector,
                            contentDescription = null,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }
                Text(
                    stringResource(wasteType.titleId),
                    fontSize = 24.sp,
                    fontWeight = FontWeight(600),
                    lineHeight = 29.sp,
                    fontFamily = fontFamily,
                    modifier = Modifier.paddingFromBaseline(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun WasteTypeHeading() {
    Column {
//        Spacer(modifier = Modifier.height(80.dp))
        Text(
            "Waste type",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.paddingFromBaseline(bottom = 8.dp),
        )
        Text(
            "What type of waste do you wan to dispose?",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WasteTypePreview() {
    MaterialTheme {
        WasteTypeScreen{}
    }
}