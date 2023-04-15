package me.inflowsolutions.muzzexercise.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import me.inflowsolutions.muzzexercise.data.db.entity.MessageDto

@Dao
interface MessagesDao {
    @Query("SELECT * FROM messages")
    fun getAllMessages(): List<MessageDto>

    @Query("DELETE FROM messages")
    fun deleteAllMessages()
}