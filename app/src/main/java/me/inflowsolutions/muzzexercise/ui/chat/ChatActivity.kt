package me.inflowsolutions.muzzexercise.ui.chat

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import me.inflowsolutions.muzzexercise.ui.theme.MuzzExerciseTheme

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
    val state by viewModel.uiStateFlow.collectAsState()

    Scaffold(
        topBar = {
            MuzzAppBar(
                state.recipientName,
                state.recipientImageUrl,
                { viewModel.setEvent(ChatUiEvent.SwitchUser) },
            )
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(Modifier.fillMaxWidth()) {
                MessageList(
                    state.messages.asReversed(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(it)
                )
                MessageInputField(
                    modifier = Modifier.fillMaxWidth(),
                    onSendClick = { viewModel.setEvent(ChatUiEvent.AddMessage(it)) },
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
                // TODO: Add a loading animation and a placeholder
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
        navigationIcon = {
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

