# Mobile TVBox Player

一款支持 TVBox 点播源的双端（TV+ 手机）Android 影视播放软件。

## 特性

- ✅ 支持导入 TVBox 点播源（网络/本地/扫码）
- ✅ 集成 libmpv-android 高性能播放器
- ✅ ExoPlayer 备用播放方案
- ✅ TV 和手机双端适配
- ✅ 观看历史记录
- ✅ 视频收藏功能
- ✅ Material Design 3 美观界面
- ✅ Android TV Leanback 支持

## 截图

### 手机端
- 主界面：显示站点列表和导入选项
- 播放器：全屏播放界面，支持手势控制
- 历史记录：显示观看进度和历史

### TV 端  
- Leanback 界面：遥控器友好导航
- 卡片式布局：美观的站点展示
- 全屏播放：影院级体验

## 技术栈

- **开发语言**: Java
- **最低 SDK**: Android 5.0 (API 21)
- **目标 SDK**: Android 14 (API 34)
- **UI 框架**: Material Design 3 / Leanback
- **播放器**: libmpv-android / ExoPlayer
- **网络库**: OkHttp
- **图片加载**: Glide
- **JSON 解析**: Gson

## 项目结构

```
mobile-tvbox-player/
├── app/
│   ├── src/main/
│   │   ├── java/com/player/tvbox/
│   │   │   ├── activity/          # Activity 类
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── PlayerActivity.java
│   │   │   │   ├── HistoryActivity.java
│   │   │   │   ├── FavoriteActivity.java
│   │   │   │   ├── QrScanActivity.java
│   │   │   │   └── SettingsActivity.java
│   │   │   ├── adapter/           # RecyclerView 适配器
│   │   │   │   ├── SiteListAdapter.java
│   │   │   │   └── HistoryListAdapter.java
│   │   │   ├── db/                # 数据库操作
│   │   │   │   ├── DatabaseHelper.java
│   │   │   │   ├── HistoryManager.java
│   │   │   │   └── FavoriteManager.java
│   │   │   ├── model/             # 数据模型
│   │   │   │   ├── TvBoxSource.java
│   │   │   │   ├── VideoSite.java
│   │   │   │   ├── HistoryItem.java
│   │   │   │   └── FavoriteItem.java
│   │   │   ├── parser/            # 解析器
│   │   │   │   └── TvBoxParser.java
│   │   │   ├── player/            # 播放器封装
│   │   │   │   ├── IVideoPlayer.java
│   │   │   │   ├── PlayerManager.java
│   │   │   │   ├── MpvPlayer.java
│   │   │   │   └── ExoPlayerWrapper.java
│   │   │   └── ui/                # UI 组件
│   │   │       ├── tv/            # TV 端 UI
│   │   │       └── mobile/        # 手机端 UI
│   │   ├── res/                   # 资源文件
│   │   │   ├── layout/            # 布局文件
│   │   │   ├── values/            # 字符串、颜色、主题
│   │   │   └── menu/              # 菜单资源
│   │   ├── cpp/                   # JNI 本地代码
│   │   │   ├── CMakeLists.txt
│   │   │   └── mpv_jni.c
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── .monkeycode/
│   ├── specs/
│   │   └── mobile-tvbox-player/
│   │       ├── requirements.md    # 需求文档
│   │       ├── design.md          # 技术设计
│   │       └── tasklist.md        # 任务列表
│   └── docs/
│       └── libmpv-integration-guide.md
└── build.gradle
```

## 快速开始

### 环境要求

- Android Studio Arctic Fox 或更高版本
- NDK 23.1 或更高版本
- CMake 3.22.1 或更高版本
- JDK 11 或更高版本

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <项目地址> mobile-tvbox-player
   cd mobile-tvbox-player
   ```

2. **下载 libmpv 库**
   ```bash
   # 访问 https://github.com/mpv-android/mpv-android/releases
   # 下载最新版本的 .so 文件
   
   # 复制到对应目录
   cp libmpv.so app/src/main/jniLibs/armeabi-v7a/
   cp libmpv.so app/src/main/jniLibs/arm64-v8a/
   ```

3. **使用 Android Studio 打开项目**

4. **同步 Gradle**
   - 点击 "Sync Now"
   - 等待依赖下载完成

5. **运行项目**
   - 连接 Android 设备或启动模拟器
   - 点击 "Run" (绿色三角形)

### AIDE 编译

1. 在 AIDE 中打开项目目录
2. 确保已安装 NDK 插件
3. 点击"构建和运行"

## 使用说明

### 导入 TVBox 源

#### 方式一：网络导入
1. 点击"网络导入"按钮
2. 输入 TVBox 源 URL 地址
3. 点击"确定"开始导入

#### 方式二：本地导入
1. 点击"本地导入"按钮
2. 在文件管理器中选择 JSON 文件
3. 等待解析完成

#### 方式三：扫码导入
1. 点击"扫码导入"按钮
2. 对准 TVBox 源二维码
3. 扫描成功后自动导入

### 播放视频

1. 在站点列表中选择要播放的站点
2. 浏览视频分类和列表
3. 点击视频开始播放
4. 播放器支持：
   - 暂停/继续
   - 进度拖动
   - 音量调节
   - 亮度调节（手机端）

### 历史记录

- 自动记录观看进度
- 支持续播功能
- 可清空或单条删除

### 收藏功能

- 一键添加/取消收藏
- 收藏列表按时间排序
- 支持快速访问

## 配置说明

### 播放器设置

在"设置"中可以配置：

- **播放器类型**: 自动/仅 MPV/仅 ExoPlayer
- **硬件解码**: 启用/禁用
- **视频缓存**: 启用/禁用
- **默认清晰度**: 自动/高清/标清

## 构建选项

### 调试版本

```bash
./gradlew assembleDebug
```

输出位置：`app/build/outputs/apk/debug/app-debug.apk`

### 发布版本

```bash
./gradlew assembleRelease
```

输出位置：`app/build/outputs/apk/release/app-release.apk`

### 签名配置

在 `gradle.properties` 中配置：

```properties
RELEASE_STORE_FILE=/path/to/keystore.jks
RELEASE_STORE_PASSWORD=your_password
RELEASE_KEY_ALIAS=your_alias
RELEASE_KEY_PASSWORD=your_key_password
```

## 测试

### 单元测试

```bash
./gradlew test
```

### 仪器测试

```bash
./gradlew connectedAndroidTest
```

## 贡献指南

欢迎提交 Issue 和 Pull Request！

### 开发流程

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 代码风格

- 遵循 Google Java Style Guide
- 使用 4 个空格缩进
- 类名使用 PascalCase
- 方法和变量使用 camelCase
- 常量使用 UPPER_SNAKE_CASE

## 已知问题

1. 部分老旧设备可能不支持硬件解码
2. 某些 TVBox 源格式可能不兼容
3. 网络不稳定时加载较慢

## 更新日志

### v1.0.0 (2026-04-22)
- ✨ 初始版本发布
- ✅ 支持 TVBox 源导入
- ✅ 集成 libmpv 播放器
- ✅ TV 和手机双端适配
- ✅ 历史记录和收藏功能

## 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。

## 致谢

感谢以下开源项目：

- [mpv-android](https://github.com/mpv-android/mpv-android)
- [ExoPlayer](https://github.com/google/ExoPlayer)
- [TVBoxOSS](https://github.com/CatVodTVOfficial/TVBoxOSS)
- [Material Design 3](https://m3.material.io/)
- [Leanback](https://developer.android.com/training/tv)

## 联系方式

- 问题反馈：请提 Issue
- 功能建议：欢迎在 Discussion 中讨论

## 免责声明

本软件仅供学习交流使用，请勿用于商业用途。
使用本软件时请遵守当地法律法规，尊重版权。
