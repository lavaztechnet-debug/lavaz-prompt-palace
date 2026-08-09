package com.lavaz.promptpalace.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lavaz.promptpalace.data.PromptCollection
@Composable
fun CollectionsScreen(collections: List<PromptCollection>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(collections) { collection ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(collection.name, style = MaterialTheme.typography.titleMedium)
                    Text("${collection.promptIds.size} prompts")
                }
            }
        }
    }
}
