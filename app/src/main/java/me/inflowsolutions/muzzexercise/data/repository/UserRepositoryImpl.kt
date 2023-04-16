package me.inflowsolutions.muzzexercise.data.repository

import me.inflowsolutions.muzzexercise.data.db.MuzzExerciseDatabase
import me.inflowsolutions.muzzexercise.data.toUser
import me.inflowsolutions.muzzexercise.domain.model.User
import me.inflowsolutions.muzzexercise.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val db: MuzzExerciseDatabase,
) : UserRepository {
    override suspend fun getUserById(id: Int): User = db.usersDao().getUserById(id).toUser()
}