package com.godzuche.recircu.features.google_maps

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.recircu.GpsDisabledDialog
import com.godzuche.recircu.RecircuDialog
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

    init {
        getLastLocation()
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
}

data class DialogState(
    val shouldShow: Boolean = false,
    val dialog: RecircuDialog? = null
)
