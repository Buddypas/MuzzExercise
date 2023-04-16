package me.inflowsolutions.muzzexercise.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDto(
    // TODO: Could have also been a long or string, using Int for simplicity
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUrl: String
)

// TODO: Emphasise that a userId foreign key was not necessary