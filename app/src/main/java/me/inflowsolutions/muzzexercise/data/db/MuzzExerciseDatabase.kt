package me.inflowsolutions.muzzexercise.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.inflowsolutions.muzzexercise.data.db.dao.MessagesDao
import me.inflowsolutions.muzzexercise.data.db.dao.UsersDao
import me.inflowsolutions.muzzexercise.data.db.entity.MessageDto
import me.inflowsolutions.muzzexercise.data.db.entity.UserDto
import me.inflowsolutions.muzzexercise.data.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider


@Database(entities = [UserDto::class, MessageDto::class], version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class MuzzExerciseDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao
    abstract fun messagesDao(): MessagesDao

    class RoomCallback @Inject constructor(
        private val database: Provider<MuzzExerciseDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val usersDao = database.get().usersDao()

            applicationScope.launch {
                usersDao.insertAll(
                    UserDto(name = "Sarah"),
                    UserDto(name = "John"),
                )
            }
        }
    }
}
