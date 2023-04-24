package com.godzuche.recircu.core.data.repository

import com.godzuche.recircu.core.model.UserPreferenceData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userPreferenceData: Flow<UserPreferenceData>
    suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean)
}