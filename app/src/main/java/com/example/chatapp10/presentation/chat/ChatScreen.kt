package com.example.chatapp10.presentation.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.chatapp10.domain.model.Message
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField


@Composable
fun ChatScreen(
    username: String?,
    viewModel: ChatViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = true) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.connectToChat()
            } else if (event == Lifecycle.Event.ON_STOP) {
                viewModel.disconnect()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    ChatList(viewModel = viewModel, username = username)
}

@Composable
fun ChatList(
    viewModel: ChatViewModel,
    username: String?
) {
    val state = viewModel.state.value

    Column {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), reverseLayout = true
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(state.messages) { message ->
                val isOwnMessage = message.username == username
                ChatMessage(message = message, isOwnMessage = isOwnMessage)
            }
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            Row {
                TextField(
                    value = viewModel.messageText.value,
                    onValueChange = viewModel::onMessageChange,
                    placeholder = {
                        Text(text = "Message..")
                    },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = viewModel::sendMessage) {
                    Icon(imageVector = Icons.Filled.Send, contentDescription = "Send Message")
                }
            }
        }
    }
}

@Composable
fun ChatMessage(
    message: Message,
    isOwnMessage: Boolean
) {
    Box(
        contentAlignment = if (isOwnMessage) {
            Alignment.CenterEnd
        } else Alignment.CenterStart,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .width(200.dp)
                .background(color = if (isOwnMessage) Color.LightGray else Color.DarkGray)
                .padding(8.dp)
        ) {
            Text(
                text = message.username,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = message.message,
                color = Color.White
            )
            Text(
                text = message.formattedTime,
                modifier = Modifier.align(Alignment.End),
                color = Color.White
            )
        }
    }
}