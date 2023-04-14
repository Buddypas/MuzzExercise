package me.inflowsolutions.muzzexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import me.inflowsolutions.muzzexercise.ui.GradientIconButton
import me.inflowsolutions.muzzexercise.ui.theme.MuzzExerciseTheme
import me.inflowsolutions.muzzexercise.ui.theme.Beige
import me.inflowsolutions.muzzexercise.ui.theme.DarkGray
import me.inflowsolutions.muzzexercise.ui.theme.Pink

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
fun UserMessage(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun FriendMessage(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
    ) {
        Text(
            text = text,
            color = DarkGray,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun MessageList(messages: List<Message>, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier) {
        val maxWidthFraction = maxWidth * 0.66f

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                val isUserMessage = message.sender == "User"
                Row(modifier = modifier.fillMaxWidth()) {
                    if (isUserMessage) {
                        Spacer(Modifier.weight(1f))
                        UserMessage(
                            message.content,
                            Modifier
                                .widthIn(max = maxWidthFraction)
                        )
                    } else {
                        FriendMessage(
                            message.content,
                            Modifier
                                .widthIn(max = maxWidthFraction)
                        )
                        Spacer(Modifier.weight(1f))
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun MessageInputField() {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)) {
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
    GradientIconButton(listOf(Pink, Beige), onClick, modifier) {
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