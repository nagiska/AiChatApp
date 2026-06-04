package com.aichat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aichat.ui.screen.agent.AgentScreen
import com.aichat.ui.screen.chat.ChatScreen
import com.aichat.ui.screen.home.HomeScreen
import com.aichat.ui.screen.provider.ProviderScreen
import com.aichat.ui.screen.settings.SettingsScreen

// 导航图配置
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // 首页
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToChat = { conversationId ->
                    navController.navigate(Screen.Chat.createRoute(conversationId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToAgents = {
                    navController.navigate(Screen.Agents.route)
                }
            )
        }

        // 聊天页面
        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
            ChatScreen(
                conversationId = conversationId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 设置页面
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProviders = {
                    navController.navigate(Screen.Providers.route)
                }
            )
        }

        // 供应商管理页面
        composable(Screen.Providers.route) {
            ProviderScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { providerId ->
                    if (providerId != null) {
                        navController.navigate(Screen.ProviderEdit.createRoute(providerId))
                    } else {
                        navController.navigate(Screen.ProviderEdit.createNew())
                    }
                }
            )
        }

        // Agent 管理页面
        composable(Screen.Agents.route) {
            AgentScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChat = { agentId ->
                    // TODO: 使用 Agent 创建新会话并跳转
                }
            )
        }
    }
}
