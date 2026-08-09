package com.lavaz.promptpalace.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val createdAt: String = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
    val dueDate: String? = null,
    val priority: String = "Medium" // Low, Medium, High
)
