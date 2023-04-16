package me.inflowsolutions.muzzexercise.domain.repository

import kotlinx.coroutines.flow.Flow
import me.inflowsolutions.muzzexercise.domain.model.Message

interface MessageRepository {
    suspend fun getAllMessages(): List<Message>
    suspend fun sendMessage(message: Message)
}