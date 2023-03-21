package com.godzuche.recircu

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.recircu.domain.location.LocationClient
import com.godzuche.recircu.domain.model.UserData
import com.godzuche.recircu.domain.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppMainViewModel @Inject constructor(
    private val locationClient: LocationClient,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData.map {
        MainActivityUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainActivityUiState.Loading
    )

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
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
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}