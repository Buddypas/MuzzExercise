package me.inflowsolutions.muzzexercise.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.inflowsolutions.muzzexercise.ui.theme.DarkGray
import me.inflowsolutions.muzzexercise.ui.theme.MuzzExerciseTheme

@Composable
fun ColumnScope.MessageBubbleContent(text: String, textColor: Color, showTail: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = textColor,
        modifier = modifier
            .align(Alignment.CenterHorizontally)
            .then(
                if (showTail) Modifier.padding(top = 12.dp, end = 16.dp, start = 16.dp)
                else Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
            )
    )
    if (showTail)
        Icon(
            Icons.Filled.DoneAll,
            tint = Color.Green,
            contentDescription = "Delivered",
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 4.dp, end = 8.dp)
        )
}

@Composable
fun UserMessageBubble(text: String, showTail: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 0.dp
                )
            )
    ) {
        MessageBubbleContent(text, Color.White, showTail, modifier)
    }
}

@Composable
fun FriendMessageBubble(text: String, showTail: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp,
                    bottomStart = 0.dp
                )
            )
    ) {
        MessageBubbleContent(text, DarkGray, showTail, modifier)
    }
}

@Composable
fun Message(chat: MessageUiModel.Chat, maxWidthFraction: Dp, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        if (chat.isMine) {
            Spacer(Modifier.weight(1f))
            UserMessageBubble(
                chat.content,
                chat.hasTail,
                Modifier
                    .widthIn(max = maxWidthFraction)
            )
        } else {
            FriendMessageBubble(
                chat.content,
                chat.hasTail,
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
        val listState = rememberLazyListState()

        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(index = 0)
            }
        }

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 24.dp),
            modifier = Modifier
                .fillMaxHeight(),
            reverseLayout = true
        ) {
            items(
                items = messages,
                key = { messageUiModel ->
                    when(messageUiModel) {
                        is MessageUiModel.Chat -> messageUiModel.id
                        is MessageUiModel.TimeSeparator -> messageUiModel.timeStamp
                    }
                }
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

@Preview(showBackground = true)
@Composable
fun MessageBubblePreview() {
    MuzzExerciseTheme {
        Column {
            UserMessageBubble("This is the message text which should be pretty", true)
            Spacer(modifier = Modifier.height(24.dp))
            UserMessageBubble("This is the message text which should be pretty", false)
            Spacer(modifier = Modifier.height(24.dp))
            FriendMessageBubble("This is the message text which should be pretty", true)
            Spacer(modifier = Modifier.height(24.dp))
            FriendMessageBubble("This is the message text which should be pretty", false)
        }
    }
}