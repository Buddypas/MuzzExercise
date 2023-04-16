package me.inflowsolutions.muzzexercise.ui.chat

import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.model.User
import me.inflowsolutions.muzzexercise.domain.repository.MessageRepository
import me.inflowsolutions.muzzexercise.domain.repository.UserRepository
import timber.log.Timber
import java.util.*
import javax.inject.Inject

sealed class MessageUiModel {
    data class Chat(
        val content: String,
        val time: String,
        val isMine: Boolean,
        val hasTail: Boolean
    ) : MessageUiModel()

    data class TimeSeparator(val day: String, val time: String) : MessageUiModel() {
        companion object {
            fun fromLocalDateTime(localDateTime: LocalDateTime): TimeSeparator =
                TimeSeparator(
                    localDateTime.dayOfWeek.name.lowercase()
                        .replaceFirstChar { it.titlecase(Locale.getDefault()) },
                    localDateTime.time.toString().substring(0..4)
                )
        }
    }
}

// TODO: Add interfaces
data class ChatState(
    val currentUser: User? = null,
    val recipientUser: User? = null,
    val messages: List<Message> = emptyList(),
)

data class ChatUiState(
    val messages: List<MessageUiModel> = emptyList(),
    val recipientName: String = "",
    val recipientImageUrl: String = "",
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val viewModelState = MutableStateFlow(ChatState())
    private val currentUserId: Int?
        get() = viewModelState.value.currentUser?.id

    val uiState: StateFlow<ChatUiState> by lazy {
        viewModelState
            .map {
                it.toUiState()
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                ChatUiState()
            )
    }

    init {
        viewModelScope.launch {
            messageRepository.getAllMessages()
                .combine(userRepository.getUsersFlow()) { messages, currentUserState ->
                    ChatState(
                        currentUser = currentUserState.currentUser,
                        recipientUser = currentUserState.otherUser,
                        messages = messages
                    )
                }.collectLatest {
                    viewModelState.value = it
                }
        }
    }

    // TODO: Validate text
    fun onSendClick(text: String) {
        viewModelScope.launch {
            runCatching {
                Message(
                    content = text,
                    senderId = currentUserId ?: 0,
                    sentAt = Clock.System.now()
                )
            }.mapCatching { message ->
                messageRepository.sendMessage(message)
                Timber.d("0--> message sent: $message")
            }.onSuccess {
            }
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            userRepository.switchUser()
            Timber.d("0--> curr user after userSwitch: ${viewModelState.value.currentUser}")
        }
    }

    private fun List<Message>.toMessageUiModelList(): List<MessageUiModel> {
        if (isEmpty()) return emptyList()
        val messageUiModelList = mutableListOf<MessageUiModel>()

        this.forEachIndexed { index, message ->
            val currentTime = message.sentAt
            val prevTime = if (index > 0) this[index - 1].sentAt else null

            if (prevTime == null || currentTime.minus(prevTime).inWholeHours > 1) {
                val currentDateTime = currentTime.toLocalDateTime(TimeZone.currentSystemDefault())
                messageUiModelList.add(
                    MessageUiModel.TimeSeparator.fromLocalDateTime(currentDateTime)
                )
            }
            val hasTail = when {
                index == this.lastIndex -> true
                this.getOrNull(index + 1)?.senderId != (currentUserId ?: 0) -> true
                else -> prevTime?.let {
                    currentTime.minus(it).inWholeSeconds > 20
                } ?: false
            }
            messageUiModelList.add(message.toChat(hasTail))
        }
        return messageUiModelList
    }

    private fun Message.toChat(hasTail: Boolean): MessageUiModel.Chat =
        MessageUiModel.Chat(
            content = content,
            time = sentAt.toLocalDateTime(TimeZone.currentSystemDefault()).time.toString(),
            isMine = senderId == currentUserId,
            hasTail = hasTail
        )

    // TODO: Create mapper
    private fun ChatState.toUiState(): ChatUiState =
        ChatUiState(
            recipientName = recipientUser?.name.orEmpty(),
            recipientImageUrl = recipientUser?.imageUrl.orEmpty(),
            messages = messages.toMessageUiModelList()
        )
}