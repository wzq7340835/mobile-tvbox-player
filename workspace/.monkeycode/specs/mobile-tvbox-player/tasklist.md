# Implementation Task List

Feature: mobile-tvbox-player  
Created: 2026-04-22

## Phase 1: Project Setup

- [x] 1.1 创建 Android 项目结构和 build.gradle 配置
- [x] 1.2 添加项目依赖（OkHttp, ExoPlayer, Material, Leanback）
- [x] 1.3 配置 AndroidManifest.xml（权限、feature）
- [x] 1.4 集成 libmpv-android .so 库文件

## Phase 2: Data Layer

- [x] 2.1 实现数据模型类（TvBoxSource, VideoSite, HistoryItem, FavoriteItem）
- [x] 2.2 实现 HistoryManager 数据库操作
- [x] 2.3 实现 FavoriteManager 数据库操作
- [ ] 2.4 实现 SharedPreferences 工具类

## Phase 3: Player Layer

- [x] 3.1 实现 IVideoPlayer 接口
- [x] 3.2 实现 MpvPlayer（JNI 封装）
- [x] 3.3 编写 MPV JNI 本地代码（C 文件）
- [x] 3.4 实现 ExoPlayerWrapper
- [x] 3.5 实现 PlayerManager 统一调度

## Phase 4: Parser Layer

- [x] 4.1 实现 TvBoxParser JSON 解析器
- [x] 4.2 实现网络导入功能（OkHttp）
- [x] 4.3 实现本地文件导入功能
- [x] 4.4 实现二维码扫描功能（ZXing）

## Phase 5: UI Layer - Mobile

- [x] 5.1 设计手机端布局（activity_main_mobile.xml）
- [x] 5.2 实现视频列表适配器（VideoListAdapter）
- [x] 5.3 实现站点列表适配器（SiteListAdapter）
- [x] 5.4 实现播放控制界面
- [x] 5.5 实现历史记录页面
- [x] 5.6 实现收藏页面

## Phase 6: UI Layer - TV

- [x] 6.1 设计 TV 端布局（activity_main_tv.xml）
- [ ] 6.2 实现 Leanback Fragment - 待实现基础 UI 完成后
- [x] 6.3 实现 TV 端卡片适配器
- [ ] 6.4 实现遥控器导航支持 - 待实现基础 UI 完成后

## Phase 7: MainActivity Integration

- [x] 7.1 实现 MainActivity 主逻辑
- [x] 7.2 实现设备类型检测
- [x] 7.3 实现源导入 UI 和逻辑
- [ ] 7.4 实现播放器与 UI 的集成 - 待播放功能完善

## Phase 8: Testing & Optimization

- [ ] 8.1 编写单元测试（Parser, Manager）
- [ ] 8.2 编写播放器集成测试
- [ ] 8.3 性能优化（内存、加载速度）
- [ ] 8.4 UI/UX 优化

## Phase 9: Documentation

- [x] 9.1 编写 libmpv 集成教程
- [x] 9.2 编写 AIDE 使用说明
- [x] 9.3 完善 README 文档

## Notes

- 优先级：Phase 1 → Phase 2 → Phase 3 → Phase 4 → Phase 5 → Phase 6 → Phase 7 → Phase 8 → Phase 9
- 每个阶段完成后需要运行测试验证
- TV 端和手机端可以并行开发
- libmpv 集成是关键路径，需优先完成
