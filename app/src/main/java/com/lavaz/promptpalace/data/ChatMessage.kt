package com.lavaz.promptpalace.data
import kotlinx.serialization.Serializable
@Serializable data class ChatMessage(val content:String,val isUser:Boolean,val timestamp:Long = System.currentTimeMillis())
