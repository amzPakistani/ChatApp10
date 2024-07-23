package com.example.chatapp10.presentation.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatapp10.domain.model.Message
import com.example.chatapp10.presentation.chat.ChatViewModel

@Composable
fun MessageDialog(viewModel: ChatViewModel, modifier: Modifier = Modifier, id: String, onDismiss: () -> Unit, message: Message) {
    Card(modifier = modifier.padding(24.dp).size(width = 150.dp, height = 250.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(modifier = Modifier.clickable {
                viewModel.deleteMessage(id)
                onDismiss()
            }) {
                Text(text = "Delete")
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Message")
            }
            Row(modifier = Modifier.clickable {
                viewModel.startEditMessage(id)
                viewModel.hideMessageDialog()
                onDismiss()
            }) {
                Text(text = "Edit")
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Message")
            }
        }
    }
}