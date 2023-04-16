package me.inflowsolutions.muzzexercise.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * The UI accuracy is limited due to not having a real design file.
 */
@Composable
fun GradientIconButton(
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    icon: @Composable () -> Unit
) {
    val colors = if(onClick == null) gradientColors.map {
        it.copy(alpha = 0.5f)
    } else gradientColors
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(colors = colors),
                shape = CircleShape
            )
            .padding(8.dp)
            .clickable { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}