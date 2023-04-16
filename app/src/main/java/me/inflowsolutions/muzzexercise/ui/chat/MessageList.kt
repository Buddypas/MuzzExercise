package me.inflowsolutions.muzzexercise.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.inflowsolutions.muzzexercise.ui.theme.DarkGray

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
                // TODO: Add key
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