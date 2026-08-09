package com.lavaz.promptpalace.data
import kotlinx.serialization.Serializable
@Serializable data class Prompt(val id:Int,val title:String,val category:String,val content:String,val tags:List<String> = emptyList(),val usageCount:Int = 0,val isFavorite:Boolean = false)
