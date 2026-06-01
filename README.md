# AiChatApp

基于 Miuix (MIUI/HyperOS 风格) 的 Android AI 聊天客户端。

## 支持的 AI 厂商

| 厂商 | Base URL | 默认模型 | 思考模型 | API 格式 |
|------|---------|---------|---------|---------|
| OpenAI | api.openai.com/v1 | gpt-4o | - | OpenAI |
| Claude | api.anthropic.com/v1 | claude-sonnet-4-20250514 | ✓ | Anthropic |
| DeepSeek | api.deepseek.com | deepseek-v4-flash | deepseek-v4-pro | OpenAI |
| Gemini | generativelanguage.googleapis.com/v1beta | gemini-2.0-flash | - | OpenAI |
| 通义千问 | dashscope.aliyuncs.com/compatible-mode/v1 | qwen-plus | qwq-plus | OpenAI |
| 智谱 GLM | open.bigmodel.cn/api/paas/v4 | glm-4-flash | - | OpenAI |
| MiMo | api.xiaomimimo.com/v1 | mimo-v2.5-pro | mimo-v2.5-pro (thinking) | OpenAI |
| 自定义 | - | - | - | OpenAI |

## 功能

- 多 AI 厂商支持，独立配置 API Key / Base URL / 模型
- 流式输出对话
- 温度调节 (0.0 - 2.0)
- 思考强度控制 (低/中/高 → reasoning_effort: low/medium/high)
- 思考输出开关 (显示/隐藏模型推理过程)
- 对话历史本地存储 (Room)
- MIUI/HyperOS 风格 UI (Miuix)

## 思考模式说明

- **DeepSeek**: 使用 `thinking: {type: "enabled"}` + `reasoning_effort` (low/medium/high)，流式返回 `reasoning_content`
- **Claude**: 使用 `extended-thinking` beta，流式返回 thinking block
- **Qwen**: 使用 qwq-plus 模型，OpenAI 兼容格式
- **MiMo**: 使用 `thinking: {type: "enabled"}` 参数，使用 `max_completion_tokens`，流式返回 `reasoning_content`

## 技术栈

- Kotlin + Jetpack Compose
- Miuix (MIUI 风格 UI 库) v0.9.1
- Hilt (依赖注入)
- Room (本地存储)
- Ktor (网络请求)
- Kotlinx Serialization

## 构建

```bash
# 推送到 GitHub 后自动构建
# 或本地构建：
gradle assembleDebug
```

## GitHub Actions

推送到 main 分支会自动构建 APK，在 Actions 页面下载。

## 项目结构

```
app/src/main/java/com/aichat/
├── data/
│   ├── local/          # Room 数据库 (DAO, Entity)
│   ├── remote/         # AI API 客户端 (OpenAI, Claude)
│   └── repository/     # Repository 实现
├── di/                 # Hilt 依赖注入
├── domain/
│   ├── model/          # 数据模型
│   └── repository/     # 接口定义
└── ui/
    ├── chat/           # 对话页面
    ├── components/     # 通用组件 (气泡, 输入框)
    ├── main/           # 对话列表
    ├── settings/       # 设置页面
    ├── nav/            # 导航
    └── theme/          # Miuix 主题
```
