package com.lavaz.promptpalace
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.lavaz.promptpalace.data.ChatMessage
import com.lavaz.promptpalace.data.PromptRepository
import com.lavaz.promptpalace.ui.screens.*
import com.lavaz.promptpalace.ui.theme.LavazPromptPalaceTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val repository = PromptRepository(applicationContext)
        setContent {
            LavazPromptPalaceTheme(darkTheme = repository.darkMode) {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { AppRoot(repository) }
            }
        }
    }
}
private enum class Tab { Home, Chat, Templates, Collections, Settings }
@Composable private fun AppRoot(repository: PromptRepository) {
    var tab by remember { mutableStateOf(Tab.Home) }
    Scaffold(bottomBar = {
        NavigationBar {
            NavigationBarItem(selected = tab == Tab.Home, onClick = { tab = Tab.Home }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") })
            NavigationBarItem(selected = tab == Tab.Chat, onClick = { tab = Tab.Chat }, icon = { Icon(Icons.Default.Chat, null) }, label = { Text("Chat") })
            NavigationBarItem(selected = tab == Tab.Templates, onClick = { tab = Tab.Templates }, icon = { Icon(Icons.Default.Bookmarks, null) }, label = { Text("Templates") })
            NavigationBarItem(selected = tab == Tab.Collections, onClick = { tab = Tab.Collections }, icon = { Icon(Icons.Default.Collections, null) }, label = { Text("Collections") })
            NavigationBarItem(selected = tab == Tab.Settings, onClick = { tab = Tab.Settings }, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Settings") })
        }
    }) {
        when (tab) {
            Tab.Home -> HomeScreen(repository.prompts.collectAsState().value, "", "All", {}, {}, { repository.toggleFavorite(it) }, { repository.incrementUsage(it.id) })
            Tab.Chat -> ChatScreen(repository.messages.collectAsState().value, { repository.addChatMessage(ChatMessage(it, true)); repository.addChatMessage(ChatMessage("Demo response for: $it", false)) }, { repository.clearChat() })
            Tab.Templates -> TemplatesScreen(repository.templates.collectAsState().value)
            Tab.Collections -> CollectionsScreen(repository.collections.collectAsState().value)
            Tab.Settings -> SettingsScreen(repository.apiKey, repository.selectedModel, repository.darkMode, repository.temperature, { repository.apiKey = it }, { repository.selectedModel = it }, { repository.darkMode = it }, { repository.temperature = it }, { repository.resetLocalData() })
        }
    }
}
