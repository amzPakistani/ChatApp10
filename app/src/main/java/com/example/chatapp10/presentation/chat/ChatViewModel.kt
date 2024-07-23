package com.example.chatapp10.presentation.chat

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.remember

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp10.data.remote.ChatSocketService
import com.example.chatapp10.data.remote.MessageService
import com.example.chatapp10.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle
):ViewModel() {
    private val _messageText = mutableStateOf("")
    val messageText:State<String> = _messageText

    private val _state = mutableStateOf(ChatState())
    val state:State<ChatState> = _state

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    private val _selectedMessageId = mutableStateOf<String?>(null)
    val selectedMessageId: State<String?> = _selectedMessageId


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

    fun deleteMessage(id: String) {
        viewModelScope.launch {
            id.let {
                messageService.deleteMessage(it)
                val updatedMessages = _state.value.messages.filter { message -> message.id != id }
                _state.value = _state.value.copy(messages = updatedMessages)
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
                _messageText.value = ""
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

    fun showMessageDialog(messageId: String) {
        _selectedMessageId.value = messageId
    }

    fun hideMessageDialog() {
        _selectedMessageId.value = null
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}