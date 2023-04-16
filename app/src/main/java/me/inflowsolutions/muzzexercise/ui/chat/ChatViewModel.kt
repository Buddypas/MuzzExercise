package me.inflowsolutions.muzzexercise.ui.chat

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.repository.MessageRepository
import me.inflowsolutions.muzzexercise.domain.repository.UserRepository
import me.inflowsolutions.muzzexercise.ui.mvi.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : BaseViewModel<ChatState, ChatUiState, ChatUiEvent>() {
    override val viewModelStateFlow = MutableStateFlow(ChatState())
    override val uiStateFlow: StateFlow<ChatUiState> by lazy {
        viewModelStateFlow
            .map { it.toUiState() }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                ChatUiState()
            )
    }
    override val uiEventsFlow: MutableSharedFlow<ChatUiEvent> = MutableSharedFlow()

    private val currentUserId: Int?
        get() = viewModelStateFlow.value.currentUser?.id

    init {
        initViewModelState()
        viewModelScope.launch {
            uiEventsFlow.collect {
                processEvent(it)
            }
        }
    }

    override fun ChatState.toUiState(): ChatUiState =
        ChatUiState(
            recipientName = recipientUser?.name.orEmpty(),
            recipientImageUrl = recipientUser?.imageUrl.orEmpty(),
            messages = messages.toMessageUiModelList()
        )

    override fun setEvent(event: ChatUiEvent) {
        viewModelScope.launch {
            uiEventsFlow.emit(event)
        }
    }

    override fun processEvent(event: ChatUiEvent) {
        when (event) {
            ChatUiEvent.SwitchUser -> switchUser()
            is ChatUiEvent.AddMessage -> sendMessage(event.text)
        }
    }

    private fun initViewModelState() {
        viewModelScope.launch {
            combine(
                messageRepository.getAllMessages(),
                userRepository.getUsersFlow()
            ) { messages, currentUserState ->
                ChatState(
                    currentUser = currentUserState.currentUser,
                    recipientUser = currentUserState.otherUser,
                    messages = messages
                )
            }.collectLatest {
                viewModelStateFlow.value = it
            }
        }
    }

    private fun sendMessage(text: String) {
        viewModelScope.launch {
            messageRepository.sendMessage(
                Message(
                    content = text,
                    senderId = currentUserId ?: 0,
                    sentAt = Clock.System.now()
                )
            )
        }
    }

    private fun switchUser() {
        viewModelScope.launch {
            userRepository.switchUser()
        }
    }

    private fun List<Message>.toMessageUiModelList(): List<MessageUiModel> {
        if (isEmpty()) return emptyList()
        val messageUiModelList = mutableListOf<MessageUiModel>()

        this.forEachIndexed { index, currentMessage ->
            val currentTime = currentMessage.sentAt
            val prevTime = if (index > 0) this[index - 1].sentAt else null

            if (prevTime == null || currentTime.minus(prevTime).inWholeHours > 1) {
                val currentDateTime = currentTime.toLocalDateTime(TimeZone.currentSystemDefault())
                messageUiModelList.add(
                    MessageUiModel.TimeSeparator.fromLocalDateTime(currentDateTime)
                )
            }
            val hasTail = when {
                index == this.lastIndex -> true
                this.getOrNull(index + 1)?.senderId != currentMessage.senderId -> true
                else -> prevTime?.let {
                    currentTime.minus(it).inWholeSeconds > 20
                } ?: false
            }
            messageUiModelList.add(currentMessage.toChat(hasTail))
        }
        return messageUiModelList
    }

    private fun Message.toChat(hasTail: Boolean): MessageUiModel.Chat =
        MessageUiModel.Chat(
            id = id ?: 0,
            content = content,
            time = sentAt.toLocalDateTime(TimeZone.currentSystemDefault()).time.toString(),
            isMine = senderId == currentUserId,
            hasTail = hasTail
        )
}

object MessageListToUiModelListMapper {

}