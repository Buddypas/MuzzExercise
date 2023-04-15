package me.inflowsolutions.muzzexercise.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageDto(
    // Could have also been a long or string, using Int for simplicity
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val senderId: Int,
    val timestamp: Long
)