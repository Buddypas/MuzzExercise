package me.inflowsolutions.muzzexercise.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import me.inflowsolutions.muzzexercise.ui.GradientIconButton
import me.inflowsolutions.muzzexercise.ui.theme.Beige
import me.inflowsolutions.muzzexercise.ui.theme.DarkGray
import me.inflowsolutions.muzzexercise.ui.theme.Pink


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