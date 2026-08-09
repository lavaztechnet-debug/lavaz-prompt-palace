package com.lavaz.promptpalace.data
import kotlinx.serialization.Serializable
@Serializable data class Template(val id:String,val name:String,val category:String,val templateBody:String)
