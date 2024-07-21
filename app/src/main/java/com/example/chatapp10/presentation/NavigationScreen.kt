package com.example.chatapp10.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatapp10.presentation.chat.ChatScreen
import com.example.chatapp10.presentation.username.UsernameScreen

@Composable
fun NavigationScreen(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "username_screen" ) {
        composable("username_screen") {
            UsernameScreen(onNavigate = navController::navigate)
        }
        composable(
            route = "chat_screen/{username}",
            arguments = listOf(
                navArgument(name = "username") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            val username = it.arguments?.getString("username")
            ChatScreen(username = username)
        }
    }
}