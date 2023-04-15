package me.inflowsolutions.muzzexercise.domain.model

import kotlinx.datetime.Instant

data class Message(
    val id: Int? = null,
    val content: String,
    val senderId: Int,
    val sentAt: Instant
)