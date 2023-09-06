package com.godzuche.recircu.core.data.di

import com.godzuche.recircu.core.data.repository.AuthRepository
import com.godzuche.recircu.core.data.repository.AuthRepositoryImpl
import com.godzuche.recircu.core.data.repository.OfflineFirstUserDataRepository
import com.godzuche.recircu.core.data.repository.UserDataRepository
import com.godzuche.recircu.core.location.DefaultLocationClient
import com.godzuche.recircu.core.location.LocationClient
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

    @Binds
    fun bindsAuthRepository(
        authRepository: AuthRepositoryImpl
    ): AuthRepository

}