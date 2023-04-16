package me.inflowsolutions.muzzexercise.data

import kotlinx.datetime.Instant
import me.inflowsolutions.muzzexercise.data.db.entity.MessageDto
import me.inflowsolutions.muzzexercise.data.db.entity.UserDto
import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.model.User

suspend fun UserDto.toMessage(): User = User(id, name, imageUrl)

suspend fun MessageDto.toMessage(): Message =
    Message(
        id, content, senderId, Instant.fromEpochMilliseconds(timestamp)
    )