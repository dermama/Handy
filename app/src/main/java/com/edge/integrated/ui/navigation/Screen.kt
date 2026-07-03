package com.edge.integrated.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object AIChat : Screen(
        route = "ai_chat",
        title = "AI Chat",
        icon = Icons.Default.SmartToy
    )

    data object Terminal : Screen(
        route = "terminal",
        title = "Terminal",
        icon = Icons.Default.Terminal
    )

    data object Winlator : Screen(
        route = "winlator",
        title = "Winlator",
        icon = Icons.Default.Android
    )

    data object OpenCode : Screen(
        route = "opencode",
        title = "OpenCode AI",
        icon = Icons.Default.Code
    )

    data object Models : Screen(
        route = "models",
        title = "Models",
        icon = Icons.Default.Memory
    )

    data object Settings : Screen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Chat
    )

    companion object {
        val bottomNavItems = listOf(AIChat, Terminal, Winlator, OpenCode)
    }
}
