package com.sdn.teampredators.polima

import android.app.Application
import com.sdn.teampredators.polima.utils.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PolimaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        else
            Timber.plant(ReleaseTree())
    }
}
