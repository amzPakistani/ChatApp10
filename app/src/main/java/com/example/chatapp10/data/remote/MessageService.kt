package com.example.chatapp10.data.remote

import com.example.chatapp10.domain.model.Message

interface MessageService {
    suspend fun getAllMessages():List<Message>

    companion object{
        const val BASE_URL = "http://192.168.0.159:8080"
    }

    sealed class Endpoints(val url:String){
        data object GetAllMessages:Endpoints("${BASE_URL}/messages")
    }
}