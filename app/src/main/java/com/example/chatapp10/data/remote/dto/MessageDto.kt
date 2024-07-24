package com.example.chatapp10.data.remote.dto

import android.text.format.DateFormat
import com.example.chatapp10.domain.model.Message
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class MessageDto(
    val username:String,
    val id:String,
    val message:String,
    val timeStamp:Long,
    val edited:Boolean? = null
){
    fun toMessage(): Message {
        val date = Date(timeStamp)
        val formattedDate = java.text.DateFormat.getDateInstance().format(date)
        return Message(
            username = username,
            message = message,
            formattedTime = formattedDate,
            id = id,
            edited = edited
        )
    }
}
