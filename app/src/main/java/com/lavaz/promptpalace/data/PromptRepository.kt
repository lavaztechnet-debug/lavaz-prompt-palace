package com.lavaz.promptpalace.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PromptRepository(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("lavaz_prefs", Context.MODE_PRIVATE)
    
    private val _prompts = MutableStateFlow<List<Prompt>>(emptyList())
    val allPrompts: StateFlow<List<Prompt>> = _prompts.asStateFlow()

    init {
        loadPromptsFromAssets()
    }

    private fun loadPromptsFromAssets() {
        try {
            val json = context.assets.open("prompts.json").bufferedReader().use { it.readText() }
            val prompts = parseJsonPrompts(json)
            _prompts.value = prompts
        } catch (e: Exception) {
            e.printStackTrace()
            _prompts.value = emptyList()
        }
    }

    private fun parseJsonPrompts(json: String): List<Prompt> {
        return listOf(
            Prompt(1, "Write a Blog Post", "Writing", "Write a comprehensive blog post about {{topic}}.", "blog, writing", 0, false),
            Prompt(2, "Code Review", "Coding", "Review this code: {{code}}", "code, review", 0, false),
            Prompt(3, "Marketing Copy", "Marketing", "Generate marketing copy for {{product}}", "marketing", 0, false)
        )
    }

    var apiKey: String
        get() = prefs.getString("api_key", "") ?: ""
        set(value) = prefs.edit().putString("api_key", value).apply()

    var selectedModel: String
        get() = prefs.getString("selected_model", "nvidia/nemotron-3-ultra-550b-a55b:free") ?: ""
        set(value) = prefs.edit().putString("selected_model", value).apply()

    var darkMode: Boolean
        get() = prefs.getBoolean("dark_mode", false)
        set(value) = prefs.edit().putBoolean("dark_mode", value).apply()

    fun resetLocalData() = prefs.edit().clear().apply()
}
