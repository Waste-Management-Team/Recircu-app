package com.godzuche.recircu.di

import com.godzuche.recircu.data.location.DefaultLocationClient
import com.godzuche.recircu.data.repository.OfflineFirstUserDataRepository
import com.godzuche.recircu.domain.location.LocationClient
import com.godzuche.recircu.domain.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository
    ): UserDataRepository

    @Binds
    fun bindsLocationClient(
        locationClient: DefaultLocationClient
    ): LocationClient
}