package me.inflowsolutions.muzzexercise.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.inflowsolutions.muzzexercise.data.db.CurrentUserDataStore
import me.inflowsolutions.muzzexercise.data.db.MuzzExerciseDatabase
import me.inflowsolutions.muzzexercise.data.di.ApplicationScope
import me.inflowsolutions.muzzexercise.data.toUser
import me.inflowsolutions.muzzexercise.domain.model.User
import me.inflowsolutions.muzzexercise.domain.model.UserState
import me.inflowsolutions.muzzexercise.domain.repository.UserRepository

class UserRepositoryImpl(
    private val db: MuzzExerciseDatabase,
    private val currentUserDataStore: CurrentUserDataStore,
    @ApplicationScope private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {
    private val currentUserStateFlow: MutableStateFlow<UserState> = MutableStateFlow(UserState())

    init {
        initCurrentUser()
    }

    override suspend fun getUserById(id: Int): User? = db.usersDao().getUserById(id)?.toUser()

    override fun getUsersFlow(): Flow<UserState> = currentUserStateFlow.asSharedFlow()

    override suspend fun switchUser() {
        currentUserStateFlow.value.run {
            currentUserDataStore.setCurrentUserId(otherUser?.id ?: 0)
            currentUserStateFlow.value = UserState(currentUser = otherUser, otherUser = currentUser)
        }
    }

    private fun initCurrentUser() {
        coroutineScope.launch(ioDispatcher) {
            currentUserDataStore.getCurrentUserId().collectLatest { currentUserId ->
                val currentUser: User?
                val otherUser: User?
                if (currentUserId == 0) {
                    currentUser = getUserById(defaultCurrentUserId)
                    otherUser = getUserById(defaultOtherUserId)
                } else {
                    currentUser = getUserById(currentUserId)
                    otherUser = getUserById(3 - currentUserId)
                }
                currentUserStateFlow.value = UserState(currentUser, otherUser)
            }
        }
    }

    private companion object {
        const val defaultCurrentUserId: Int = 1
        const val defaultOtherUserId: Int = 2
    }
}