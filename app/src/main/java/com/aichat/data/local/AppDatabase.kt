package com.aichat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aichat.data.local.dao.ApiKeyDao
import com.aichat.data.local.dao.ConversationDao
import com.aichat.data.local.dao.MessageDao
import com.aichat.data.local.entity.ApiKeyEntity
import com.aichat.data.local.entity.ConversationEntity
import com.aichat.data.local.entity.MessageEntity

@Database(
    entities = [ConversationEntity::class, MessageEntity::class, ApiKeyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun apiKeyDao(): ApiKeyDao
}
