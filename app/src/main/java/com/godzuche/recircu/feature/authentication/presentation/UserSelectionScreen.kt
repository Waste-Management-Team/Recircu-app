package com.godzuche.recircu.feature.authentication.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.godzuche.recircu.core.designsystem.components.RecircuButton

@Composable
fun UserSelectionRoute(
    onSellerClick: () -> Unit
) {
    UserSelectionScreen(
        onSellerClick = onSellerClick
    )
}

@Composable
fun UserSelectionScreen(
    onSellerClick: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.CenterVertically,
        ),
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        ),
        contentPadding = PaddingValues(16.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "Sign in as:",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(12.dp))
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            RecircuButton(
                onClick = { },
                label = "Buyer",
                modifier = Modifier.fillMaxWidth()
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "OR",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            RecircuButton(
                onClick = onSellerClick,
                label = "Seller",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}