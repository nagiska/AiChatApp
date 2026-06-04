package com.aichat.ui.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aichat.ai.AIManager
import com.aichat.ai.model.ChatMessage
import com.aichat.data.db.entity.Message
import com.aichat.data.repository.ConversationRepository
import com.aichat.data.repository.MessageRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// 聊天页面 ViewModel
class ChatViewModel(
    private val conversationId: String,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val aiManager: AIManager
) : ViewModel() {

    // 会话信息
    private val _conversation = MutableStateFlow<com.aichat.data.db.entity.Conversation?>(null)
    val conversation: StateFlow<com.aichat.data.db.entity.Conversation?> = _conversation.asStateFlow()

    // 消息列表
    val messages: StateFlow<List<Message>> = 
        messageRepository.getMessagesByConversationId(conversationId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 输入文本
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    // 是否正在生成
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    // 错误消息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadConversation()
    }

    // 加载会话信息
    private fun loadConversation() {
        viewModelScope.launch {
            _conversation.value = conversationRepository.getConversationById(conversationId)
        }
    }

    // 更新输入文本
    fun updateInput(text: String) {
        _inputText.value = text
    }

    // 发送消息
    fun sendMessage() {
        val content = _inputText.value.trim()
        if (content.isEmpty() || _isGenerating.value) return

        viewModelScope.launch {
            try {
                // 保存用户消息
                messageRepository.sendUserMessage(conversationId, content)
                _inputText.value = ""
                _isGenerating.value = true

                // 获取会话配置
                val conv = _conversation.value ?: return@launch

                // 构建消息历史
                val chatMessages = messages.value.map { msg ->
                    ChatMessage(role = msg.role, content = msg.content)
                }

                // 调用 AI 接口
                val response = aiManager.sendMessage(
                    providerId = conv.providerId,
                    messages = chatMessages,
                    model = conv.model,
                    temperature = conv.temperature
                )

                // 保存 AI 回复
                val assistantContent = response.choices.firstOrNull()?.message?.content ?: ""
                messageRepository.saveAssistantMessage(conversationId, assistantContent)

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "发送失败"
            } finally {
                _isGenerating.value = false
            }
        }
    }

    // 清除错误消息
    fun clearError() {
        _errorMessage.value = null
    }
}
