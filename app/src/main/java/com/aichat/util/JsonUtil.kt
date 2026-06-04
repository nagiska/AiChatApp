package com.aichat.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

// JSON 工具
object JsonUtil {
    
    private val json = Json { 
        ignoreUnknownKeys = true 
        prettyPrint = true
    }
    
    // 将对象转换为 JSON 字符串
    fun <T> toJson(obj: T): String {
        return json.encodeToString(obj)
    }
    
    // 将 JSON 字符串转换为对象
    inline fun <reified T> fromJson(jsonStr: String): T {
        return json.decodeFromString(jsonStr)
    }
    
    // 将 JSON 字符串转换为 Map
    fun toMap(jsonStr: String): Map<String, Any> {
        return try {
            val element = json.parseToJsonElement(jsonStr)
            elementToMap(element)
        } catch (e: Exception) {
            emptyMap()
        }
    }
    
    private fun elementToMap(element: kotlinx.serialization.json.JsonElement): Map<String, Any> {
        // 简化实现，实际需要递归处理
        return emptyMap()
    }
}
