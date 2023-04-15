package me.inflowsolutions.muzzexercise

import android.app.Application
import me.inflowsolutions.muzzexercise.data.db.MuzzExerciseDatabaseImpl
import timber.log.Timber

class MuzzExerciseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        MuzzExerciseDatabaseImpl.initDatabase(applicationContext)
        // Initialize timber
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}