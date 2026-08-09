package com.lavaz.promptpalace.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lavaz.promptpalace.data.Prompt
import com.lavaz.promptpalace.data.PromptRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    repository: PromptRepository,
    onPromptClick: (Prompt) -> Unit,
    modifier: Modifier = Modifier
) {
    val prompts by repository.allPrompts.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Lavaz Prompt Palace") })
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search prompts...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        }
                    )
                }
                
                items(prompts) { prompt ->
                    PromptCard(
                        prompt = prompt,
                        onClick = { onPromptClick(prompt) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PromptCard(
    prompt: Prompt,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = prompt.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = prompt.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = prompt.content.take(100) + if (prompt.content.length > 100) "..." else "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
