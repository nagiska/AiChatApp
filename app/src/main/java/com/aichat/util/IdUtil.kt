package com.aichat.util

import java.util.UUID

// ID 生成工具
object IdUtil {
    
    // 生成唯一 ID
    fun generateId(): String = UUID.randomUUID().toString()
    
    // 生成短 ID（8位）
    fun generateShortId(): String = UUID.randomUUID().toString().take(8)
}
