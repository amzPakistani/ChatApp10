package com.example.chatapp10.presentation.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.chatapp10.domain.model.Message
import com.example.chatapp10.presentation.chat.ChatViewModel

@Composable
fun MessageDialog(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier,
    id: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .width(150.dp)
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.deleteMessage(id)
                        onDismiss()
                    }.padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Delete ")
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Message")
            }
            HorizontalDivider(
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp).fillMaxWidth(),
                thickness = 2.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.startEditMessage(id)
                        viewModel.hideMessageDialog()
                        onDismiss()
                    }.padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Edit ")
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Message")
            }
        }
    }
}

