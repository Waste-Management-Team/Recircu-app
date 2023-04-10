package com.godzuche.recircu

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.recircu.core.domain.model.UserPreferenceData
import com.godzuche.recircu.core.domain.repository.UserDataRepository
import com.godzuche.recircu.feature.authentication.navigation.authGraphRoute
import com.godzuche.recircu.navigation.gettingStartedRoute
import com.godzuche.recircu.navigation.sellerHomeGraphRoute
import com.godzuche.recircu.navigation.sellerHomeRoute
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppMainViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userPreferenceData.map {
        MainActivityUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainActivityUiState.Loading
    )

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

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
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(
        val userPreferenceData: UserPreferenceData,
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