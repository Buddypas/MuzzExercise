package me.inflowsolutions.muzzexercise.ui.chat

import kotlinx.datetime.LocalDateTime
import java.util.*

sealed class MessageUiModel {
    data class Chat(
        val id: Int,
        val content: String,
        val time: String,
        val isMine: Boolean,
        val hasTail: Boolean
    ) : MessageUiModel()

    data class TimeSeparator(val day: String, val time: String, val timeStamp: Long) :
        MessageUiModel() {
        companion object {
            fun fromLocalDateTime(localDateTime: LocalDateTime): TimeSeparator =
                TimeSeparator(
                    localDateTime.dayOfWeek.name.lowercase()
                        .replaceFirstChar { it.titlecase(Locale.getDefault()) },
                    localDateTime.time.toString().substring(0..4),
                    System.currentTimeMillis()
                )
        }
    }
}