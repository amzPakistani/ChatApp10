package com.example.chatapp10.presentation.chat

import com.example.chatapp10.domain.model.Message

data class ChatState(
    val messages:List<Message> = emptyList(),
    val isLoading:Boolean = false
)