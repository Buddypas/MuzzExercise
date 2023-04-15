package me.inflowsolutions.muzzexercise.data.db

import android.content.Context
import androidx.room.Room

object MuzzExerciseDatabaseImpl {
    private lateinit var db: MuzzExerciseDatabase

    fun initDatabase(context: Context) {
        db = Room.databaseBuilder(
            context.applicationContext,
            MuzzExerciseDatabase::class.java,
            "muzz_database"
        ).build()
    }

    fun getDatabase(): MuzzExerciseDatabase = db
}