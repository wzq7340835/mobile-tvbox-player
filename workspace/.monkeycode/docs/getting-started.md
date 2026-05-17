# 项目开发指南

## 项目完成度

### 已完成的功能（✅）

#### 1. 项目基础（Phase 1）
- ✅ 完整的 Gradle 项目结构
- ✅ 所有依赖配置（OkHttp, ExoPlayer, Material, Leanback, ZXing）
- ✅ AndroidManifest.xml 配置（权限、feature）
- ✅ JNI 库目录结构

#### 2. 数据层（Phase 2）
- ✅ 4 个数据模型类
  - TvBoxSource
  - VideoSite
  - HistoryItem
  - FavoriteItem
- ✅ HistoryManager（历史记录管理）
- ✅ FavoriteManager（收藏管理）
- ✅ DatabaseHelper（SQLite 数据库）

#### 3. 播放器层（Phase 3）
- ✅ IVideoPlayer 统一接口
- ✅ MpvPlayer（libmpv JNI 封装）
- ✅ ExoPlayerWrapper（ExoPlayer 包装）
- ✅ PlayerManager（播放器管理器，支持自动切换）
- ✅ JNI 本地代码（C 语言实现）

#### 4. 解析层（Phase 4）
- ✅ TvBoxParser JSON 解析器
- ✅ 网络导入功能（OkHttp 异步）
- ✅ 本地文件导入功能
- ✅ 二维码扫描集成（ZXing）

#### 5. UI 层 - 手机端（Phase 5）
- ✅ 主界面布局（Material Design 3）
- ✅ 站点列表适配器
- ✅ 历史记录适配器
- ✅ 播放界面布局
- ✅ 历史记录界面
- ✅ 收藏界面
- ✅ 设置界面

#### 6. UI 层 - TV 端（Phase 6）
- ✅ TV 端布局框架
- ✅ Leanback 主题配置
- ⏳ Leanback Fragment（需进一步完善）
- ⏳ 遥控器导航（需进一步完善）

#### 7. 主逻辑集成（Phase 7）
- ✅ MainActivity 主逻辑
- ✅ 设备类型自动检测
- ✅ 三种导入方式 UI
- ⏳ 播放器 UI 集成（待完善）

#### 8. 文档（Phase 9）
- ✅ 完整的需求文档（requirements.md）
- ✅ 技术设计文档（design.md）
- ✅ 任务列表（tasklist.md）
- ✅ libmpv 集成教程
- ✅ AIDE 使用说明
- ✅ 项目 README

### 待完成的功能（⏳）

#### 1. 测试（Phase 8）
- ⏳ 单元测试
- ⏳ 播放器集成测试
- ⏳ 性能优化
- ⏳ UI/UX 优化

#### 2. TV 端增强
- ⏳ 完整的 Leanback Fragment
- ⏳ 遥控器按键事件处理
- ⏳ TV 端卡片动画
- ⏳ 焦点处理优化

#### 3. 功能完善
- ⏳ 视频详情页
- ⏳ 视频分类浏览
- ⏳ 搜索功能
- ⏳ 多清晰度切换
- ⏳ 字幕支持
- ⏳ 倍速播放
- ⏳ 投屏功能

#### 4. 优化
- ⏳ 图片缓存优化
- ⏳ 网络请求优化
- ⏳ 数据库事务优化
- ⏳ 内存泄漏检测
- ⏳ 启动速度优化

## 使用指南

### 在 Android Studio 中运行

1. **打开项目**
   ```bash
   # 使用 Android Studio 打开 mobile-tvbox-player 目录
   ```

2. **下载 libmpv 库**
   - 从 https://github.com/mpv-android/mpv-android/releases 下载
   - 解压后将 `.so` 文件放入 `app/src/main/jniLibs/` 对应架构目录

3. **同步 Gradle**
   - 点击 "Sync Now" 按钮
   - 等待依赖下载完成

4. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击 "Run 'app'"

### 在 AIDE 中运行

1. **复制项目到设备**
   ```
   /sdcard/mobile-tvbox-player/
   ```

2. **在 AIDE 中打开**
   - 打开 AIDE
   - 选择 "打开项目"
   - 浏览到项目目录

3. **配置 NDK**
   - Settings → Build and Run
   - 启用 "Native C/C++ Code"

4. **下载 libmpv 并放置**
   - 下载 .so 文件
   - 复制到 `jniLibs/` 目录

5. **编译运行**
   - 点击 "Build and Run"
   - 或按 Shift+F5

### libmpv 配置（重要）

libmpv 库文件**不包含**在项目中，需要手动下载：

```bash
# 1. 下载
wget https://github.com/mpv-android/mpv-android/releases/download/2024-01-28-release/mpv-android-2024-01-28-release.zip

# 2. 解压
unzip mpv-android-2024-01-28-release.zip

# 3. 复制库文件
cp mpv-android/armeabi-v7a/libmpv.so app/src/main/jniLibs/armeabi-v7a/
cp mpv-android/arm64-v8a/libmpv.so app/src/main/jniLibs/arm64-v8a/
# x86 和 x86_64 架构同理
```

**如果不想使用 libmpv**，可以：
1. 修改 `PlayerManager.java`
2. 始终使用 `ExoPlayerWrapper`
3. 删除 JNI 相关代码

## 代码结构说明

### 包结构

```
com.player.tvbox/
├── activity/          # Activity 类
│   ├── MainActivity
│   ├── PlayerActivity
│   ├── HistoryActivity
│   ├── FavoriteActivity
│   ├── QrScanActivity
│   └── SettingsActivity
├── adapter/           # RecyclerView 适配器
│   ├── SiteListAdapter
│   └── HistoryListAdapter
├── db/                # 数据库操作
│   ├── DatabaseHelper
│   ├── HistoryManager
│   └── FavoriteManager
├── model/             # 数据模型
│   ├── TvBoxSource
│   ├── VideoSite
│   ├── HistoryItem
│   └── FavoriteItem
├── parser/            # 解析器
│   └── TvBoxParser
├── player/            # 播放器封装
│   ├── IVideoPlayer
│   ├── PlayerManager
│   ├── MpvPlayer
│   └── ExoPlayerWrapper
└── ui/                # UI 组件（待完善）
    ├── tv/
    └── mobile/
```

### 资源结构

```
res/
├── layout/
│   ├── activity_main_mobile.xml
│   ├── activity_main_tv.xml
│   ├── activity_player.xml
│   ├── activity_history.xml
│   ├── activity_favorite.xml
│   ├── activity_settings.xml
│   ├── item_site.xml
│   ├── item_history.xml
│   └── dialog_import_url.xml
├── values/
│   ├── strings.xml
│   ├── colors.xml
│   ├── themes.xml
│   └── leanback_styles.xml
├── menu/
│   └── bottom_nav_menu.xml
└── xml/
    ├── preferences_root.xml
    └── arrays.xml
```

## 核心代码示例

### 导入 TVBox 源

```java
TvBoxParser.parseFromUrl("https://example.com/tvbox.json", 
    new TvBoxParser.ParseCallback() {
    @Override
    public void onSuccess(TvBoxSource source) {
        // 解析成功
        List<VideoSite> sites = source.getSites();
        // 更新 UI...
    }
    
    @Override
    public void onError(String error) {
        // 处理错误
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }
});
```

### 播放视频

```java
// 获取 PlayerManager 实例
PlayerManager pm = PlayerManager.getInstance(context);

// 初始化播放器
pm.initPlayer(PlayerManager.PlayerType.MPV);

// 获取播放器接口
IVideoPlayer player = pm.getPlayer();

// 设置 Surface（从 SurfaceView）
player.setSurface(surface);

// 播放视频
player.play("https://example.com/video.mp4");
```

### 管理历史记录

```java
HistoryManager history = new HistoryManager(context);

// 添加记录
history.addHistory("视频标题", "url", "站点名", 30000, 60000);

// 获取列表
List<HistoryItem> list = history.getHistory(50);

// 更新进度
history.updateProgress("url", 60000, 120000);

// 删除
history.removeHistory(id);
```

### 管理收藏

```java
FavoriteManager favorite = new FavoriteManager(context);

// 添加收藏
favorite.addFavorite("标题", "url", "站点", "缩略图");

// 检查是否已收藏
boolean isFavorite = favorite.isFavorite("标题");

// 移除收藏
favorite.removeFavorite("标题");

// 获取所有收藏
List<FavoriteItem> favorites = favorite.getAllFavorites();
```

## 常见问题

### Q1: libmpv 加载失败怎么办？

**A**: 有以下两种解决方案：

1. **使用 ExoPlayer（推荐）**
   ```java
   // 在 PlayerManager 中
   public void initPlayer(PlayerType type) {
       currentPlayer = new ExoPlayerWrapper(context);
   }
   ```

2. **下载 libmpv**
   - 访问 GitHub Release 页面
   - 下载对应架构的 .so 文件
   - 放置到正确的目录

### Q2: 编译时出现 "Cannot resolve symbol"？

**A**: 
1. 点击 File → Invalidate Caches / Restart
2. 或执行：
   ```bash
   ./gradlew clean build --refresh-dependencies
   ```

### Q3: TVBox 源解析失败？

**A**: 
1. 检查 JSON 格式是否正确
2. 使用在线 JSON 校验工具
3. 查看 Logcat 中的详细错误信息
4. 确保网络权限已开启

### Q4: 视频无法播放？

**A**: 
1. 检查 URL 是否有效
2. 确认网络连通性
3. 尝试使用 ExoPlayer
4. 查看播放器日志

### Q5: 运行闪退怎么办？

**A**: 
1. 查看 Logcat 错误日志
2. 检查是否缺少权限
3. 确认 libmpv 架构匹配
4. 尝试在模拟器上运行

## 测试计划

### 单元测试

```bash
# 运行所有单元测试
./gradlew test

# 运行特定测试
./gradlew testDebugUnitTest --tests "*ParserTest"
```

### 仪器测试

```bash
# 连接设备后运行
./gradlew connectedAndroidTest
```

### 手动测试项目

1. **导入功能测试**
   - [ ] 网络导入正常 URL
   - [ ] 网络导入无效 URL
   - [ ] 本地文件导入
   - [ ] 二维码扫描导入

2. **播放功能测试**
   - [ ] MPV 播放器正常播放
   - [ ] ExoPlayer 备用播放
   - [ ] 播放器切换
   - [ ] 暂停/继续功能
   - [ ] seek 功能
   - [ ] 进度记录

3. **历史功能测试**
   - [ ] 自动记录历史
   - [ ] 续播功能
   - [ ] 删除历史
   - [ ] 清空历史

4. **收藏功能测试**
   - [ ] 添加收藏
   - [ ] 取消收藏
   - [ ] 重复收藏检测
   - [ ] 收藏列表

5. **UI 测试**
   - [ ] 手机端布局正常
   - [ ] TV 端布局正常
   - [ ] 遥控器导航（TV）
   - [ ] 触摸操作（手机）

## 发布准备

### 签名配置

在 `gradle.properties` 中：

```properties
RELEASE_STORE_FILE=/path/to/keystore.jks
RELEASE_STORE_PASSWORD=your_password
RELEASE_KEY_ALIAS=alias
RELEASE_KEY_PASSWORD=key_password
```

### 构建 APK

```bash
# Debug 版
./gradlew assembleDebug

# Release 版
./gradlew assembleRelease
```

### 代码混淆

`build.gradle` 中：

```gradle
buildTypes {
    release {
        minifyEnabled true
        shrinkResources true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

## 持续改进

### 性能优化建议

1. **图片加载优化**
   - 使用 Glide 缓存
   - 压缩缩略图
   - 懒加载长列表

2. **网络优化**
   - 添加请求缓存
   - 使用连接池
   - 优化超时设置

3. **内存优化**
   - 及时释放播放器
   - 避免内存泄漏
   - 使用 WeakReference

4. **数据库优化**
   - 使用事务批量操作
   - 添加索引
   - 异步读写

### 功能扩展建议

1. **用户体验**
   - 添加加载动画
   - 优化错误提示
   - 添加引导页

2. **播放功能**
   - 支持弹幕
   - 添加画中画
   - 支持 DLNA 投屏

3. **内容管理**
   - 多源管理
   - 批量导入
   - 源自动更新

4. **界面美化**
   - 支持换肤
   - 夜间模式
   - 自定义布局

## 参考资料

### 官方文档

- [libmpv-android](https://github.com/mpv-android/mpv-android)
- [ExoPlayer](https://exoplayer.dev/)
- [Material Design](https://m3.material.io/)
- [Android TV](https://developer.android.com/training/tv)

### 相关项目

- [TVBoxOSS](https://github.com/CatVodTVOfficial/TVBoxOSS)
- [OK 影视](https://github.com/OKYun/OKMovie)
- [TV 盒子](https://github.com/xianyuyimu/TVBOX)

### 学习资源

- [Android Developers](https://developer.android.com/)
- [Android 公开课](https://developer.android.com/courses)
- [Kotlin 中文文档](https://www.kotlincn.net/)

## 联系方式

如有问题或建议，欢迎：
1. 提交 Issue
2. 发起 Discussion
3. 提交 Pull Request

祝你开发顺利！🎉
