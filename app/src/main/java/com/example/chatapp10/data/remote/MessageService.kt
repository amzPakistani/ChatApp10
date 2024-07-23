package com.example.chatapp10.data.remote

import com.example.chatapp10.data.remote.ChatSocketService.Companion
import com.example.chatapp10.data.remote.ChatSocketService.Endpoint
import com.example.chatapp10.domain.model.Message

interface MessageService {
    suspend fun getAllMessages():List<Message>
    suspend fun deleteMessage(id:String)
    suspend fun editMessage(message:Message)

    companion object{
        const val BASE_URL = "http://192.168.0.159:8080"
    }

    sealed class Endpoints(val url:String){
        data object GetAllMessages:Endpoints("${BASE_URL}/messages")
        data object Delete: Endpoint("${BASE_URL}/delete_message")
        data object Edit: Endpoint("${BASE_URL}/edit_message")
    }
}