package com.godzuche.recircu

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.recircu.core.common.RecircuResult
import com.godzuche.recircu.core.data.repository.AuthRepository
import com.godzuche.recircu.core.data.repository.UserDataRepository
import com.godzuche.recircu.core.domain.UserData
import com.godzuche.recircu.core.location.LocationClient
import com.godzuche.recircu.core.location.LocationErrorType
import com.godzuche.recircu.core.location.LocationResult
import com.godzuche.recircu.core.model.UserPreferenceData
import com.godzuche.recircu.feature.authentication.navigation.authGraphRoute
import com.godzuche.recircu.feature.google_maps.presentation.DialogState
import com.godzuche.recircu.feature.onboarding.navigation.gettingStartedRoute
import com.godzuche.recircu.feature.seller.seller_dashboard.navigation.sellerHomeGraphRoute
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppMainViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    private val locationClient: LocationClient,
    private val authRepository: AuthRepository,
    private val app: Application
) : AndroidViewModel(app) {

    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> get() = _dialogState.asStateFlow()

    private val _lastLocation = MutableStateFlow<Location?>(null)
    val lastLocation = _lastLocation.asStateFlow()

    private val _userPreferenceData = userDataRepository.userPreferenceData

    private var _userAuthState: MutableStateFlow<UserAuthState> =
        MutableStateFlow(UserAuthState.Loading)
    val userAuthState: StateFlow<UserAuthState> get() = _userAuthState.asStateFlow()

    private val _isLocationPermissionGranted = MutableStateFlow(true)
//    val isLocationPermissionGranted get() = _isLocationPermissionGranted.asStateFlow()

    val uiState: StateFlow<MainActivityUiState> =
        combine(
            _userAuthState,
            _lastLocation,
            _userPreferenceData,
            _isLocationPermissionGranted
        ) { userState, location, userData, isLocationPermissionGranted ->
            MainActivityUiState.Success(
                userPreferenceData = userData,
                userState,
                lastLocation = location,
                isLocationPermissionGranted = isLocationPermissionGranted
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainActivityUiState.Loading
        )

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    init {
        getLastLocation()
        getCurrentUser()
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
        _dialogState.update {
            it.copy(
                shouldShow = shouldShow,
                dialog = dialog
            )
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getSignedInUser().onEach { result ->
                when (result) {
                    is RecircuResult.Loading -> {
                        _userAuthState.update {
                            UserAuthState.Loading
                        }
                    }

                    is RecircuResult.Success -> {
                        result.data?.let {
                            _userAuthState.update {
                                UserAuthState.SignedIn(userData = result.data)
                            }
                        } ?: _userAuthState.update {
                            UserAuthState.NotSignedIn
                        }
                    }

                    else -> Unit
                }
            }.launchIn(viewModelScope)
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.signOut().onEach { result ->
                when (result) {
                    is RecircuResult.Loading -> {
                        _userAuthState.update {
                            UserAuthState.Loading
                        }
                    }

                    is RecircuResult.Success -> {
                        _userAuthState.update {
                            UserAuthState.SignedOut
                        }
                    }

                    is RecircuResult.Error -> {
                        _userAuthState.update {
                            UserAuthState.Error(e = result.exception)
                        }
                    }

                    else -> Unit
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getLastLocation() {
        /*_isLocationPermissionGranted.update {
            Log.d("Permission Dialog", "getLastLocation = ${app.hasLocationPermission()}")
            app.hasLocationPermission()
        }*/
        viewModelScope.launch(Dispatchers.IO) {
            locationClient.getLocation()
                .distinctUntilChanged()
                .collect { result ->
                    Log.d("Permission Dialog", "getLastLocation flow collected")
                    when (result) {
                        is LocationResult.Success -> {
                            Log.d("Location", "Success ${result.location.latitude}")
                            _lastLocation.update {
                                result.location
                            }
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
                                    Log.d("Permission Dialog", "Missing perm in ViewModel")
                                    _isLocationPermissionGranted.update {
                                        false
                                    }
                                }
                            }
                        }

                        else -> Unit
                    }
                }
        }
    }
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(
        val userPreferenceData: UserPreferenceData,
        val userState: UserAuthState,
        val lastLocation: Location?,
        val isLocationPermissionGranted: Boolean
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

sealed interface UserAuthState {
    object Loading : UserAuthState
    data class SignedIn(
        val userData: UserData
    ) : UserAuthState

    object NotSignedIn : UserAuthState
    object SignedOut : UserAuthState
    data class Error(val e: Throwable?) : UserAuthState
}