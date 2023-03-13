package com.godzuche.recircu.domain.repository

import com.godzuche.recircu.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>
    suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean)
}