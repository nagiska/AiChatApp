package com.aichat.ui.theme

import androidx.compose.runtime.Composable
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController
import top.yukonga.miuix.kmp.theme.ColorSchemeMode

// 应用主题配置
@Composable
fun AiChatTheme(
    content: @Composable () -> Unit
) {
    // 使用 Miuix 主题，支持浅色/深色/Monet 动态取色
    val controller = ThemeController(ColorSchemeMode.System)
    MiuixTheme(
        controller = controller,
        content = content
    )
}

// 主题模式枚举
enum class ThemeMode {
    SYSTEM,  // 跟随系统
    LIGHT,   // 浅色模式
    DARK,    // 深色模式
    MONET    // Monet 动态取色
}
