package me.inflowsolutions.muzzexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.inflowsolutions.muzzexercise.ui.GradientIconButton
import me.inflowsolutions.muzzexercise.ui.theme.MuzzExerciseTheme
import me.inflowsolutions.muzzexercise.ui.theme.beige
import me.inflowsolutions.muzzexercise.ui.theme.pink

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
        MessageList(messages, modifier = Modifier.weight(1f))
        MessageInputField()
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
fun MessageList(messages: List<Message>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        items(messages) { message ->
            ChatMessage(message = message)
        }
    }
}

@Composable
fun MessageInputField() {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            OutlinedTextField(value = "test", onValueChange = {}, shape = CircleShape)
            Spacer(Modifier.width(32.dp))
            SendButton({ /* TODO: Send */ })
        }
    }
}

@Composable
fun SendButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GradientIconButton(listOf(pink, beige), onClick, modifier) {
        Icon(
            imageVector = Icons.Filled.Send,
            contentDescription = "send",
            tint = Color.White
        )
    }
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
        ChatScreen(messages = messages)
    }
}