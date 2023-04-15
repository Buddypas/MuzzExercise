package me.inflowsolutions.muzzexercise.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import me.inflowsolutions.muzzexercise.data.db.entity.UserDto

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg categories: UserDto)
}