package com.lavaz.promptpalace.data
import kotlinx.serialization.Serializable
@Serializable data class PromptCollection(val id:String,val name:String,val promptIds:List<Int> = emptyList())
