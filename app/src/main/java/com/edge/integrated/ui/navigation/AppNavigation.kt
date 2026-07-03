package com.edge.integrated.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.edge.integrated.ui.ai.AIChatScreen
import com.edge.integrated.ui.model.ModelManagerScreen
import com.edge.integrated.ui.opencode.OpencodeScreen
import com.edge.integrated.ui.settings.SettingsScreen
import com.edge.integrated.ui.terminal.TerminalScreen
import com.edge.integrated.ui.winlator.WinlatorScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = Screen.bottomNavItems.any { screen ->
        currentDestination?.hierarchy?.any { it.route == screen.route } == true
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar {
                    Screen.bottomNavItems.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true

                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.AIChat.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.AIChat.route) {
                AIChatScreen(
                    onNavigateToModels = {
                        navController.navigate(Screen.Models.route)
                    }
                )
            }
            composable(Screen.Terminal.route) {
                TerminalScreen()
            }
            composable(Screen.Winlator.route) {
                WinlatorScreen()
            }
            composable(Screen.OpenCode.route) {
                OpencodeScreen()
            }
            composable(Screen.Models.route) {
                ModelManagerScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
