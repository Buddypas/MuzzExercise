package me.inflowsolutions.muzzexercise.domain.repository

import kotlinx.coroutines.flow.Flow
import me.inflowsolutions.muzzexercise.domain.model.User
import me.inflowsolutions.muzzexercise.domain.model.UserState

interface UserRepository {
    suspend fun initCurrentUser()
    suspend fun getUserById(id: Int): User?
    fun getUsersFlow(): Flow<UserState>
    suspend fun switchUser()
}