package com.example.recircu.features.seller.waste_listing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recircu.R
import com.example.recircu.core.ui.components.DetailsTextField
import com.example.recircu.core.ui.components.RecircuButton

@Composable
fun WasteDetailsRoute() {
    WasteDetailsScreen(modifier = Modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteDetailsScreen(modifier: Modifier = Modifier) {
    var locationInput by remember {
        mutableStateOf("")
    }
    var estimatedQuantityInput by remember {
        mutableStateOf("")
    }
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        item {
            DetailsTextField(
                label = "Location",
                value = locationInput,
                onTextChange = { locationInput = it }
            )
        }
        item { Spacer(modifier = Modifier.height(14.dp)) }
        item {
            DetailsTextField(
                label = "Estimated Quantity",
                value = estimatedQuantityInput,
                onTextChange = { estimatedQuantityInput = it }
            )
        }
        item {
            Text(
                text = buildAnnotatedString {
                    append("Price: ")
                    addStyle(
                        SpanStyle(
                            color = Color(0xFFA0A0A0)
                        ),
                        0,
                        "Price: ".length,
                    )
                    pushStyle(
                        SpanStyle(color = Color(0xFF00821D))
                    )
                    append("N2,000")
                    toAnnotatedString()
                },
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End)
            )
        }
        item {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Color(0xFF00801C)))
                    .padding(vertical = 36.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.upload_icon),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF00801C)
                )
                Spacer(modifier = Modifier.height(24.dp))
                RecircuButton(
                    onClick = {},
                    modifier = Modifier.width(197.dp),
                    label = "Upload Image"
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {
            RecircuButton(
                onClick = { },
                label = "Done",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WasteDetailsPreview() {
    MaterialTheme { WasteDetailsScreen() }
}