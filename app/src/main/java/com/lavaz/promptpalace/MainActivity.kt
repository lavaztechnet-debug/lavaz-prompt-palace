package com.lavaz.promptpalace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.lavaz.promptpalace.data.PromptRepository
import com.lavaz.promptpalace.ui.screens.ChatScreen
import com.lavaz.promptpalace.ui.screens.HomeScreen
import com.lavaz.promptpalace.ui.screens.SettingsScreen
import com.lavaz.promptpalace.ui.theme.LavazPromptPalaceTheme

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Chat : Screen("chat")
    object Settings : Screen("settings")
}

class MainActivity : ComponentActivity() {
    private lateinit var repository: PromptRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        repository = PromptRepository(applicationContext)
        setContent {
            LavazPromptPalaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent(repository)
                }
            }
        }
    }
}

@Composable
fun AppContent(repository: PromptRepository) {
    var currentScreen by remember { mutableStateOf(Screen.Home) }
    var selectedPrompt by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = currentScreen == Screen.Home,
                    onClick = { currentScreen = Screen.Home }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = currentScreen == Screen.Chat,
                    onClick = { currentScreen = Screen.Chat }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = currentScreen == Screen.Settings,
                    onClick = { currentScreen = Screen.Settings }
                )
            }
        }
    ) { innerPadding ->
        when (currentScreen) {
            Screen.Home -> HomeScreen(
                repository = repository,
                onPromptClick = { prompt ->
                    selectedPrompt = prompt.content
                    currentScreen = Screen.Chat
                },
                modifier = Modifier.padding(innerPadding)
            )
            Screen.Chat -> ChatScreen(
                repository = repository,
                initialPrompt = selectedPrompt ?: "",
                onBack = { 
                    selectedPrompt = null
                    currentScreen = Screen.Home 
                },
                modifier = Modifier.padding(innerPadding)
            )
            Screen.Settings -> SettingsScreen(
                repository = repository,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
