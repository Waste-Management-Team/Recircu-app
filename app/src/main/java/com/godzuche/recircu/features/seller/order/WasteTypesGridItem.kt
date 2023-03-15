package com.godzuche.recircu.features.seller.order

import androidx.compose.foundation.layout.*
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
import com.godzuche.recircu.core.ui.icon.RecircuIcon
import com.godzuche.recircu.core.ui.theme.fontFamily
import com.godzuche.recircu.features.seller.seller_dashboard.WasteType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteTypesGridItem(
    wasteType: WasteType,
    navigateToWasteDetails: (WasteType) -> Unit
) {
    Surface(
        onClick = { navigateToWasteDetails.invoke(wasteType) },
        shape = RoundedCornerShape(5.dp),
        color = MaterialTheme.colorScheme.primary,
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
                    Icon(
                        painter = painterResource(wasteType.icon.id),
                        contentDescription = null,
                        modifier = Modifier.size(54.dp)
                    )
                }
                is RecircuIcon.ImageVectorIcon -> {
                    Icon(
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