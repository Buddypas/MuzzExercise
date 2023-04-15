package me.inflowsolutions.muzzexercise.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.model.User
import me.inflowsolutions.muzzexercise.domain.repository.MessageRepository
import me.inflowsolutions.muzzexercise.domain.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

data class ChatUiState(
    private val messages: List<Message> = emptyList(),
    private val currentUser: User? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val uiState = MutableStateFlow(ChatUiState())

    fun getUiStateFlow(): StateFlow<ChatUiState> = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            messageRepository.getAllMessages().collectLatest { messages ->
                uiState.update { currentState ->
                    currentState.copy(messages = messages)
                }
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
        userRepository

    }
}