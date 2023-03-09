package com.example.recircu.features.seller.waste_type_selection

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recircu.R
import com.example.recircu.core.ui.components.DetailsTextField
import com.example.recircu.core.ui.components.LocationTextField
import com.example.recircu.core.ui.components.RecircuButton
import com.example.recircu.core.ui.icon.RecircuIcons

@Composable
fun WasteDetailsRoute(
    showScheduleBottomSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    WasteDetailsScreen(
        showScheduleBottomSheet = showScheduleBottomSheet,
        modifier = modifier
    )
}

@Composable
fun WasteDetailsScreen(
    showScheduleBottomSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            LocationTextField(
                label = stringResource(R.string.location),
                value = locationInput,
                onValueChange = { locationInput = it },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = locationInput.isNotEmpty(),
                        enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                        exit = shrinkOut(shrinkTowards = Alignment.Center) + fadeOut()
                    ) {
                        IconButton(
                            onClick = { locationInput = "" }
                        ) {
                            Icon(
                                imageVector = RecircuIcons.Clear,
                                contentDescription = null
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    autoCorrect = false
                ),
                onGetMyLocation = {},
                singleLine = true
            )
        }
        item { Spacer(modifier = Modifier.height(14.dp)) }
        item {
            DetailsTextField(
                label = stringResource(R.string.estimated_quantity),
                value = estimatedQuantityInput,
                onValueChange = { estimatedQuantityInput = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                    autoCorrect = false
                ),
                singleLine = true
            )
        }
        item {
            Price()
        }
        item {
            ImageUpload()
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {
            RecircuButton(
                onClick = showScheduleBottomSheet,
                label = stringResource(R.string.next),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ImageUpload() {
    var imageUri: Uri? by remember {
        mutableStateOf(null)
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            imageUri = it
        }
    )
    Box {
        if (imageUri != null) {
            val imageRequest = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .build()
            AsyncImage(
                model = imageRequest.data, contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary))
                .padding(vertical = 36.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.upload_icon),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            RecircuButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.width(197.dp),
                label = stringResource(R.string.upload_image)
            )
        }
    }
}

@Composable
private fun Price(amount: String = "2,000") {
    Text(
        text = buildAnnotatedString {
            append("Price: ")
            addStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f) /*Color(0xFFA0A0A0)*/
                ),
                0,
                "Price: ".length,
            )
            pushStyle(
                SpanStyle(color = MaterialTheme.colorScheme.primary)
            )
            append(amount)
            toAnnotatedString()
        },
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End)
    )
}

@Preview(showBackground = true)
@Composable
fun WasteDetailsPreview() {
    MaterialTheme { WasteDetailsScreen(showScheduleBottomSheet = {}) }
}