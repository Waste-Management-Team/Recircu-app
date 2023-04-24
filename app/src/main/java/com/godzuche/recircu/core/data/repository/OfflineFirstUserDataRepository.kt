package com.godzuche.recircu.core.data.repository

import com.godzuche.recircu.core.datastore.RecircuPrefencesDataSource
import com.godzuche.recircu.core.model.UserPreferenceData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineFirstUserDataRepository @Inject constructor(
    private val recircuPrefencesDataSource: RecircuPrefencesDataSource
) : UserDataRepository {
    override val userPreferenceData: Flow<UserPreferenceData> =
        recircuPrefencesDataSource.getUserPreferenceData

    override suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean) =
        recircuPrefencesDataSource.setIsOnboardingCompleted(isOnboardingCompleted)

}