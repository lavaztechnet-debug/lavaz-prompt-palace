package com.lavaz.promptpalace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lavaz.promptpalace.data.TodoEntity
import com.lavaz.promptpalace.data.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    private val _todos = MutableStateFlow<List<TodoEntity>>(emptyList())
    val todos: StateFlow<List<TodoEntity>> = _todos.asStateFlow()

    private val _filterState = MutableStateFlow<FilterState>(FilterState.ALL)
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            when (filterState.value) {
                FilterState.ALL -> repository.getAllTodos().collect { _todos.value = it }
                FilterState.ACTIVE -> repository.getActiveTodos().collect { _todos.value = it }
                FilterState.COMPLETED -> repository.getCompletedTodos().collect { _todos.value = it }
            }
        }
    }

    fun setFilter(filter: FilterState) {
        _filterState.value = filter
        loadTodos()
    }

    fun addTodo(title: String, description: String = "", priority: String = "Medium", dueDate: String? = null) {
        viewModelScope.launch {
            val todo = TodoEntity(
                title = title,
                description = description,
                priority = priority,
                dueDate = dueDate
            )
            repository.insertTodo(todo)
        }
    }

    fun updateTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.updateTodo(todo)
        }
    }

    fun toggleTodoCompletion(todo: TodoEntity) {
        viewModelScope.launch {
            repository.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
        }
    }

    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }

    fun deleteCompletedTodos() {
        viewModelScope.launch {
            repository.deleteCompletedTodos()
        }
    }
}

enum class FilterState {
    ALL, ACTIVE, COMPLETED
}

class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
