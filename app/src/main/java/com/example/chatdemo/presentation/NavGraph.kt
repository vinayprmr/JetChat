package com.example.chatdemo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatdemo.presentation.chat.ChatScreen
import com.example.chatdemo.presentation.HomeScreen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = "navGraph",
        startDestination = "home"
    ) {
        composable(route ="home") {
            HomeScreen(navController = navController)
        }
        composable(route ="chat") {
            ChatScreen(navController = navController)
        }
    }
}
