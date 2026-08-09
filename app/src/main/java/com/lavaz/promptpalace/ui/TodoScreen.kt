package com.lavaz.promptpalace.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.lavaz.promptpalace.data.TodoEntity
import com.lavaz.promptpalace.viewmodel.FilterState
import com.lavaz.promptpalace.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val todos by viewModel.todos.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var titleInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var priorityInput by remember { mutableStateOf("Medium") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My To-Do List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = filterState == FilterState.ALL,
                    onClick = { viewModel.setFilter(FilterState.ALL) },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = filterState == FilterState.ACTIVE,
                    onClick = { viewModel.setFilter(FilterState.ACTIVE) },
                    label = { Text("Active") }
                )
                FilterChip(
                    selected = filterState == FilterState.COMPLETED,
                    onClick = { viewModel.setFilter(FilterState.COMPLETED) },
                    label = { Text("Completed") }
                )
            }

            // Todo List
            if (todos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No todos yet. Add one to get started!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(todos) { todo ->
                        TodoItem(todo, viewModel)
                    }
                }

                // Clear Completed Button
                if (filterState == FilterState.ALL && todos.any { it.isCompleted }) {
                    Button(
                        onClick = { viewModel.deleteCompletedTodos() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("Clear Completed")
                    }
                }
            }
        }
    }

    // Add Todo Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Todo") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = descriptionInput,
                        onValueChange = { descriptionInput = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 60.dp)
                    )
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        TextField(
                            value = priorityInput,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Priority") },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("Low", "Medium", "High").forEach { priority ->
                                DropdownMenuItem(
                                    text = { Text(priority) },
                                    onClick = {
                                        priorityInput = priority
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (titleInput.isNotBlank()) {
                            viewModel.addTodo(titleInput, descriptionInput, priorityInput)
                            titleInput = ""
                            descriptionInput = ""
                            priorityInput = "Medium"
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TodoItem(todo: TodoEntity, viewModel: TodoViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = todo.isCompleted,
                    onCheckedChange = { viewModel.toggleTodoCompletion(todo) }
                )
                Column {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null
                    )
                    if (todo.description.isNotEmpty()) {
                        Text(
                            text = todo.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AssistChip(
                            onClick = {},
                            label = { Text(todo.priority, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.heightIn(max = 24.dp)
                        )
                    }
                }
            }
            IconButton(onClick = { viewModel.deleteTodo(todo) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
