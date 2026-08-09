package com.lavaz.promptpalace.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lavaz.promptpalace.data.PromptRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    repository: PromptRepository,
    modifier: Modifier = Modifier
) {
    var apiKey by remember { mutableStateOf(repository.apiKey) }
    var darkMode by remember { mutableStateOf(repository.darkMode) }
    var showApiKey by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Settings") })
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                text = "OpenRouter API Key",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = apiKey,
                                onValueChange = { 
                                    apiKey = it
                                    repository.apiKey = it
                                },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("API Key") },
                                visualTransformation = if (showApiKey) 
                                    androidx.compose.ui.text.input.PasswordVisualTransformation() 
                                else 
                                    androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { showApiKey = !showApiKey }) {
                                        Icon(
                                            if (showApiKey) Icons.Default.Visibility 
                                            else Icons.Default.VisibilityOff,
                                            contentDescription = if (showApiKey) "Hide" else "Show"
                                        )
                                    }
                                }
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Dark Mode",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Switch(
                                checked = darkMode,
                                onCheckedChange = { 
                                    darkMode = it
                                    repository.darkMode = it
                                }
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Reset Local Data",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            TextButton(
                                onClick = { 
                                    repository.resetLocalData()
                                    apiKey = ""
                                }
                            ) {
                                Text("Reset")
                            }
                        }
                    }
                }
            }
        }
    }
}
