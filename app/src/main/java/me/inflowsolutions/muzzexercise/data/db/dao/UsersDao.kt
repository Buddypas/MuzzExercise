package me.inflowsolutions.muzzexercise.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import me.inflowsolutions.muzzexercise.data.db.entity.UserDto


@Dao
interface UsersDao {
    @Insert
    fun addUser(user: UserDto)
}