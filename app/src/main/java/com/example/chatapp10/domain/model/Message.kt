package com.example.chatapp10.domain.model

data class Message(
    val username:String,
    val formattedTime:String,
    val message: String,
    val id: String? = null,
    val edited:Boolean = false
)
