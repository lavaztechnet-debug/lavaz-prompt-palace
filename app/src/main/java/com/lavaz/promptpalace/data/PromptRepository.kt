package com.lavaz.promptpalace.data
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
class PromptRepository(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("lavaz_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }
    private val _prompts = MutableStateFlow<List<Prompt>>(emptyList())
    val prompts: StateFlow<List<Prompt>> = _prompts.asStateFlow()
    private val _templates = MutableStateFlow<List<Template>>(emptyList())
    val templates: StateFlow<List<Template>> = _templates.asStateFlow()
    private val _collections = MutableStateFlow<List<PromptCollection>>(emptyList())
    val collections: StateFlow<List<PromptCollection>> = _collections.asStateFlow()
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    init { loadAll() }
    fun loadAll() {
        runCatching {
            val text = context.assets.open("prompts.json").bufferedReader().use { it.readText() }
            _prompts.value = json.decodeFromString(ListSerializer(Prompt.serializer()), text)
        }.onFailure {
            _prompts.value = listOf(
                Prompt(1,"Write a Blog Post","Writing","Write a comprehensive blog post about {{topic}}.",listOf("blog","writing")),
                Prompt(2,"Code Review Assistant","Coding","Review this code for bugs and best practices: {{code}}.",listOf("code","review")),
                Prompt(3,"Marketing Copy Generator","Marketing","Generate compelling marketing copy for {{product}}.",listOf("marketing"))
            )
        }
        _templates.value = listOf(Template("t1","Idea Expander","Writing","Expand this idea into a detailed outline: {{idea}}"), Template("t2","Debug Helper","Coding","Find bugs and suggest fixes for: {{code}}"))
        _collections.value = listOf(PromptCollection("c1","Favorites"), PromptCollection("c2","Marketing"), PromptCollection("c3","Coding"))
    }
    var apiKey: String get() = prefs.getString("api_key","") ?: "" set(v) = prefs.edit().putString("api_key", v).apply()
    var selectedModel: String get() = prefs.getString("selected_model","nvidia/nemotron-3-ultra-550b-a55b:free") ?: "" set(v) = prefs.edit().putString("selected_model", v).apply()
    var darkMode: Boolean get() = prefs.getBoolean("dark_mode", false) set(v) = prefs.edit().putBoolean("dark_mode", v).apply()
    var temperature: Float get() = prefs.getFloat("temperature", 0.7f) set(v) = prefs.edit().putFloat("temperature", v).apply()
    fun addChatMessage(message: ChatMessage) { _messages.value = _messages.value + message }
    fun clearChat() { _messages.value = emptyList() }
    fun toggleFavorite(promptId: Int) { _prompts.value = _prompts.value.map { if (it.id == promptId) it.copy(isFavorite = !it.isFavorite) else it } }
    fun incrementUsage(promptId: Int) { _prompts.value = _prompts.value.map { if (it.id == promptId) it.copy(usageCount = it.usageCount + 1) else it } }
    fun resetLocalData() { prefs.edit().clear().apply(); clearChat(); loadAll() }
}
