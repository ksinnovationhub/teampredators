package com.sdn.teampredators.polima.domain.di

import com.sdn.teampredators.polima.data.remote.DummyApi
import com.sdn.teampredators.polima.data.remote.DummyApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideDummyApi(dummyApiImpl: DummyApiImpl): DummyApi{
        return dummyApiImpl
    }
}