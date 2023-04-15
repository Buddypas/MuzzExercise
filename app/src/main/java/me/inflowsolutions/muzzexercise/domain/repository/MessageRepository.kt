package me.inflowsolutions.muzzexercise.domain.repository

import kotlinx.coroutines.flow.Flow
import me.inflowsolutions.muzzexercise.domain.model.Message

interface MessageRepository {
    fun getAllMessages(): Flow<List<Message>>
}