package me.inflowsolutions.muzzexercise.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.inflowsolutions.muzzexercise.data.db.dao.MessagesDao
import me.inflowsolutions.muzzexercise.data.db.dao.UsersDao
import me.inflowsolutions.muzzexercise.data.db.entity.MessageDto
import me.inflowsolutions.muzzexercise.data.db.entity.UserDto
import me.inflowsolutions.muzzexercise.data.di.ApplicationScope
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Database(entities = [UserDto::class, MessageDto::class], version = 1, exportSchema = false)
abstract class MuzzExerciseDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun messagesDao(): MessagesDao

    @Singleton
    class RoomCallback @Inject constructor(
        private val database: Provider<MuzzExerciseDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope,
    ) : RoomDatabase.Callback() {
//        val prePopulationDeferred = CompletableDeferred<Unit>()

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val userDao = database.get().usersDao()

            applicationScope.launch {
                userDao.insertAll(
                    UserDto(
//                        id = 1,
                        name = "John",
                        imageUrl = "https://t4.ftcdn.net/jpg/03/98/85/77/360_F_398857704_n44BPhqM68Xk9vT31BeFkLQwWsgeZS6C.jpg"
                    ),
                    UserDto(
//                        id = 2,
                        name = "Sarah",
                        imageUrl = "https://media.istockphoto.com/id/1311858467/photo/head-shot-portrait-attractive-young-indian-woman-looking-at-camera.jpg?s=612x612&w=0&k=20&c=0QWC0t9uc6tptvQkWZxlFKK6hsnOxQBCobTfgkuNbLA="
                    ),
                )
                Timber.d("0--> inserted")
//                prePopulationDeferred.complete(Unit)
            }
        }
    }
}
