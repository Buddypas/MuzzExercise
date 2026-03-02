package me.inflowsolutions.muzzexercise.data

import me.inflowsolutions.muzzexercise.data.db.entity.MessageDto
import me.inflowsolutions.muzzexercise.data.db.entity.UserDto
import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.model.User
import kotlin.time.Instant

fun UserDto.toUser(): User = User(id, name, imageUrl)

fun MessageDto.toMessage(): Message =
    Message(id, content, senderId, Instant.fromEpochMilliseconds(timestamp))