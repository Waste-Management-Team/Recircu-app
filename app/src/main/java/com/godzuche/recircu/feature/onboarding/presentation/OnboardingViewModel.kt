package com.godzuche.recircu.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.recircu.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    fun saveOnboardingState(isOnboardingCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.setIsOnboardingCompleted(isOnboardingCompleted = isOnboardingCompleted)
        }
    }
}