package com.example.chatapp10.data.remote

import com.example.chatapp10.data.remote.dto.MessageDto
import com.example.chatapp10.domain.model.Message
import com.example.chatapp10.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.WebSocketSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json


class ChatSocketServiceImpl(val client: HttpClient):ChatSocketService {
    private var socket:WebSocketSession? = null

    override suspend fun initSession(username: String): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("${ChatSocketService.Endpoint.ChatSocket.url}?username=${username}")
            }
            if (socket?.isActive==true){
                Resource.Success(Unit)
            }else{
                Resource.Error("Can't Connect")
            }
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Error(e.localizedMessage?:"Unknown Error")
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun observeMessages(): Flow<Message> {
        return try {
            socket?.incoming?.receiveAsFlow()?.map {
                val json= (it as? Frame.Text)?.readText()?:""
                val messageDto = Json.decodeFromString<MessageDto>(json)
                messageDto.toMessage()
            }?:flow{}
        }catch (e:Exception){
            e.printStackTrace()
            flow {}
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}