package com.lavaz.promptpalace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.lavaz.promptpalace.data.TodoDatabase
import com.lavaz.promptpalace.data.TodoRepository
import com.lavaz.promptpalace.ui.TodoScreen
import com.lavaz.promptpalace.ui.theme.LavazPromptPalaceTheme
import com.lavaz.promptpalace.viewmodel.TodoViewModel
import com.lavaz.promptpalace.viewmodel.TodoViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database, repository, and viewmodel
        val database = TodoDatabase.getDatabase(applicationContext)
        val repository = TodoRepository(database.todoDao())
        val factory = TodoViewModelFactory(repository)
        todoViewModel = ViewModelProvider(this, factory).get(TodoViewModel::class.java)

        setContent {
            LavazPromptPalaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoScreen(todoViewModel)
                }
            }
        }
    }
}
