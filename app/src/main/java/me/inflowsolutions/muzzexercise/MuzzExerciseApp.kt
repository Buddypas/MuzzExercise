package me.inflowsolutions.muzzexercise

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MuzzExerciseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize timber
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}