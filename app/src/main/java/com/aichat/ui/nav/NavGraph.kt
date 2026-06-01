package com.aichat.ui.nav

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aichat.AiChatApp
import com.aichat.ui.ChatViewModel
import com.aichat.ui.chat.ChatScreen
import com.aichat.ui.main.ChatListScreen
import com.aichat.ui.settings.SettingsScreen

@Composable
fun AiChatNavGraph() {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as AiChatApp
    val viewModel: ChatViewModel = viewModel(
        factory = ChatViewModel.factory(
            app.container.chatRepository,
            app.container.apiKeyRepository,
            app.container.aiClientRepository
        )
    )

    NavHost(navController = navController, startDestination = "chat_list") {
        composable("chat_list") {
            ChatListScreen(
                viewModel = viewModel,
                onNavigateToChat = { conversation ->
                    viewModel.selectConversation(conversation)
                    navController.navigate("chat")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }
        composable("chat") {
            ChatScreen(
                viewModel = viewModel,
                onBack = {
                    viewModel.backToList()
                    navController.popBackStack()
                }
            )
        }
        composable("settings") {
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
