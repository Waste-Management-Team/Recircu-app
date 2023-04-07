package com.godzuche.recircu.feature.seller.order

import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.godzuche.recircu.R
import com.godzuche.recircu.RecircuBottomSheetContent
import com.godzuche.recircu.core.designsystem.components.DetailsTextField
import com.godzuche.recircu.core.designsystem.components.LocationTextField
import com.godzuche.recircu.core.designsystem.components.RecircuButton
import com.godzuche.recircu.core.designsystem.icon.RecircuIcons
import com.godzuche.recircu.feature.google_maps.PlacesAutocompleteResult
import com.godzuche.recircu.feature.google_maps.presentation.MapsViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun OrderDetailsRoute(
    showScheduleBottomSheet: (RecircuBottomSheetContent) -> Unit,
    modifier: Modifier = Modifier,
    mapsViewModel: MapsViewModel = hiltViewModel()
) {
    val lastLocation by mapsViewModel.lastLocation.collectAsStateWithLifecycle()
    val selectedLocation by mapsViewModel.selectedLocation.collectAsStateWithLifecycle()
    val locationAutofill by mapsViewModel.locationAutofill.collectAsStateWithLifecycle()
    OrderDetailsScreen(
        lastLocation = lastLocation,
        locationAutofill = locationAutofill,
        selectedLocation = selectedLocation,
        showScheduleBottomSheet = showScheduleBottomSheet,
        modifier = modifier,
        onLocationQueryChange = {
            mapsViewModel.onSearchPlaces(it)
        },
        onPlaceClick = {
            // Todo: use event class
            mapsViewModel.stopPlacesSearch()
//            mapsViewModel.getCordinates(it)
        }
    )
}

@Composable
fun OrderDetailsScreen(
    lastLocation: Location?,
    showScheduleBottomSheet: (RecircuBottomSheetContent) -> Unit,
    locationAutofill: List<PlacesAutocompleteResult>,
    selectedLocation: LatLng,
    onLocationQueryChange: (String) -> Unit,
    onPlaceClick: (PlacesAutocompleteResult) -> Unit,
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
            Column {
                LocationTextField(
                    label = stringResource(R.string.location),
                    value = locationInput,
                    onValueChange = {
                        Log.d("Places", "${locationAutofill.isEmpty()}")
                        locationInput = it
                        onLocationQueryChange.invoke(it)
                    },
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
                    onGetMyLocationFromMap = {
                        showScheduleBottomSheet.invoke(
                            RecircuBottomSheetContent.MAP
                        )
                    },
                    singleLine = true
                )

                AnimatedVisibility(
                    visible = locationAutofill.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(locationAutofill) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clickable {
                                        locationInput = it.address
                                        // Todo: Use an event class instead
                                        onPlaceClick.invoke(it)
                                    }
                            ) {

                            }
                        }
                    }
                }
            }
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
        /*      item {
                  Price()
              }*/
        item {
            ImageUpload()
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {
            RecircuButton(
                onClick = { showScheduleBottomSheet.invoke(RecircuBottomSheetContent.SCHEDULE) },
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
    MaterialTheme {
        OrderDetailsScreen(
            lastLocation = null,
            showScheduleBottomSheet = {},
            locationAutofill = emptyList(),
            selectedLocation = LatLng(0.0, 0.0),
            onPlaceClick = {},
            onLocationQueryChange = {}
        )
    }
}