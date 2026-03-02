package me.inflowsolutions.muzzexercise

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MuzzExerciseApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}