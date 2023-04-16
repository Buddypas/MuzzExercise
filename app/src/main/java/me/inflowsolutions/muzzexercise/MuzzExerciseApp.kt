package me.inflowsolutions.muzzexercise

import android.app.Application
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import me.inflowsolutions.muzzexercise.data.db.MuzzExerciseDatabase
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
//@AndroidEntryPoint
class MuzzExerciseApp : Application() {
//    @Inject
//    lateinit var db: MuzzExerciseDatabase

    override fun onCreate() {
        super.onCreate()

//        Timber.d("0--> MuzzExerciseDatabase: $db")

        // Initialize timber
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}