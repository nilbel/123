package com.example.cinematv.ui.theme

import androidx.compose.runtime.Composable
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

private val CinemaColorScheme = darkColorScheme()

@Composable
fun CinemaTvTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CinemaColorScheme,
        content = content
    )
}
