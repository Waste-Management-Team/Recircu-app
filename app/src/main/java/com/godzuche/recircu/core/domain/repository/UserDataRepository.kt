package com.godzuche.recircu.core.domain.repository

import com.godzuche.recircu.core.domain.model.UserPreferenceData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userPreferenceData: Flow<UserPreferenceData>
    suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean)
}