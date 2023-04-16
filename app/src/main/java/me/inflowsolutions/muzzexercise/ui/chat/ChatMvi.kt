package me.inflowsolutions.muzzexercise.ui.chat

import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.model.User
import me.inflowsolutions.muzzexercise.ui.mvi.UiEvent
import me.inflowsolutions.muzzexercise.ui.mvi.UiState
import me.inflowsolutions.muzzexercise.ui.mvi.ViewModelState

data class ChatState(
    val currentUser: User? = null,
    val recipientUser: User? = null,
    val messages: List<Message> = emptyList(),
) : ViewModelState

data class ChatUiState(
    val messages: List<MessageUiModel> = emptyList(),
    val recipientName: String = "",
    val recipientImageUrl: String = "",
): UiState

sealed class ChatUiEvent : UiEvent {
    object SwitchUser : ChatUiEvent()
    data class AddMessage(val text: String): ChatUiEvent()
}