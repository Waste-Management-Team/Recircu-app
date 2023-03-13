package com.godzuche.recircu.features.google_maps

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.recircu.data.location.LocationResult
import com.godzuche.recircu.domain.location.LocationClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val locationClient: LocationClient
) : ViewModel() {
    private val _lastLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    val lastLocation: StateFlow<Location?> get() = _lastLocation.asStateFlow()
    private val _state: MutableStateFlow<MapsState> = MutableStateFlow(MapsState())
    val state: StateFlow<MapsState> get() = _state.asStateFlow()

    init {
        getLastLocation()
    }

    fun getLastLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            locationClient.getLocation().catch {
                if (it is LocationClient.LocationException) {
                    //
                }
            }.collect { result ->
                when (result) {
                    is LocationResult.Success -> {
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
                        //use a state or event to show dialog
                    }
                    else -> {}
                }
            }
        }
    }
}