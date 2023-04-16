package me.inflowsolutions.muzzexercise.ui.chat

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
import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.model.User
import me.inflowsolutions.muzzexercise.domain.repository.MessageRepository
import me.inflowsolutions.muzzexercise.domain.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

sealed class MessageUiModel {
    data class Chat(val content: String, val time: String, val isMine: Boolean)
    data class TimeSeparator(val day: String, val time: String)
}

// TODO: Add interfaces
data class ChatState(
    val currentUser: User? = null,
    val recipientUser: User? = null,
    val messages: List<Message> = emptyList(),
)

data class ChatUiState(
    val messages: List<MessageUiModel> = emptyList(),
    val recipientName: String = ""
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val viewModelState = MutableStateFlow(ChatState())

    val uiState: StateFlow<ChatUiState> by lazy {
        viewModelState
            .map { it.toUiState() }
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
                    senderId = 1,
                    sentAt = Clock.System.now()
                )
            }.mapCatching { message ->
                messageRepository.sendMessage(message)
            }.onSuccess {
                Timber.d("0--> message sent")
            }
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            userRepository.switchUser()
        }
    }

    private fun List<Message>.toMessageUiModelList(): List<MessageUiModel> {
        // TODO: Implement
        return emptyList()
    }

    // TODO: Create mapper
    private fun ChatState.toUiState(): ChatUiState =
        ChatUiState(
            recipientName = recipientUser?.name.orEmpty(),
            messages = messages.toMessageUiModelList()
        )
}