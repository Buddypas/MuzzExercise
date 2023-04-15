package me.inflowsolutions.muzzexercise.ui.chat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.inflowsolutions.muzzexercise.Message

class ChatViewModel : ViewModel() {
    private val messages = MutableStateFlow<List<Message>>(emptyList())
    fun getMessages(): StateFlow<List<Message>> = messages.asStateFlow()

    init {
        messages.value = listOf(
            Message("User", "Hello!"),
            Message("Friend", "Hi, how are you?"),
            Message("User", "I'm good, thanks! What about you?"),
            Message("Friend", "I'm doing well, thank you.")
        )
    }

    fun onMessageSent(text: String) {
        messages.value.toMutableList().also {
            it.add(Message("User", text))
            messages.value = it
        }
    }
}