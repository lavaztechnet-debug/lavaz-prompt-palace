package com.lavaz.promptpalace.data

data class Prompt(
    val id: Int,
    val title: String,
    val category: String,
    val content: String,
    val tags: String = "",
    val usageCount: Int = 0,
    val isFavorite: Boolean = false
)
