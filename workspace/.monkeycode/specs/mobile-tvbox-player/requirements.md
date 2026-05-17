# Requirements Document

## Introduction

本项目是一个基于 Android 平台的影视播放软件，支持导入 TVBox 点播源接口，集成 libmpv-android 作为主播放器（ExoPlayer 作为备用），适配 TV 和手机双端。软件提供简洁美观的 UI 界面，支持视频点播、收藏夹管理和观看历史记录功能。

## Glossary

- **TVBox Source**: TVBox 点播源配置，包含多个视频站点信息的 JSON 格式配置
- **libmpv-android**: 基于 mpv 的 Android 平台播放器库，支持硬件解码
- **ExoPlayer**: Google 开发的 Android 媒体播放器库，作为备用播放方案
- **Leanback**: Android TV 专用的 UI 框架，支持遥控器导航
- **VOD (Video On Demand)**: 视频点播服务

## Requirements

### REQ-1: TVBox 源导入功能

**User Story:** AS 用户，I WANT 通过多种方式导入 TVBox 源，SO THAT 可以快速配置视频内容来源

#### Acceptance Criteria

1. WHEN 用户输入有效的 HTTP/HTTPS URL 时，系统 SHALL 从网络获取并解析 TVBox 源配置
2. WHEN 用户选择本地 JSON 文件时，系统 SHALL 读取并解析文件内容
3. WHEN 用户扫描二维码时，系统 SHALL 识别 QR 码中的 URL 并自动导入
4. IF 导入的 JSON 格式无效时，系统 SHALL 显示友好的错误提示
5. WHILE 导入过程中，系统 SHALL 显示加载进度指示器
6. 系统 SHALL 保存最近导入的源配置历史记录

### REQ-2: 双播放器支持

**User Story:** AS 用户，I WANT 使用高性能播放器播放视频，SO THAT 获得流畅的观影体验

#### Acceptance Criteria

1. 系统 SHALL 优先使用 libmpv-android 播放器
2. IF libmpv 初始化失败时，系统 SHALL 自动降级到 ExoPlayer
3. 系统 SHALL 支持硬件解码加速（MediaCodec）
4. 系统 SHALL 支持暂停、继续、停止、 seek 等基本播放控制
5. 系统 SHALL 支持音量调节和亮度调节
6. WHILE 视频缓冲时，系统 SHALL 显示缓冲进度
7. IF 播放失败时，系统 SHALL 显示错误信息并提供重试选项

### REQ-3: TV 和手机双端适配

**User Story:** AS TV/手机用户，I WANT 获得适配设备的界面体验，SO THAT 可以方便地浏览和操作

#### Acceptance Criteria

1. 系统 SHALL 自动检测设备类型（TV 或手机）
2. WHILE 运行在 TV 设备时，系统 SHALL 使用 Leanback 界面和遥控器导航
3. WHILE 运行在手机设备时，系统 SHALL 使用 Material Design 3 触摸界面
4. 系统 SHALL 在 TV 端支持 D-Pad 方向键导航
5. 系统 SHALL 在手机端支持手势操作（滑动、双击）
6. 系统 SHALL 根据屏幕尺寸自适应布局

### REQ-4: 视频点播功能

**User Story:** AS 用户，I WANT 浏览和点播视频内容，SO THAT 可以观看喜欢的影视节目

#### Acceptance Criteria

1. 系统 SHALL 展示 TVBox 源中的所有视频站点列表
2. WHEN 用户选择站点时，系统 SHALL 加载并显示该站点的视频分类
3. WHEN 用户选择分类时，系统 SHALL 加载并显示视频列表
4. WHEN 用户选择视频时，系统 SHALL 启动播放器开始播放
5. 系统 SHALL 支持视频搜索功能
6. 系统 SHALL 支持视频详情页面（标题、简介、封面图）

### REQ-5: 历史记录管理

**User Story:** AS 用户，I WANT 查看和管理观看历史，SO THAT 可以继续观看未完成的视频

#### Acceptance Criteria

1. WHILE 用户观看视频时，系统 SHALL 自动记录观看进度
2. WHEN 用户再次观看相同视频时，系统 SHALL 提示是否从上次位置继续
3. 系统 SHALL  displaying 最近观看历史列表（至少 50 条）
4. 系统 SHALL 支持清空历史记录功能
5. 系统 SHALL 支持单条删除历史记录
6. 历史记录 SHALL 包含：标题、URL、站点名、观看位置、时长、时间戳

### REQ-6: 收藏功能

**User Story:** AS 用户，I WANT 收藏喜欢的视频，SO THAT 可以快速访问

#### Acceptance Criteria

1. 系统 SHALL 支持添加视频到收藏夹
2. 系统 SHALL 支持从收藏夹移除视频
3. 系统 SHALL 显示所有收藏的视频列表
4. 系统 SHALL 显示收藏状态标识（已收藏/未收藏）
5. IF 视频已在收藏中，系统 SHALL 提示"已收藏"
6. 收藏夹 SHALL 包含：标题、URL、站点名、缩略图、添加时间

## Data Requirements

### DATA-1: 数据持久化

1. 系统 SHALL 使用 SQLite 数据库存储历史记录和收藏夹
2. 系统 SHALL 使用 SharedPreferences 存储用户设置和最近导入的源
3. 系统 SHALL 在应用退出时保存当前播放进度
4. 系统 SHALL 定期清理超过 30 天的历史记录

### DATA-2: 网络请求

1. 系统 SHALL 使用 OkHttp 进行网络请求
2. 系统 SHALL 设置 30 秒超时时间
3. 系统 SHALL 支持 HTTPS 加密连接
4. 系统 SHALL 处理网络错误并显示友好提示

## Quality Requirements

### PERF-1: 性能要求

1. 应用启动时间 SHALL 小于 3 秒
2. 视频加载时间 SHALL 小于 5 秒（网络良好时）
3. UI 响应时间 SHALL 小于 100 毫秒
4. 内存占用 SHALL 不超过 200MB

### SEC-1: 安全要求

1. 系统 SHALL 不存储任何用户隐私数据到外部存储
2. 系统 SHALL 不使用明文 HTTP 传输敏感数据
3. 系统 SHALL 不请求不必要的权限

### USAB-1: 可用性要求

1. 界面 SHALL 简洁直观，无需教程即可使用
2. 系统 SHALL 提供中文界面
3. 错误提示 SHALL 清晰易懂，提供解决建议
