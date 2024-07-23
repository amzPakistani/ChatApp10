package com.example.chatapp10.data.remote

import com.example.chatapp10.domain.model.Message
import com.example.chatapp10.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {
    suspend fun initSession(username:String):Resource<Unit>
    suspend fun sendMessage(message: String)
    fun observeMessages(): Flow<Message>
    suspend fun closeSession()
    companion object{
        const val BASE_URL = "ws://192.168.0.159:8080"
    }

    sealed class Endpoint(val url:String){
        data object ChatSocket:Endpoint("${BASE_URL}/chat-socket")
    }
}