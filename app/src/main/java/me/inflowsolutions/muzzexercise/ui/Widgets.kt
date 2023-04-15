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
 * TODO: Play with padding
 */
@Composable
fun GradientIconButton(
    gradientColors: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(colors = gradientColors),
                shape = CircleShape
            )
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}