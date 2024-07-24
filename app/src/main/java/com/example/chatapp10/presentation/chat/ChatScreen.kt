package com.example.chatapp10.presentation.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp10.presentation.dialog.MessageDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    username: String?,
    viewModel: ChatViewModel = hiltViewModel()
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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "ChatApp",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )

        }
    ) { padding ->
        ChatList(viewModel = viewModel, username = username, padding)
    }
}

@Composable
fun ChatList(
    viewModel: ChatViewModel,
    username: String?,
    paddingValues: PaddingValues

) {
    val state = viewModel.state.value

    Column(Modifier.padding(4.dp)) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(paddingValues)
                .fillMaxWidth(), reverseLayout = true
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(state.messages) { message ->
                val isOwnMessage = message.username == username
                val id = message.id
                ChatMessage(message = message, isOwnMessage = isOwnMessage, viewModel)
            }
        }
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 4.dp, bottom = 16.dp, top = 8.dp)
                .fillMaxWidth()
        ) {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = viewModel.messageText.value,
                    onValueChange = viewModel::onMessageChange,
                    placeholder = {
                        Text(text = "Message..")
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    )
                )
                IconButton(onClick = viewModel::sendMessage) {
                    CompositionLocalProvider(LocalContentColor provides Color(0, 81, 212)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Message"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessage(
    message: Message,
    isOwnMessage: Boolean,
    viewModel: ChatViewModel
) {
    var messageText by rememberSaveable { mutableStateOf(message.message) }
    val isEditing = viewModel.editingMessage.value == message.id

    Box(
        contentAlignment = if (isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset ->
                        message.id?.let { viewModel.showMessageDialog(it) }
                    }
                )
            }
    ) {
        if (isEditing) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it }, modifier = Modifier.fillMaxSize()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = {
                        val editedMessage = message.copy(
                            message = messageText
                        )
                        viewModel.endEditMessage()
                    }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(onClick = {
                        val editedMessage = message.copy(
                            message = messageText
                        )
                        viewModel.editMessage(editedMessage)
                        viewModel.endEditMessage()
                    }) {
                        Text("Ok")
                    }
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .widthIn(min = 200.dp, max = 330.dp)
                        .background(
                            color = if (isOwnMessage) Color(115, 144, 191) else Color(45, 67, 128),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = message.username,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        if(message.edited == true){
                            Text(
                                text = "(edited)",
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                modifier = Modifier.padding(start = 6.dp, end = 4.dp, top = 2.dp, bottom = 2.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                    Text(
                        text = message.message,
                        color = Color.White,
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        text = message.formattedTime,
                        modifier = Modifier.align(Alignment.End),
                        color = Color.White
                    )

                }

                if (isOwnMessage && viewModel.selectedMessageId.value == message.id) {
                    message.id?.let {
                        MessageDialog(
                            viewModel = viewModel,
                            id = it,
                            onDismiss = { viewModel.hideMessageDialog() }
                        )
                    }
                }
            }
        }
    }
}

