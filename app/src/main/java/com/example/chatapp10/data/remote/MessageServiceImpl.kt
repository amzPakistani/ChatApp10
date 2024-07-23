package com.example.chatapp10.data.remote

import com.example.chatapp10.data.remote.dto.MessageDto
import com.example.chatapp10.domain.model.Message
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class MessageServiceImpl(private val client:HttpClient):MessageService {
    override suspend fun getAllMessages(): List<Message> = withContext(Dispatchers.IO) {
        val response: HttpResponse = client.get(MessageService.Endpoints.GetAllMessages.url) {}
        if (response.status.isSuccess()) {
            val responseBody = response.bodyAsText()
            val messageDto = Json.decodeFromString(ListSerializer(MessageDto.serializer()), responseBody)
            messageDto.map { it.toMessage() }
        } else {
            emptyList()
        }
    }


    override suspend fun deleteMessage(id: String) {
        client.delete("${MessageService.Endpoints.Delete.url}/$id")
    }

    override suspend fun editMessage(message: Message) {
        client.post(MessageService.Endpoints.Edit.url)
    }
}