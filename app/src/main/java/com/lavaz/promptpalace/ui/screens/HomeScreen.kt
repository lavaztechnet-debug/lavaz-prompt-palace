package com.lavaz.promptpalace.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lavaz.promptpalace.data.Prompt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(prompts: List<Prompt>, searchQuery: String, selectedCategory: String, onSearchQueryChange: (String) -> Unit, onCategoryChange: (String) -> Unit, onToggleFavorite: (Int) -> Unit, onUsePrompt: (Prompt) -> Unit) {
    val categories = listOf("All","Writing","Coding","Marketing","Productivity")
    val filtered = prompts.filter { (selectedCategory == "All" || it.category == selectedCategory) && (searchQuery.isBlank() || it.title.contains(searchQuery, true) || it.content.contains(searchQuery, true) || it.tags.any { tag -> tag.contains(searchQuery, true) }) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = searchQuery, onValueChange = onSearchQueryChange, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Search prompts...") }, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) })
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 88.dp)) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { category -> FilterChip(selected = selectedCategory == category, onClick = { onCategoryChange(category) }, label = { Text(category) }) }
                }
            }
            items(filtered) { prompt ->
                Card(modifier = Modifier.fillMaxWidth(), onClick = { onUsePrompt(prompt) }) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(prompt.title, style = MaterialTheme.typography.titleMedium)
                            IconButton(onClick = { onToggleFavorite(prompt.id) }) { Icon(if (prompt.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = null) }
                        }
                        Text(prompt.category, style = MaterialTheme.typography.labelMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(prompt.content)
                    }
                }
            }
        }
    }
}
