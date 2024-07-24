package com.example.chatapp10.domain.model

import kotlinx.serialization.Serializable

data class Message(
    val username:String,
    val formattedTime:String,
    val message: String,
    val id: String? = null,
    val edited:Boolean? = null,
)
