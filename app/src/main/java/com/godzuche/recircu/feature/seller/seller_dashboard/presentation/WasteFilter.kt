package com.godzuche.recircu.feature.seller.seller_dashboard.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.godzuche.recircu.core.designsystem.icon.RecircuIcon
import com.godzuche.recircu.core.designsystem.theme.fontFamily

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
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(0.2f)
            )
            WasteFilterItem(
                wasteType = it,
                isSelected, surfaceColor
            ) { wasteType ->
                filterBuyersAds.invoke(wasteType, wasteType.isSelected)
                isSelected = !isSelected
            }
        }
    }
}

@Composable
fun WasteFilterItem(
    wasteType: WasteType,
    isSelected: Boolean,
    color: Color,
    onSelect: (WasteType) -> Unit
) {
    Surface(
        selected = isSelected,
        onClick = { onSelect.invoke(wasteType) },
        shape = RoundedCornerShape(5.dp),
        color = color
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
                    Icon(
                        painter = painterResource(wasteType.icon.id),
                        contentDescription = stringResource(wasteType.titleId)
                    )
                }

                is RecircuIcon.ImageVectorIcon -> {
                    Icon(
                        imageVector = wasteType.icon.imageVector,
                        contentDescription = stringResource(wasteType.titleId)
                    )
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