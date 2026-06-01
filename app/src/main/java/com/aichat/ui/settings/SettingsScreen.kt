package com.aichat.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aichat.domain.model.AIProvider
import com.aichat.domain.model.ApiKeyConfig
import com.aichat.domain.model.ThinkingIntensity
import com.aichat.ui.ChatViewModel
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Scaffold
import androidx.compose.material3.Switch
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.theme.MiuixTheme
import androidx.compose.material3.MaterialTheme

@Composable
fun SettingsScreen(
    viewModel: ChatViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingProvider by remember { mutableStateOf<AIProvider?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = "设置")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "AI 模型配置",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            AIProvider.entries.forEach { provider ->
                val config = uiState.apiConfigs.find { it.provider == provider }
                ApiProviderCard(
                    provider = provider,
                    config = config,
                    onEdit = {
                        editingProvider = provider
                        showAddDialog = true
                    },
                    onDelete = {
                        config?.let { viewModel.deleteApiKeyConfig(it.id) }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    if (showAddDialog) {
        val provider = editingProvider ?: AIProvider.OPENAI
        val existingConfig = uiState.apiConfigs.find { it.provider == provider }
        ApiKeyEditDialog(
            provider = provider,
            existingConfig = existingConfig,
            onDismiss = {
                showAddDialog = false
                editingProvider = null
            },
            onSave = { config ->
                viewModel.saveApiKeyConfig(config)
                showAddDialog = false
                editingProvider = null
            }
        )
    }
}

@Composable
private fun ApiProviderCard(
    provider: AIProvider,
    config: ApiKeyConfig?,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = provider.icon, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = provider.displayName, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (config != null) {
                InfoRow("模型", config.modelName.ifBlank { "默认" })
                InfoRow("API Key", "${config.apiKey.take(8)}...${config.apiKey.takeLast(4)}")
                InfoRow("温度", String.format("%.1f", config.temperature))
                if (ApiKeyConfig.supportsThinking(provider)) {
                    InfoRow("思考输出", if (config.showThinking) "开启" else "关闭")
                    if (config.showThinking) {
                        InfoRow("思考强度", config.thinkingIntensity.displayName)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Button(onClick = onEdit) {
                        Text("编辑", fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onDelete) {
                        Text("删除", fontSize = 13.sp)
                    }
                }
            } else {
                Text(
                    text = "未配置 - 点击添加",
                    fontSize = 13.sp,
                    color = MiuixTheme.colorScheme.onSurfaceSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onEdit) {
                    Text("添加配置", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = "$label: ",
            fontSize = 13.sp,
            color = MiuixTheme.colorScheme.onSurfaceSecondary
        )
        Text(
            text = value,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun ApiKeyEditDialog(
    provider: AIProvider,
    existingConfig: ApiKeyConfig?,
    onDismiss: () -> Unit,
    onSave: (ApiKeyConfig) -> Unit
) {
    var apiKey by remember { mutableStateOf(existingConfig?.apiKey ?: "") }
    var baseUrl by remember {
        mutableStateOf(existingConfig?.baseUrl ?: ApiKeyConfig.defaultBaseUrl(provider))
    }
    var modelName by remember {
        mutableStateOf(existingConfig?.modelName ?: ApiKeyConfig.defaultModel(provider))
    }
    var temperature by remember {
        mutableFloatStateOf(existingConfig?.temperature ?: 0.7f)
    }
    var maxTokens by remember {
        mutableStateOf((existingConfig?.maxTokens ?: 4096).toString())
    }
    var showThinking by remember {
        mutableStateOf(existingConfig?.showThinking ?: false)
    }
    var thinkingIntensity by remember {
        mutableStateOf(existingConfig?.thinkingIntensity ?: ThinkingIntensity.MEDIUM)
    }

    val supportsThinking = ApiKeyConfig.supportsThinking(provider)

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = provider.icon, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${provider.displayName} 配置",
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                SectionTitle("API Key")
                TextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                SectionTitle("Base URL")
                TextField(
                    value = baseUrl,
                    onValueChange = { baseUrl = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                SectionTitle("模型名称")
                TextField(
                    value = modelName,
                    onValueChange = { modelName = it },
                    modifier = Modifier.fillMaxWidth()
                )

                val thinkingModel = ApiKeyConfig.thinkingModel(provider)
                if (supportsThinking && thinkingModel.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "思考模型: $thinkingModel",
                        fontSize = 11.sp,
                        color = MiuixTheme.colorScheme.onSurfaceSecondary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                SectionTitle("温度: ${String.format("%.1f", temperature)}")
                androidx.compose.material3.Slider(
                    value = temperature,
                    onValueChange = { temperature = it },
                    valueRange = 0f..2f,
                    steps = 19,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("精确", fontSize = 11.sp, color = MiuixTheme.colorScheme.onSurfaceSecondary)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("平衡", fontSize = 11.sp, color = MiuixTheme.colorScheme.onSurfaceSecondary)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("创造", fontSize = 11.sp, color = MiuixTheme.colorScheme.onSurfaceSecondary)
                }

                Spacer(modifier = Modifier.height(12.dp))

                SectionTitle("最大 Token 数")
                TextField(
                    value = maxTokens,
                    onValueChange = { maxTokens = it.filter { c -> c.isDigit() } },
                    modifier = Modifier.fillMaxWidth()
                )

                if (supportsThinking) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("思考输出", fontSize = 14.sp)
                                    Text(
                                        text = "显示模型的推理过程",
                                        fontSize = 12.sp,
                                        color = MiuixTheme.colorScheme.onSurfaceSecondary
                                    )
                                }
                                Switch(
                                    checked = showThinking,
                                    onCheckedChange = { showThinking = it }
                                )
                            }

                            if (showThinking) {
                                Spacer(modifier = Modifier.height(12.dp))
                                SectionTitle("思考强度")
                                Row {
                                    ThinkingIntensity.entries.forEach { intensity ->
                                        Button(
                                            onClick = { thinkingIntensity = intensity },
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(horizontal = 2.dp)
                                        ) {
                                            Text(
                                                text = intensity.displayName,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val config = ApiKeyConfig(
                            id = existingConfig?.id ?: java.util.UUID.randomUUID().toString(),
                            provider = provider,
                            apiKey = apiKey,
                            baseUrl = baseUrl,
                            modelName = modelName,
                            isDefault = true,
                            temperature = temperature,
                            maxTokens = maxTokens.toIntOrNull() ?: 4096,
                            showThinking = showThinking,
                            thinkingIntensity = thinkingIntensity
                        )
                        onSave(config)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("保存")
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}
