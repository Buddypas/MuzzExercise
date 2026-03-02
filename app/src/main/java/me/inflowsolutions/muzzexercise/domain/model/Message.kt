package me.inflowsolutions.muzzexercise.domain.model

import kotlin.time.Instant

data class Message(
    val id: Int? = null,
    val content: String,
    val senderId: Int,
    val sentAt: Instant
)