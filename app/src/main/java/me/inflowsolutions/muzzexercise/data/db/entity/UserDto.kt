package me.inflowsolutions.muzzexercise.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDto(
    // Could have also been a long or string, using Int for simplicity
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String
)

// TODO: Emphasise that a userId foreign key was not necessary