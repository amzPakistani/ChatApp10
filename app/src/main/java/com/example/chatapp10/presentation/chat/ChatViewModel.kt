package com.example.chatapp10.presentation.chat

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.remember

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp10.data.remote.ChatSocketService
import com.example.chatapp10.data.remote.MessageService
import com.example.chatapp10.domain.model.Message
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

    private val _editingMessage = mutableStateOf<String?>(null)
    val editingMessage: State<String?> = _editingMessage



    fun connectToChat() {
        getAllMessages()
        savedStateHandle.get<String>("username")?.let { username ->
            viewModelScope.launch {
                val result = chatSocketService.initSession(username)
                when (result) {
                    is Resource.Error -> {
                        _toastEvent.emit(result.message ?: "Unknown Error")
                    }
                    is Resource.Success -> {
                        chatSocketService.observeMessages().onEach { message ->
                            val updatedMessages = when (message.action) {
                                "delete" -> {
                                    _state.value.messages.filter { it.id != message.id }
                                }
                                else -> {
                                    val currentMessages = _state.value.messages.toMutableList()
                                    val index = currentMessages.indexOfFirst { it.id == message.id }

                                    if (index != -1) {
                                        currentMessages[index] = message
                                    } else {
                                        currentMessages.add(0, message)
                                    }
                                    currentMessages
                                }
                            }

                            _state.value = _state.value.copy(messages = updatedMessages)
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
                _state.value = _state.value.copy(messages = _state.value.messages)
            }
        }
    }

    fun editMessage(message: Message) {
        viewModelScope.launch {
            try {
                messageService.editMessage(message)
                val updatedList = _state.value.messages.map { existingMessage ->
                    if (existingMessage.id == message.id) {
                        message.copy(edited = true)
                    } else {
                        existingMessage
                    }
                }
                _state.value = _state.value.copy(messages = updatedList)
            } catch (e: Exception) {
                e.printStackTrace()
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

    fun startEditMessage(messageId: String){
        _editingMessage.value = messageId
    }

    fun endEditMessage(){
        _editingMessage.value = null
    }
    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}