package com.example.chatapp10.presentation.chat

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp10.data.remote.ChatSocketService
import com.example.chatapp10.data.remote.MessageService
import com.example.chatapp10.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    val messageService: MessageService,
    val chatSocketService: ChatSocketService,
    val savedStateHandle: SavedStateHandle
):ViewModel() {
    private val _messageText = mutableStateOf("")
    val messageText:State<String> = _messageText

    private val _state = mutableStateOf(ChatState())
    val state:State<ChatState> = _state

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun connectToChat(){
        getAllMessages()
        savedStateHandle.get<String>("username")?.let { username ->
            viewModelScope.launch {
                val result = chatSocketService.initSession(username)
                when(result){
                    is Resource.Error -> {
                        _toastEvent.emit(result.message?:"Unknown Error")
                    }
                    is Resource.Success -> {
                        chatSocketService.observeMessages().onEach { message ->
                            val newList = state.value.messages.toMutableList().apply {
                                add(0, message)
                            }
                            _state.value = state.value.copy(
                                messages = newList
                            )
                        }.launchIn(viewModelScope)
                    }
                }
            }
        }
    }

    fun onMessageChange(message:String){
        _messageText.value = message
    }
    fun disconnect(){
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }

    fun sendMessage(){
        viewModelScope.launch {
            if(messageText.value.isNotBlank()){
                chatSocketService.sendMessage(messageText.value)
            }
        }
    }

    fun getAllMessages(){
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val result = messageService.getAllMessages()
            _state.value = state.value.copy(
                isLoading = false,
                messages = result
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}