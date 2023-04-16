package me.inflowsolutions.muzzexercise.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.inflowsolutions.muzzexercise.data.db.MuzzExerciseDatabase
import me.inflowsolutions.muzzexercise.data.db.entity.MessageDto
import me.inflowsolutions.muzzexercise.data.db.mapper.MessageDtoToMessageMapper
import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.repository.MessageRepository
import javax.inject.Inject
import javax.inject.Singleton

class MessageRepositoryImpl(
    private val db: MuzzExerciseDatabase,
    private val messageDtoMapper: MessageDtoToMessageMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MessageRepository {
    override fun getAllMessages(): Flow<List<Message>> =
        db.messagesDao().getAllMessages().map { messageDtoList ->
            messageDtoMapper.mapList(messageDtoList)
        }

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