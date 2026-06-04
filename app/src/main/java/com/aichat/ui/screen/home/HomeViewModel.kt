package com.aichat.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aichat.data.db.entity.Conversation
import com.aichat.data.repository.ConversationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// 首页 ViewModel
class HomeViewModel(
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    // 会话列表
    val conversations: StateFlow<List<Conversation>> = 
        conversationRepository.getAllConversations()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 搜索查询
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 搜索结果
    val searchResults: StateFlow<List<Conversation>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                conversationRepository.getAllConversations()
            } else {
                conversationRepository.searchConversations(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 更新搜索查询
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // 创建新会话
    fun createConversation(
        title: String,
        providerId: String,
        model: String,
        onCreated: (String) -> Unit
    ) {
        viewModelScope.launch {
            val conversation = conversationRepository.createConversation(
                title = title,
                providerId = providerId,
                model = model
            )
            onCreated(conversation.id)
        }
    }

    // 删除会话
    fun deleteConversation(id: String) {
        viewModelScope.launch {
            conversationRepository.deleteConversation(id)
        }
    }

    // 归档会话
    fun archiveConversation(id: String) {
        viewModelScope.launch {
            conversationRepository.archiveConversation(id)
        }
    }
}
