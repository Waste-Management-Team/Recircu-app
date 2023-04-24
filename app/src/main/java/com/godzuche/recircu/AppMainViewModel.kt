package com.godzuche.recircu

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.recircu.core.data.repository.UserDataRepository
import com.godzuche.recircu.core.location.LocationClient
import com.godzuche.recircu.core.location.LocationErrorType
import com.godzuche.recircu.core.location.LocationResult
import com.godzuche.recircu.core.model.UserPreferenceData
import com.godzuche.recircu.core.util.hasLocationPermission
import com.godzuche.recircu.feature.authentication.navigation.authGraphRoute
import com.godzuche.recircu.feature.google_maps.presentation.DialogState
import com.godzuche.recircu.navigation.gettingStartedRoute
import com.godzuche.recircu.navigation.sellerHomeGraphRoute
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppMainViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val locationClient: LocationClient,
    private val app: Application
) : AndroidViewModel(app) {

    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> get() = _dialogState.asStateFlow()

    private val _lastLocation = MutableStateFlow<Location?>(null)

    private val _userData = userDataRepository.userPreferenceData

    private val _isLocationPermissionGranted = MutableStateFlow(true)
    val isLocationPermissionEnabled get() = _isLocationPermissionGranted.asStateFlow()

    /*    .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )*/

    val uiState: StateFlow<MainActivityUiState> =
        combine(
            _lastLocation,
            _userData,
            _isLocationPermissionGranted
        ) { location, userData, isLocationPermissionGranted ->
            MainActivityUiState.Success(
                userPreferenceData = userData,
                lastLocation = location,
                isLocationPermissionGranted = isLocationPermissionGranted
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainActivityUiState.Loading
        )

//    private val _uiState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Loading)
    //    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userPreferenceData.map {
//        MainActivityUiState.Success(it)
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000),
//        initialValue = MainActivityUiState.Loading
//    )

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    init {
        getLastLocation()
    }

    fun saveOnboardingState(isCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.setIsOnboardingCompleted(isOnboardingCompleted = isCompleted)
        }
    }

    fun dismissPermissionDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    // from the activity result
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    fun setDialogState(shouldShow: Boolean, dialog: RecircuDialog? = null) {
        Log.d("Location", "setShow dialog called shouldShow = $shouldShow")
        Log.d("SignOut", "setShow dialog called shouldShow = $shouldShow")
        _dialogState.update {
            it.copy(
                shouldShow = shouldShow,
                dialog = dialog
            )
        }
    }

    fun getLastLocation() {
        _isLocationPermissionGranted.update {
            app.hasLocationPermission()
        }
        viewModelScope.launch(Dispatchers.IO) {
            locationClient.getLocation()
                .collect { result ->
                    when (result) {
                        is LocationResult.Success -> {
                            Log.d("Location", "Success ${result.location.latitude}")
                            _lastLocation.update {
                                result.location
                            }
                            /*_state.update {
                                it.copy(
                                    properties = it.properties.copy(isMyLocationEnabled = _lastLocation.value != null)
                                )
                            }*/
                        }
                        is LocationResult.Error -> {
//                            Log.d("Location", "Error")
                            when (result.errorType) {
                                LocationErrorType.DISABLED_GPS -> {
                                    _dialogState.update {
                                        it.copy(
                                            shouldShow = true,
                                            dialog = GpsDisabledDialog()
                                        )
                                    }
                                }
                                LocationErrorType.MISSING_PERMISSION -> {
                                    /*_state.update {
                                        it.copy(
                                            isLocationPermissionEnabled = false
                                        )
                                    }*/
                                    _isLocationPermissionGranted.update {
                                        false
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(
        val userPreferenceData: UserPreferenceData,
        val lastLocation: Location?,
        val isLocationPermissionGranted: Boolean,
//        val isLocationPermissionEnabled: Boolean = true
    ) : MainActivityUiState {
        fun getStartDestination(auth: FirebaseAuth): String {
            val isUserSignedIn = auth.currentUser != null
            return when {
                userPreferenceData.isOnboardingCompleted && isUserSignedIn -> sellerHomeGraphRoute
                userPreferenceData.isOnboardingCompleted && !isUserSignedIn -> authGraphRoute
                else -> gettingStartedRoute
            }
        }
    }
}