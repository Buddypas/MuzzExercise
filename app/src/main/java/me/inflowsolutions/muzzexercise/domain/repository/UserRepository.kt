package me.inflowsolutions.muzzexercise.domain.repository

import kotlinx.coroutines.flow.Flow
import me.inflowsolutions.muzzexercise.domain.model.User

interface UserRepository {
    suspend fun getUserById(id: Int): User?

    fun getAllUsers(): Flow<List<User>>
}