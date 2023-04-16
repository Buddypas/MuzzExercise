package me.inflowsolutions.muzzexercise.ui

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(
    condition: Boolean,
    crossinline other: Modifier.() -> Modifier,
) = if (condition) other() else this