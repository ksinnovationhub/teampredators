package com.sdn.teampredators.polima.di

import android.content.Context
import android.content.SharedPreferences
import com.sdn.teampredators.polima.utils.Constants.SHARED_PREFERENCE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }
}
