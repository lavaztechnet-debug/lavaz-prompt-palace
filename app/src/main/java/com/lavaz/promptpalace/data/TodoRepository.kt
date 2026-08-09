package com.lavaz.promptpalace.data

import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    fun getAllTodos(): Flow<List<TodoEntity>> = todoDao.getAllTodos()

    fun getActiveTodos(): Flow<List<TodoEntity>> = todoDao.getActiveTodos()

    fun getCompletedTodos(): Flow<List<TodoEntity>> = todoDao.getCompletedTodos()

    suspend fun insertTodo(todo: TodoEntity) = todoDao.insertTodo(todo)

    suspend fun updateTodo(todo: TodoEntity) = todoDao.updateTodo(todo)

    suspend fun deleteTodo(todo: TodoEntity) = todoDao.deleteTodo(todo)

    suspend fun getTodoById(id: Int) = todoDao.getTodoById(id)

    suspend fun deleteCompletedTodos() = todoDao.deleteCompletedTodos()
}
