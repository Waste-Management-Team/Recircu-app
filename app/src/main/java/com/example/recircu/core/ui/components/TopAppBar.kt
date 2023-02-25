package com.example.recircu.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import com.example.recircu.R
import com.example.recircu.navigation.SellerBottomBarDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Column {
            Text(
                text = "Hi Jonah!",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.paddingFromBaseline(bottom = 14.dp)
            )
            Text(
                "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 26.sp,
                modifier = Modifier.paddingFromBaseline(bottom = 7.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Image(
                    painter = painterResource(R.drawable.location),
                    contentDescription = null,
                    modifier = Modifier.size(17.dp)
                )
                Text(
                    "Rivers State University",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF00821D)
                )
            }
        }
        FilledIconButton(
            onClick = {},
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color(0xFFE2FFE9)
            ),
            modifier = Modifier.size(45.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                modifier = Modifier
                    .width(18.dp)
                    .height(24.dp),
                contentDescription = null,
                tint = Color(0xFF00821D)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecircuTopBar(
    currentDestination: NavDestination?,
    currentBottomBarDestination: SellerBottomBarDestination?,
    scrollBehavior: TopAppBarScrollBehavior?
) {
    if (currentBottomBarDestination != null) {
        if (currentBottomBarDestination == SellerBottomBarDestination.SELLER_HOME) {
        } else {
            MediumTopAppBar(
                title = { Text(stringResource(currentBottomBarDestination.titleTextId)) }
            )
        }
    } else {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeTopBarPreview() {
    MaterialTheme {
        HomeTopAppBar(scrollBehavior = null)
    }
}