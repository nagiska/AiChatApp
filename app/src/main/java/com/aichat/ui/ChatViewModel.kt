package com.aichat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aichat.domain.model.AIProvider
import com.aichat.domain.model.ApiKeyConfig
import com.aichat.domain.model.ChatMessage
import com.aichat.domain.model.Conversation
import com.aichat.domain.model.MessageRole
import com.aichat.domain.repository.AiClientRepository
import com.aichat.domain.repository.ApiKeyRepository
import com.aichat.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ChatUiState(
    val conversations: List<Conversation> = emptyList(),
    val currentConversation: Conversation? = null,
    val messages: List<ChatMessage> = emptyList(),
    val isStreaming: Boolean = false,
    val streamingContent: String = "",
    val streamingThinking: String = "",
    val error: String? = null,
    val apiConfigs: List<ApiKeyConfig> = emptyList(),
    val showThinkingOutput: Boolean = false
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val apiKeyRepository: ApiKeyRepository,
    private val aiClientRepository: AiClientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadConversations()
        loadApiConfigs()
    }

    private fun loadConversations() {
        viewModelScope.launch {
            chatRepository.getAllConversations().collect { conversations ->
                _uiState.value = _uiState.value.copy(conversations = conversations)
            }
        }
    }

    private fun loadApiConfigs() {
        viewModelScope.launch {
            apiKeyRepository.getAllConfigs().collect { configs ->
                _uiState.value = _uiState.value.copy(apiConfigs = configs)
            }
        }
    }

    fun createConversation(provider: AIProvider = AIProvider.OPENAI, title: String = "新对话") {
        viewModelScope.launch {
            val conversation = Conversation(
                id = UUID.randomUUID().toString(),
                title = title,
                provider = provider,
                modelName = ApiKeyConfig.defaultModel(provider)
            )
            chatRepository.createConversation(conversation)
            selectConversation(conversation)
        }
    }

    fun selectConversation(conversation: Conversation) {
        _uiState.value = _uiState.value.copy(
            currentConversation = conversation,
            messages = emptyList()
        )
        viewModelScope.launch {
            chatRepository.getMessagesByConversation(conversation.id).collect { messages ->
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }

    fun deleteConversation(conversationId: String) {
        viewModelScope.launch {
            chatRepository.deleteConversation(conversationId)
            if (_uiState.value.currentConversation?.id == conversationId) {
                _uiState.value = _uiState.value.copy(
                    currentConversation = null,
                    messages = emptyList()
                )
            }
        }
    }

    fun sendMessage(content: String) {
        val conversation = _uiState.value.currentConversation ?: return
        if (content.isBlank() || _uiState.value.isStreaming) return

        viewModelScope.launch {
            val userMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                role = MessageRole.USER,
                content = content
            )
            chatRepository.addMessage(userMessage, conversation.id)

            val config = apiKeyRepository.getDefaultConfig(conversation.provider)
            if (config == null) {
                _uiState.value = _uiState.value.copy(
                    error = "请先在设置中配置 ${conversation.provider.displayName} 的 API Key"
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isStreaming = true,
                streamingContent = "",
                streamingThinking = "",
                error = null
            )

            try {
                val allMessages = _uiState.value.messages + userMessage
                val assistantMessageId = UUID.randomUUID().toString()
                val fullContent = StringBuilder()
                val fullThinking = StringBuilder()

                val enableThinking = config.showThinking &&
                    ApiKeyConfig.supportsThinking(conversation.provider)

                aiClientRepository.sendMessage(
                    provider = conversation.provider,
                    apiKey = config.apiKey,
                    baseUrl = config.baseUrl.ifBlank { ApiKeyConfig.defaultBaseUrl(conversation.provider) },
                    modelName = conversation.modelName.ifBlank { config.modelName },
                    messages = allMessages,
                    temperature = config.temperature,
                    maxTokens = config.maxTokens,
                    enableThinking = enableThinking,
                    thinkingBudget = (config.thinkingIntensity.temperature * 4096).toInt()
                ).collect { result ->
                    if (result.content.isNotEmpty()) fullContent.append(result.content)
                    if (result.thinkingContent.isNotEmpty()) fullThinking.append(result.thinkingContent)
                    _uiState.value = _uiState.value.copy(
                        streamingContent = fullContent.toString(),
                        streamingThinking = fullThinking.toString()
                    )
                }

                val assistantMessage = ChatMessage(
                    id = assistantMessageId,
                    role = MessageRole.ASSISTANT,
                    content = fullContent.toString(),
                    thinkingContent = fullThinking.toString()
                )
                chatRepository.addMessage(assistantMessage, conversation.id)

                chatRepository.updateConversation(
                    conversation.copy(updatedAt = System.currentTimeMillis())
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "请求失败")
            } finally {
                _uiState.value = _uiState.value.copy(
                    isStreaming = false,
                    streamingContent = "",
                    streamingThinking = ""
                )
            }
        }
    }

    fun toggleThinkingOutput() {
        _uiState.value = _uiState.value.copy(
            showThinkingOutput = !_uiState.value.showThinkingOutput
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun saveApiKeyConfig(config: ApiKeyConfig) {
        viewModelScope.launch {
            apiKeyRepository.saveConfig(config)
        }
    }

    fun deleteApiKeyConfig(id: String) {
        viewModelScope.launch {
            apiKeyRepository.deleteConfig(id)
        }
    }

    fun backToList() {
        _uiState.value = _uiState.value.copy(
            currentConversation = null,
            messages = emptyList()
        )
    }
}
