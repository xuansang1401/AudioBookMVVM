package com.t3h.aihocgioi.di

import android.content.Context
import com.t3h.basemvvm.util.AppConfig
import com.t3h.basemvvm.netword.AudioBookRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesAppConfig(@ApplicationContext appContext: Context): AppConfig {
        return AppConfig.create(appContext)
    }

    @Singleton
    @Provides
    fun providesStudyRequest(): AudioBookRequest {
        return AudioBookRequest.create()
    }
}