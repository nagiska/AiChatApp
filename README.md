# AiChatApp

基于 Miuix (MIUI/HyperOS 风格) 的 Android AI 聊天客户端。

## 支持的 AI 厂商

| 厂商 | 默认模型 | 思考模型 | API 格式 |
|------|---------|---------|---------|
| OpenAI | gpt-4o | - | OpenAI |
| Claude | claude-sonnet-4-20250514 | claude-sonnet-4-20250514 | Anthropic |
| DeepSeek | deepseek-chat | deepseek-reasoner | OpenAI |
| Gemini | gemini-2.0-flash | - | OpenAI |
| 通义千问 | qwen-plus | qwq-plus | OpenAI |
| 智谱 GLM | glm-4-flash | - | OpenAI |
| 自定义 | - | - | OpenAI |

## 功能

- 多 AI 厂商支持，独立配置 API Key / Base URL / 模型
- 流式输出对话
- 温度调节 (0.0 - 2.0)
- 思考强度控制 (低/中/高)
- 思考输出开关 (显示/隐藏模型推理过程)
- 对话历史本地存储
- MIUI/HyperOS 风格 UI (Miuix)

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
