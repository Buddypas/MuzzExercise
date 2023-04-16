package me.inflowsolutions.muzzexercise.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import me.inflowsolutions.muzzexercise.data.db.CurrentUserDataStore
import me.inflowsolutions.muzzexercise.data.db.MuzzExerciseDatabase
import me.inflowsolutions.muzzexercise.data.di.ApplicationScope
import me.inflowsolutions.muzzexercise.data.toUser
import me.inflowsolutions.muzzexercise.domain.model.User
import me.inflowsolutions.muzzexercise.domain.model.UserState
import me.inflowsolutions.muzzexercise.domain.repository.UserRepository
import timber.log.Timber

class UserRepositoryImpl(
    private val db: MuzzExerciseDatabase,
    private val currentUserDataStore: CurrentUserDataStore,
    private val roomCallback: MuzzExerciseDatabase.RoomCallback,
    @ApplicationScope private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {
    private lateinit var currentUserState: UserState
    private val currentUserFlow: MutableSharedFlow<UserState> = MutableSharedFlow()

    override suspend fun getUserById(id: Int): User? = db.usersDao().getUserById(id)?.toUser()

    override fun getUsersFlow(): Flow<UserState> = currentUserFlow

    override suspend fun switchUser() {
        currentUserState.run {
            currentUserDataStore.setCurrentUserId(otherUser.id)
            currentUserState = UserState(currentUser = otherUser, otherUser = currentUser)
            currentUserFlow.emit(currentUserState)
        }
    }

    override suspend fun initCurrentUser() {
//        withContext(ioDispatcher) {
        roomCallback.prePopulationDeferred.await() // Add this line

        currentUserDataStore.getCurrentUserId().collectLatest { currentUserId ->
            Timber.d("0--> currentUserId: $currentUserId")
            val currentUser: User?
            val otherUser: User?
            if (currentUserId == 0) {
                currentUser = getUserById(defaultCurrentUserId)
                otherUser = getUserById(defaultOtherUserId)
            } else {
                currentUser = getUserById(currentUserId)
                otherUser = getUserById(3 - currentUserId)
            }
            Timber.d("0--> currentUser: $currentUser, otherUser: $otherUser")
            currentUserState = UserState(currentUser!!, otherUser!!)
            currentUserFlow.emit(currentUserState)
//                currentUserFlow.value = UserState(currentUser, otherUser)
        }
//        }
    }

    private companion object {
        const val defaultCurrentUserId: Int = 1
        const val defaultOtherUserId: Int = 2
    }
}