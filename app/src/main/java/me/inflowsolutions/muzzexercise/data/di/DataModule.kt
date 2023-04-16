package me.inflowsolutions.muzzexercise.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.inflowsolutions.muzzexercise.data.db.MuzzExerciseDatabase
import me.inflowsolutions.muzzexercise.data.db.dao.MessagesDao
import me.inflowsolutions.muzzexercise.data.db.dao.UsersDao
import me.inflowsolutions.muzzexercise.data.repository.MessageRepositoryImpl
import me.inflowsolutions.muzzexercise.data.repository.UserRepositoryImpl
import me.inflowsolutions.muzzexercise.domain.repository.MessageRepository
import me.inflowsolutions.muzzexercise.domain.repository.UserRepository
import javax.inject.Provider
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    @Singleton
    fun provideRoomCallback(
        database: Provider<MuzzExerciseDatabase>,
        @ApplicationScope applicationScope: CoroutineScope
    ): MuzzExerciseDatabase.RoomCallback {
        return MuzzExerciseDatabase.RoomCallback(database, applicationScope)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
        callback: MuzzExerciseDatabase.RoomCallback
    ): MuzzExerciseDatabase {
        return Room
            .databaseBuilder(appContext, MuzzExerciseDatabase::class.java, "muzz.db")
            .addCallback(callback)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUsersDao(database: MuzzExerciseDatabase): UsersDao =
        database.usersDao()

    @Provides
    fun provideMessagesDao(database: MuzzExerciseDatabase): MessagesDao =
        database.messagesDao()

    @Singleton
    @Provides
    fun provideMessageRepository(
        database: MuzzExerciseDatabase,
    ): MessageRepository = MessageRepositoryImpl(database)

    @Singleton
    @Provides
    fun provideUserRepository(
        database: MuzzExerciseDatabase,
//        currentUserDataStore: CurrentUserDataStore,
        roomCallback: MuzzExerciseDatabase.RoomCallback,
        @ApplicationScope applicationScope: CoroutineScope,
    ): UserRepository =
        UserRepositoryImpl(
            database,
//            currentUserDataStore,
//            roomCallback,
//            applicationScope
        )

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope