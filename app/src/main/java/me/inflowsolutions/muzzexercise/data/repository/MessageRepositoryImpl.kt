package me.inflowsolutions.muzzexercise.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.inflowsolutions.muzzexercise.data.db.MuzzExerciseDatabase
import me.inflowsolutions.muzzexercise.data.db.mapper.MessageDtoToMessageMapper
import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.repository.MessageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor(
    private val db: MuzzExerciseDatabase,
    private val messageDtoMapper: MessageDtoToMessageMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MessageRepository {
    override fun getAllMessages(): Flow<List<Message>> =
        db.messagesDao().getAllMessages().map { messageDtoList ->
            messageDtoMapper.mapList(messageDtoList)
        }
}