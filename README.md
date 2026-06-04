# AI Chat App

一个使用 Miuix UI 库构建的 AI 聊天 Android 应用，类似 RikkaHub。

## 功能特性

- 🎨 **HyperOS 风格 UI** - 使用 Miuix 实现小米 HyperOS 设计语言
- 🤖 **多 AI 供应商** - 支持 OpenAI、DeepSeek、通义千问、Google Gemini、Anthropic Claude
- 💬 **流式响应** - 打字机效果实时显示 AI 回复
- 📝 **Markdown 渲染** - 支持代码高亮、LaTeX 公式、表格
- 🔀 **消息分支** - 同一问题可以获取多条不同回复
- 🤖 **Agent 系统** - 自定义 AI 助手（翻译、代码、写作等）
- 🌙 **深色模式** - 支持浅色/深色/Monet 动态取色
- 💾 **本地存储** - 所有数据保存在本地

## 技术栈

| 技术 | 用途 |
|------|------|
| Kotlin | 开发语言 |
| Miuix | UI 框架 (HyperOS 风格) |
| Room | 本地数据库 |
| DataStore | 偏好设置存储 |
| Ktor Client | HTTP 客户端 |
| Koin | 依赖注入 |
| Coil | 图片加载 |
| kotlinx.serialization | JSON 序列化 |

## 项目结构

```
app/src/main/java/com/aichat/
├── App.kt                    # Application 入口
├── MainActivity.kt           # 主 Activity
├── di/                       # 依赖注入
│   └── AppModule.kt
├── data/                     # 数据层
│   ├── db/                   # Room 数据库
│   │   ├── AppDatabase.kt
│   │   ├── dao/              # 数据访问对象
│   │   └── entity/           # 数据实体
│   └── repository/           # 仓库层
├── ai/                       # AI 供应商
│   ├── provider/             # 供应商实现
│   ├── model/                # 数据模型
│   └── AIManager.kt
├── ui/                       # UI 层
│   ├── theme/                # 主题配置
│   ├── navigation/           # 导航配置
│   ├── screen/               # 页面
│   │   ├── home/             # 首页（会话列表）
│   │   ├── chat/             # 聊天页面
│   │   ├── settings/         # 设置页面
│   │   ├── provider/         # 供应商管理
│   │   └── agent/            # Agent 管理
│   └── component/            # 通用组件
└── util/                     # 工具类
```

## 开始使用

### 1. 环境准备

- 安装 [Android Studio](https://developer.android.com/studio)
- Android SDK 34
- JDK 17

### 2. 打开项目

1. 打开 Android Studio
2. 选择 "Open an existing project"
3. 选择 `AiChatApp` 文件夹
4. 等待 Gradle 同步完成

### 3. 运行应用

1. 连接 Android 设备或启动模拟器
2. 点击 "Run" 按钮 (绿色三角形)
3. 应用将安装到设备上

### 4. 配置 AI 供应商

1. 打开应用
2. 点击底部 "设置" 标签
3. 选择 "AI 供应商"
4. 选择一个供应商（如 OpenAI）
5. 输入 API Key 和其他配置
6. 保存配置

## 添加新的 AI 供应商

### 1. 创建供应商类

在 `app/src/main/java/com/aichat/ai/provider/` 目录下创建新的供应商类：

```kotlin
class NewProvider(
    private val apiKey: String,
    private val baseUrl: String
) : AIProvider {
    override val name = "New Provider"
    override val type = "new"
    override val models = listOf("model1", "model2")
    
    override suspend fun chat(...): ChatResponse {
        // 实现非流式聊天
    }
    
    override fun chatStream(...): Flow<StreamEvent> {
        // 实现流式聊天
    }
}
```

### 2. 注册供应商

在 `AIManager.kt` 的 `createProvider` 方法中添加新供应商：

```kotlin
"new" -> NewProvider(
    apiKey = provider.apiKey,
    baseUrl = provider.baseUrl
)
```

## 自定义主题

主题配置在 `app/src/main/java/com/aichat/ui/theme/Theme.kt`：

```kotlin
@Composable
fun AiChatTheme(content: @Composable () -> Unit) {
    val controller = ThemeController(ColorSchemeMode.System)
    MiuixTheme(
        controller = controller,
        content = content
    )
}
```

支持的主题模式：
- `ColorSchemeMode.System` - 跟随系统
- `ColorSchemeMode.Light` - 浅色模式
- `ColorSchemeMode.Dark` - 深色模式
- `ColorSchemeMode.MonetSystem` - Monet 动态取色

## 常见问题

### 1. Gradle 同步失败

确保网络连接正常，或配置代理。

### 2. 应用崩溃

检查 Logcat 中的错误信息，通常是由于：
- API Key 无效
- 网络连接问题
- 数据库初始化失败

### 3. AI 回复无响应

- 检查 API Key 是否正确
- 检查网络连接
- 检查 API 余额

## 许可证

Apache License 2.0

## 致谢

- [Miuix](https://github.com/compose-miuix-ui/miuix) - UI 框架
- [RikkaHub](https://github.com/rikkahub/rikkahub) - 功能参考
