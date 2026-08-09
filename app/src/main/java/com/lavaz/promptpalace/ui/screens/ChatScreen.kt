package com.lavaz.promptpalace.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lavaz.promptpalace.data.PromptRepository

data class ChatMessage(val content: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    repository: PromptRepository,
    initialPrompt: String = "",
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var inputText by remember { mutableStateOf(initialPrompt) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(initialPrompt) {
        if (initialPrompt.isNotEmpty()) {
            messages = listOf(ChatMessage(initialPrompt, true))
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AI Chat") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { message ->
                        ChatBubble(message)
                    }
                    if (isLoading) {
                        item {
                            ChatBubble(ChatMessage("Thinking...", false), isLoading = true)
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type a message...") },
                        maxLines = 4
                    )
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                messages = messages + ChatMessage(inputText, true)
                                isLoading = true
                                inputText = ""
                                // Simulate AI response
                                kotlinx.coroutines.GlobalScope.launch {
                                    kotlinx.coroutines.delay(1000)
                                    isLoading = false
                                    messages = messages + ChatMessage("This is a demo response. Add OpenRouter API integration for real AI!", false)
                                }
                            }
                        },
                        enabled = inputText.isNotBlank() && !isLoading
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (message.isUser) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = if (message.isUser) "You" else "Assistant",
                style = MaterialTheme.typography.labelSmall,
                color = if (message.isUser)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (isLoading) {
                LinearProgressIndicator()
            } else {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
