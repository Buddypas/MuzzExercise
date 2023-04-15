package me.inflowsolutions.muzzexercise.data.db.mapper

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Instant
import me.inflowsolutions.muzzexercise.data.SuspendingMapper
import me.inflowsolutions.muzzexercise.data.db.entity.MessageDto
import me.inflowsolutions.muzzexercise.domain.model.Message

class MessageDtoToMessageMapper(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendingMapper<MessageDto, Message>(coroutineDispatcher) {
    override suspend fun MessageDto.toMappedEntity(): Message =
        Message(
            id, content, senderId, Instant.fromEpochMilliseconds(timestamp)
        )
}