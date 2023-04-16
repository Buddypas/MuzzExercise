package me.inflowsolutions.muzzexercise.ui.chat

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.More
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import me.inflowsolutions.muzzexercise.ui.GradientIconButton
import me.inflowsolutions.muzzexercise.ui.theme.Beige
import me.inflowsolutions.muzzexercise.ui.theme.DarkGray
import me.inflowsolutions.muzzexercise.ui.theme.MuzzExerciseTheme
import me.inflowsolutions.muzzexercise.ui.theme.Pink

@AndroidEntryPoint
class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuzzExerciseTheme {
                ChatScreen()
            }
        }
    }
}

@Composable
fun ChatScreen(viewModel: ChatViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            MuzzAppBar(state.recipientName, state.recipientImageUrl, { viewModel.onBackClick() })
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(Modifier.fillMaxWidth()) {
                MessageList(
                    state.messages.asReversed(), modifier = Modifier
                        .weight(1f)
                        .padding(it)
                )
                MessageInputField(
                    modifier = Modifier.fillMaxWidth(),
                    onSendClick = { viewModel.onSendClick(it) },
                )
            }
        }
    }
}

@Composable
fun MuzzAppBar(
    recipientName: String,
    recipientImageUrl: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as? Activity)
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO: Add loader
                AsyncImage(
                    model = recipientImageUrl,
                    contentDescription = "Avatar image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(36.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    recipientName,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        },
        navigationIcon =
        {
            IconButton(onClick = { activity?.finish() }) {
                Icon(
                    Icons.Outlined.ArrowBackIosNew,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Navigate up",
                    modifier = Modifier.height(36.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    Icons.Outlined.MoreHoriz,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Switch user",
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        modifier = modifier.height(56.dp),
    )
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
fun Message(chat: MessageUiModel.Chat, maxWidthFraction: Dp, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        if (chat.isMine) {
            Spacer(Modifier.weight(1f))
            UserMessage(
                chat.content,
                Modifier
                    .widthIn(max = maxWidthFraction)
            )
        } else {
            FriendMessage(
                chat.content,
                Modifier
                    .widthIn(max = maxWidthFraction)
            )
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
fun TimeDisplay(day: String, time: String, modifier: Modifier = Modifier) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(day)
            }
            append(" $time")
        },
        style = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center,
            color = DarkGray
        ),
        modifier = modifier
    )
}


@Composable
fun MessageList(messages: List<MessageUiModel>, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier) {
        val maxWidthFraction = maxWidth * 0.66f

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            reverseLayout = true
        ) {
            items(
                items = messages,
//                key = { message -> message.id ?: 0 }
            ) { messageUiModel ->
                when (messageUiModel) {
                    is MessageUiModel.Chat -> {
                        Message(messageUiModel, maxWidthFraction)
                    }
                    is MessageUiModel.TimeSeparator -> {
                        TimeDisplay(
                            day = messageUiModel.day,
                            time = messageUiModel.time,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

// TODO: Handle max width
@Composable
fun MessageInputField(onSendClick: (String) -> Unit, modifier: Modifier = Modifier) {
    var fieldText by remember { mutableStateOf("") }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        shape = CardDefaults.shape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                shape = CircleShape,
                placeholder = {
                    Text(
                        "Type your message here",
                        style = MaterialTheme.typography.bodyLarge.copy(color = DarkGray)
                    )
                },
                value = fieldText,
                onValueChange = { fieldText = it },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
            )
            Spacer(Modifier.width(24.dp))
            SendButton(
                onClick =
                if (fieldText.isEmpty()) null
                else (
                    {
                        onSendClick(fieldText)
                        fieldText = ""
                    })
            )
        }
    }
}

@Composable
fun SendButton(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    GradientIconButton(
        gradientColors = listOf(Pink, Beige),
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Send,
            contentDescription = "send",
            tint = Color.White
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TimeDisplayPreview() {
//    MuzzExerciseTheme {
//        TimeDisplay("Monday", "12:25")
//    }
//}

@Preview(showBackground = true)
@Composable
fun MuzzAppBarPreview() {
    MuzzExerciseTheme {
        Scaffold(
            topBar = {
                MuzzAppBar(
                    "Sarah",
                    "https://t4.ftcdn.net/jpg/03/98/85/77/360_F_398857704_n44BPhqM68Xk9vT31BeFkLQwWsgeZS6C.jpg",
                    {})
            }
        ) {
            Spacer(modifier = Modifier.padding(it))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    MuzzExerciseTheme {
        ChatScreen()
    }
}

