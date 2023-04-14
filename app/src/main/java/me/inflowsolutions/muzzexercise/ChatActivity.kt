package me.inflowsolutions.muzzexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.inflowsolutions.muzzexercise.ui.theme.MuzzExerciseTheme

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val messages = listOf(
            Message("User", "Hello!"),
            Message("Friend", "Hi, how are you?"),
            Message("User", "I'm good, thanks! What about you?"),
            Message("Friend", "I'm doing well, thank you.")
        )
        setContent {
            MuzzExerciseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen(messages)
                }
            }
        }
    }
}

data class Message(val sender: String, val content: String)

@Composable
fun ChatScreen(messages: List<Message>) {
    Column {
        MessageList(messages)
    }
}

@Composable
fun ChatMessage(message: Message) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = if (message.sender == "User") Alignment.End else Alignment.Start
    ) {
        Text(text = message.sender)
        Text(text = message.content)
    }
}

@Composable
fun MessageList(messages: List<Message>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        reverseLayout = true
    ) {
        items(messages) { message ->
            ChatMessage(message = message)
        }
    }
}

@Composable
fun MessageInputField() {

}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    val messages = listOf(
        Message("User", "Hello!"),
        Message("Friend", "Hi, how are you?"),
        Message("User", "I'm good, thanks! What about you?"),
        Message("Friend", "I'm doing well, thank you.")
    )
    MuzzExerciseTheme {
        MessageList(messages = messages)
    }
}