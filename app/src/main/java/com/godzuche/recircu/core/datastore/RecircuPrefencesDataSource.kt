package com.godzuche.recircu.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.godzuche.recircu.core.model.UserPreferenceData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class RecircuPrefencesDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>
) {
    val getUserPreferenceData = userPreferences.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else throw it
        }.map {
            UserPreferenceData(
                isOnboardingCompleted = it[IS_ONBOARDING_COMPLETED] ?: false
            )
        }

    suspend fun setIsOnboardingCompleted(isOnboardingCompleted: Boolean) {
        userPreferences.edit {
            it[IS_ONBOARDING_COMPLETED] = isOnboardingCompleted
        }
    }

    companion object {
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
    }
}