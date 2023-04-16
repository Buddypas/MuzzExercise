package me.inflowsolutions.muzzexercise.ui.chat

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.inflowsolutions.muzzexercise.domain.model.Message
import me.inflowsolutions.muzzexercise.domain.repository.MessageRepository
import me.inflowsolutions.muzzexercise.domain.repository.UserRepository
import me.inflowsolutions.muzzexercise.ui.mvi.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

// TODO: Extract strings
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : BaseViewModel<ChatState, ChatUiState, ChatUiEvent>() {
    override val viewModelStateFlow = MutableStateFlow(ChatState())
    override val uiStateFlow: StateFlow<ChatUiState> by lazy {
        viewModelStateFlow
            .map {
                it.toUiState()
            }
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
        viewModelScope.launch {
            initViewModelState()
            collectEvents()
        }
    }

    override suspend fun ChatState.toUiState(): ChatUiState =
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

    private suspend fun initViewModelState() {
        Timber.d("0--> initViewModelState")
        val currentUser = userRepository.getUserById(defaultCurrentUserId)
        val recipientUser = userRepository.getUserById(defaultOtherUserId)
        val messages = messageRepository.getAllMessages()
        val state = ChatState(
            currentUser = currentUser,
            recipientUser = recipientUser,
            messages = messages
        )
        viewModelStateFlow.value = state
    }

    private suspend fun collectEvents() {
        uiEventsFlow.collect {
            processEvent(it)
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
        val currentState = viewModelStateFlow.value
        viewModelStateFlow.update {
            it.copy(
                currentUser = currentState.recipientUser,
                recipientUser = currentState.currentUser
            )
        }
    }

    private suspend fun List<Message>.toMessageUiModelList(): List<MessageUiModel> =
        if (isEmpty()) emptyList()
        else calculateUiModelList()

    // TODO: Could have used a separate class for this and made it cleaner
    private suspend fun List<Message>.calculateUiModelList(): List<MessageUiModel> {
        val messageUiModelList = mutableListOf<MessageUiModel>()
        withContext(Dispatchers.Default) {
            this@calculateUiModelList.forEachIndexed { index, currentMessage ->
                val currentTime = currentMessage.sentAt
                val prevTime = if (index > 0) this@calculateUiModelList[index - 1].sentAt else null

                if (prevTime == null || currentTime.minus(prevTime).inWholeHours > 1) {
                    val currentDateTime =
                        currentTime.toLocalDateTime(TimeZone.currentSystemDefault())
                    messageUiModelList.add(
                        MessageUiModel.TimeSeparator.fromLocalDateTime(currentDateTime)
                    )
                }
                val hasTail = when {
                    index == this@calculateUiModelList.lastIndex -> true
                    this@calculateUiModelList.getOrNull(index + 1)?.senderId != currentMessage.senderId -> true
                    else -> prevTime?.let {
                        currentTime.minus(it).inWholeSeconds > 20
                    } ?: false
                }
                messageUiModelList.add(currentMessage.toChat(hasTail))
            }
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

    private companion object {
        const val defaultCurrentUserId: Int = 1
        const val defaultOtherUserId: Int = 2
    }
}