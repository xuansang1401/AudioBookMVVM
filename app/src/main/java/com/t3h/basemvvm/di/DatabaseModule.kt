package com.t3h.aihocgioi.di

import android.content.Context
import com.t3h.basemvvm.db.AppDatabase
import com.t3h.basemvvm.db.DatabaseDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context)
            : AppDatabase {
        return AppDatabase.create(context)
    }
    @Singleton
    @Provides
    fun provideDatabaseDao(appDatabase: AppDatabase): DatabaseDao {
        return appDatabase.databaseDao()
    }

}