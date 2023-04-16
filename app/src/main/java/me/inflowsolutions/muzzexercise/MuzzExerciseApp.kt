package me.inflowsolutions.muzzexercise

import android.app.Application
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import me.inflowsolutions.muzzexercise.data.db.MuzzExerciseDatabase
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MuzzExerciseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize timber
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}