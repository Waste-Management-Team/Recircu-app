package com.godzuche.recircu.data.repository

import com.godzuche.recircu.data.local.RecircuPrefencesDataSource
import com.godzuche.recircu.domain.model.UserData
import com.godzuche.recircu.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineFirstUserDataRepository @Inject constructor(
    private val recircuPrefencesDataSource: RecircuPrefencesDataSource
) : UserDataRepository {
    override val userData: Flow<UserData> = recircuPrefencesDataSource.getUserData

    override suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean) =
        recircuPrefencesDataSource.setIsOnboardingCompleted(isOnboardingCompleted)

}