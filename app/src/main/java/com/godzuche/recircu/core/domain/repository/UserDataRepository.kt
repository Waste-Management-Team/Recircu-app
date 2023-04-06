package com.godzuche.recircu.core.domain.repository

import com.godzuche.recircu.core.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean)
}