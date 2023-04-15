package me.inflowsolutions.muzzexercise.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.repository.MessageRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val messageRepository: MessageRepository) : ViewModel() {
    private val messages = MutableStateFlow<List<Message>>(emptyList())
    fun getMessages(): StateFlow<List<Message>> = messages.asStateFlow()

    init {
        viewModelScope.launch {
            messageRepository.getAllMessages().collectLatest {
                messages.value = it
            }
        }
    }

    // TODO: Validate text
    fun onSendClick(text: String) {
        viewModelScope.launch {
            runCatching {
                Message(
                    content = text,
                    senderId = 0,
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
        // TODO: Switch user
    }
}