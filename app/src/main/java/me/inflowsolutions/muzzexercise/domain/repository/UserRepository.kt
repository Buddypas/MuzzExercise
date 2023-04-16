package me.inflowsolutions.muzzexercise.domain.repository

import me.inflowsolutions.muzzexercise.domain.model.User

interface UserRepository {
    suspend fun getUserById(id: Int): User?
}