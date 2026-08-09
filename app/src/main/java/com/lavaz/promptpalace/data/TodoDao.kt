package com.lavaz.promptpalace.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity): Long

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Int): TodoEntity?

    @Query("SELECT * FROM todos WHERE isCompleted = 0 ORDER BY createdAt DESC")
    fun getActiveTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedTodos(): Flow<List<TodoEntity>>

    @Query("DELETE FROM todos WHERE isCompleted = 1")
    suspend fun deleteCompletedTodos()
}
