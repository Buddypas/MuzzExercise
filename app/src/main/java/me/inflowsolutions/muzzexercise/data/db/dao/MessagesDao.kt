package me.inflowsolutions.muzzexercise.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.inflowsolutions.muzzexercise.data.db.entity.MessageDto

@Dao
interface MessagesDao {
    @Query("SELECT * FROM messages")
    suspend fun getAllMessages(): List<MessageDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(vararg messages: MessageDto)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
}