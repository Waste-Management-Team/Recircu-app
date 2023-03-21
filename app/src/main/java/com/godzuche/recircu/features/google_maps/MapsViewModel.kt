package com.godzuche.recircu.features.google_maps

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.recircu.GpsDisabledDialog
import com.godzuche.recircu.RecircuDialog
import com.godzuche.recircu.data.location.LocationResult
import com.godzuche.recircu.domain.location.LocationClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val locationClient: LocationClient,
    private val placesClient: PlacesClient
) : ViewModel() {
    private val _lastLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    val lastLocation: StateFlow<Location?> get() = _lastLocation.asStateFlow()

    private val _locationAutofill = MutableStateFlow<List<PlacesAutocompleteResult>>(emptyList())
    val locationAutofill = _locationAutofill.asStateFlow()

    private val _selectedLocation = MutableStateFlow(LatLng(0.0, 0.0))
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _state: MutableStateFlow<MapsState> = MutableStateFlow(MapsState())
    val state = combine(_lastLocation, _state) { location, mapState ->
        mapState.copy(
            lastLocation = location
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MapsState()
    )

    val _dialogState = MutableStateFlow(DialogState())
    val dialogState get() = _dialogState.asStateFlow()

    private var job: Job? = null

    init {
        getLastLocation()
    }

    // should be called when the user clicks on a place in the suggestion
    fun getCordinates(result: PlacesAutocompleteResult) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(
            result.placeId,
            placeFields
        )
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                if (response != null) {
                    _selectedLocation.update {
                        response.place.latLng!!
                    }
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    fun onSearchPlaces(query: String) {
        job?.cancel()
        job = viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest
                .builder()
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    _locationAutofill.update {
                        response.autocompletePredictions.map {
                            PlacesAutocompleteResult(
                                address = it.getFullText(null).toString(),
                                placeId = it.placeId
                            )
                        }
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    println(it.cause)
                    println(it.message)
                }
        }
    }

    fun getLastLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            locationClient.getLocation()
                .collect { result ->
                    when (result) {
                        is LocationResult.Success -> {
                            Log.d("Location", "Success")
                            _lastLocation.update {
                                result.location
                            }
                            _state.update {
                                it.copy(
                                    properties = it.properties.copy(isMyLocationEnabled = _lastLocation.value != null)
                                )
                            }
                        }
                        is LocationResult.Error -> {
                            Log.d("Location", "Error")
                            _dialogState.update {
                                it.copy(
                                    shouldShow = true,
                                    dialog = GpsDisabledDialog()
                                )
                            }
                        }
                    }
                }
        }
    }

    fun setDialogState(shouldShow: Boolean, dialog: RecircuDialog? = null) {
        _dialogState.update {
            it.copy(
                shouldShow = shouldShow,
                dialog = dialog
            )
        }
    }

    fun stopPlacesSearch() {
        _locationAutofill.update {
            emptyList()
        }
    }
}

data class DialogState(
    val shouldShow: Boolean = false,
    val dialog: RecircuDialog? = null
)
