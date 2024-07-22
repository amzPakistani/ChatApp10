package com.example.chatapp10.presentation.username

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun UsernameScreen(
    viewModel: UsernameViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.onJoinChat.collectLatest { username ->
            onNavigate("chat_screen/${username}")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            TextField(
                value = viewModel.usernameText.value,
                onValueChange = viewModel::onUsernameChange,
                placeholder = {
                    Text("Enter username")
                },
                modifier = Modifier.padding().fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = viewModel::onJoinClick
            ) {
                Text(text = "Join Chat")
            }
        }
    }

}