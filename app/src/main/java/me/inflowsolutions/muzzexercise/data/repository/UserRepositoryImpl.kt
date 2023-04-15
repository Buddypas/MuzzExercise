package me.inflowsolutions.muzzexercise.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.inflowsolutions.muzzexercise.data.db.MuzzExerciseDatabase
import me.inflowsolutions.muzzexercise.data.db.mapper.UserDtoToUserMapper
import me.inflowsolutions.muzzexercise.data.di.ApplicationScope
import me.inflowsolutions.muzzexercise.domain.model.User
import me.inflowsolutions.muzzexercise.domain.repository.UserRepository

class UserRepositoryImpl(
    private val db: MuzzExerciseDatabase,
    private val userDtoMapper: UserDtoToUserMapper,
    @ApplicationScope private val coroutineScope: CoroutineScope,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {
    // TODO: Create db table or datastore for current user
    private lateinit var currentUser: User

    init {
        coroutineScope.launch(ioDispatcher) {
            setCurrentUser(getDefaultUser())
        }
    }

    override suspend fun getUserById(id: Int): User {
        db.usersDao().getUserById(id).let { currentUser ->
            return userDtoMapper.map(currentUser)
        }
    }

    override suspend fun getDefaultUser(): User = getUserById(defaultUserId)

    override suspend fun setCurrentUser(user: User) {
        currentUser = user
    }

    override suspend fun switchUser() {
        TODO("Not yet implemented")
    }

    private companion object {
        const val defaultUserId: Int = 1
    }
}