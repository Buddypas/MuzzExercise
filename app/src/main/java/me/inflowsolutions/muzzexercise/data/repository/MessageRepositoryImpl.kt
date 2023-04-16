package me.inflowsolutions.muzzexercise.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.inflowsolutions.muzzexercise.data.db.MuzzExerciseDatabase
import me.inflowsolutions.muzzexercise.data.db.entity.MessageDto
import me.inflowsolutions.muzzexercise.data.toMessage
import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.repository.MessageRepository

class MessageRepositoryImpl(
    private val db: MuzzExerciseDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MessageRepository {
    override fun getAllMessages(): Flow<List<Message>> =
        db.messagesDao().getAllMessages().map {
            it.map { messageDto -> messageDto.toMessage() }
        }.flowOn(ioDispatcher)

    override suspend fun sendMessage(message: Message) {
        withContext(ioDispatcher) {
            val messageDto = MessageDto(
                content = message.content,
                senderId = message.senderId,
                timestamp = message.sentAt.toEpochMilliseconds()
            )
            db.messagesDao().insertMessage(messageDto)
        }
    }
}