# 蓝牙聊天应用

## 简介

蓝牙聊天应用是一个基于 Android 的即时通信工具，允许用户通过蓝牙与附近已配对的设备进行点对点消息交流。该应用使用 Jetpack Compose 构建现代化 UI，集成 Hilt 进行依赖注入，支持蓝牙设备的发现、连接和消息收发，适用于 Android 8.0（API 26）及以上设备。

## 功能

- **设备配对**：显示已配对的蓝牙设备列表，支持选择设备进行连接。
- **蓝牙连接**：支持作为服务器监听连接或作为客户端连接到其他设备。
- **消息收发**：提供实时消息发送和接收功能，消息以发送/接收区分显示。
- **权限管理**：自动请求蓝牙相关权限（`BLUETOOTH`, `BLUETOOTH_ADMIN`, `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, `BLUETOOTH_ADVERTISE`, `ACCESS_FINE_LOCATION`）。
- **状态提示**：实时显示连接状态（未连接、监听、已连接、错误），并提供中文错误提示。
- **现代化 UI**：使用 Jetpack Compose 实现响应式界面，支持深色模式和动态主题。

## 技术栈

- **语言**：Kotlin
- **UI 框架**：Jetpack Compose
- **依赖注入**：Dagger Hilt
- **异步处理**：Kotlin Coroutines 和 Flow
- **蓝牙通信**：Android Bluetooth API（RFCOMM）
- **构建工具**：Gradle (Kotlin DSL)
- **最低支持版本**：Android 8.0 (API 26)
- **目标 SDK**：Android 15 (API 35)

## 安装

### 前置条件
- Android Studio（推荐 2024.2.1 或以上版本）
- Android 设备或模拟器（运行 Android 8.0 或以上）
- 已启用蓝牙的 Android 设备（用于测试）

### 步骤
1. **克隆仓库**：
   ```bash
   git clone <仓库地址>
   cd bluetooth-chat
   ```

2. **打开项目**：
   - 在 Android Studio 中选择 `File -> Open`，导航到项目根目录并打开。

3. **同步项目**：
   - 点击 Android Studio 工具栏中的 `Sync Project with Gradle Files` 按钮。
   - 确保 `gradle-wrapper.properties` 使用 Gradle 8.9：
     ```properties
     distributionUrl=https\://services.gradle.org/distributions/gradle-8.9-bin.zip
     ```

4. **配置设备**：
   - 连接一台支持蓝牙的 Android 设备，或在 Android Studio 中配置模拟器（需启用蓝牙支持）。
   - 确保设备已配对至少一个蓝牙设备（通过系统设置完成配对）。

5. **运行应用**：
   - 点击 `Run` 按钮，或使用快捷键 `Shift + F10`。
   - 应用将自动请求蓝牙和位置权限，授予所有权限以确保功能正常。

## 使用方法

1. **启动应用**：
   - 打开应用，主界面显示当前蓝牙连接状态（初始为“未连接”）。

2. **选择设备**：
   - 点击导航到设备列表页面，查看已配对的蓝牙设备。
   - 点击设备名称发起连接，成功后返回聊天界面。

3. **发送消息**：
   - 在聊天界面输入消息，点击“发送”按钮。
   - 发送的消息显示在右侧，接收的消息显示在左侧。

4. **断开连接**：
   - 点击顶部工具栏的“断开连接”按钮，终止当前蓝牙连接。

5. **错误处理**：
   - 如果缺少权限或连接失败，界面会显示中文错误提示（如“缺少 BLUETOOTH_CONNECT 权限”）。
   - 按照提示授予权限或检查设备状态。

## 项目结构

```
bluetooth-chat/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/bluetoothchat/
│   │   │   │   ├── data/
│   │   │   │   │   ├── bluetooth/
│   │   │   │   │   │   └── BluetoothService.kt  # 蓝牙通信逻辑
│   │   │   │   │   └── model/
│   │   │   │   │       └── Message.kt         # 消息数据模型
│   │   │   │   ├── di/
│   │   │   │   │   └── AppModule.kt           # Hilt 依赖注入模块
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screen/
│   │   │   │   │   │   ├── ChatScreen.kt      # 聊天界面
│   │   │   │   │   │   └── DeviceListScreen.kt # 设备列表界面
│   │   │   │   │   └── viewmodel/
│   │   │   │   │       └── BluetoothViewModel.kt # 蓝牙视图模型
│   │   │   │   └── util/
│   │   │   │       └── PermissionsHandler.kt  # 权限管理
│   │   │   ├── AndroidManifest.xml            # 应用清单
│   │   │   └── res/                           # 资源文件
│   ├── build.gradle.kts                       # 模块级 Gradle 配置
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties          # Gradle 包装器配置
├── build.gradle.kts                           # 项目级 Gradle 配置
└── gradle/libs.versions.toml                  # 依赖版本管理
```

## 常见问题

1. **应用启动后崩溃，提示缺少权限**：
   - 确保授予所有蓝牙和位置权限（`BLUETOOTH`, `BLUETOOTH_ADMIN`, `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, `BLUETOOTH_ADVERTISE`, `ACCESS_FINE_LOCATION`）。
   - 检查 `PermissionsHandler.kt` 是否正确请求权限。

2. **设备列表为空**：
   - 确保设备已通过系统设置配对至少一个蓝牙设备。
   - 检查蓝牙适配器是否启用（通过 `BluetoothAdapter.getDefaultAdapter()`）。

3. **消息发送失败**：
   - 确认连接状态为“已连接”。
   - 检查日志（`adb logcat | grep "BluetoothChat"`）以定位错误。

4. **构建失败，提示 Hilt 相关错误**：
   - 运行 `./gradlew kaptDebugKotlin --stacktrace` 检查 Hilt 生成代码。
   - 确保 `build.gradle.kts` 包含 Hilt 插件和依赖：
     ```kotlin
     alias(libs.plugins.hilt.android)
     id("org.jetbrains.kotlin.kapt")
     implementation(libs.hilt.android)
     kapt(libs.hilt.compiler)
     ```

## 贡献

欢迎提交问题或拉取请求！请遵循以下步骤：
1. Fork 仓库。
2. 创建特性分支（`git checkout -b feature/xxx`）。
3. 提交更改（`git commit -m '添加 xxx 功能'`）。
4. 推送到分支（`git push origin feature/xxx`）。
5. 创建拉取请求。

## 许可证

本项目采用 MIT 许可证，详情见 [LICENSE](LICENSE) 文件。