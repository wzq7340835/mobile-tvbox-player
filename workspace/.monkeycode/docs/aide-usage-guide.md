# AIDE 使用说明

## 简介

AIDE (Android IDE) 是一款可以在 Android 设备上直接进行 Android 应用开发的集成开发环境。本说明将指导你如何在 AIDE 中打开、配置和运行 Mobile TVBox Player 项目。

## 环境准备

### 1. 安装 AIDE

从 Google Play 或官网下载并安装 AIDE：
- **应用名称**: AIDE - IDE for Android Java/C++
- **包名**: com.aide.ui
- **官网**: https://www.aide.info

### 2. 安装必要插件

在 AIDE 内安装以下插件：
1. 打开 AIDE
2. 点击菜单 → Settings → Plugins
3. 安装：
   - NDK Plugin (用于 JNI 开发)
   - Git Plugin (可选，版本控制)

### 3. 配置 NDK

如果项目包含 JNI 代码（如 libmpv）：
1. 进入 Settings → Build and Run
2. 确保 "Native C/C++ Code" 已启用
3. 检查 NDK 路径是否正确

## 打开项目

### 方法一：从存储卡打开

1. 将项目文件夹复制到设备存储
   ```
   /sdcard/mobile-tvbox-player/
   ```

2. 打开 AIDE
3. 点击 "打开项目"
4. 浏览到 `mobile-tvbox-player` 目录
5. 选择并打开

### 方法二：使用 Git 克隆

如果项目托管在 Git 仓库：

1. 确保已安装 Git 插件
2. AIDE 主界面 → Git → Clone Repository
3. 输入仓库 URL
4. 输入认证信息（如果需要）
5. 选择保存位置后克隆

## 项目配置

### 1. 检查 Gradle 配置

打开 `app/build.gradle`，确认：
- `compileSdk` 版本与 AIDE 兼容（建议 33 或更低）
- `buildToolsVersion` 与 AIDE 一致

### 2. 下载依赖

首次打开项目时，AIDE 会自动下载 Gradle 依赖：
- 查看底部进度条
- 等待下载完成

如果下载失败：
1. 点击菜单 → Refresh
2. 或重新打开项目

### 3. 配置 libmpv 库

**重要**: libmpv 的 `.so` 文件需要手动放置

1. 下载 libmpv-android 预编译库
2. 解压得到 `.so` 文件
3. 复制到对应目录：
   ```
   app/src/main/jniLibs/armeabi-v7a/libmpv.so
   app/src/main/jniLibs/arm64-v8a/libmpv.so
   ```

## 编译项目

### 编译 APK

1. 点击菜单 → Build and Run（或按 Shift+F5）
2. AIDE 开始编译
3. 查看编译日志
4. 成功会提示 "Build successful"

### 编译选项

在 Settings → Build and Run 中可以配置：
- Build Type: Debug / Release
- Install app: 是否自动安装
- Run app: 是否自动启动

## 运行项目

### 在设备上运行

1. 确保设备已安装 APK
2. 编译完成后自动启动
3. 或在菜单 → Run App

### 在模拟器上运行

需要额外安装 Android 模拟器应用：
- 推荐：Android x86 模拟器
- 或：VMOS（虚拟 Android 系统）

## 调试

AIDE 支持基本的调试功能：

### 设置断点

1. 在代码行号左侧点击
2. 红色圆点表示断点已设置

### 启动调试

1. 菜单 → Debug and Run（或按 F9）
2. 程序会在断点处暂停

### 调试操作

- **Step Over (F10)**: 单步执行，不进入函数
- **Step Into (F11)**: 进入函数内部
- **Continue (F8)**: 继续执行到下一个断点
- **Stop**: 停止调试

## 代码编辑

### Java 代码编辑

- 语法高亮
- 代码自动完成（Ctrl+Space）
- 实时错误检查
- 重构功能（重命名、提取方法等）

### XML 资源编辑

- 布局预览
- 资源引用检查
- 代码补全

### 快速导航

- **Ctrl+O**: 快速打开文件
- **Ctrl+Shift+O**: 快速打开类
- **Ctrl+Shift+R**: 按名称搜索资源

## 常见问题

### 1. Build 失败：Unsupported class file major version

**原因**: Gradle 版本过高

**解决**:
```gradle
// 修改 gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-7.5-bin.zip
```

### 2. 找不到 libmpv.so

**原因**: 库文件未放置到正确位置

**解决**:
1. 检查 `app/src/main/jniLibs/` 目录
2. 确认架构匹配的 `.so` 文件存在
3. 清理并重新编译

### 3. 编译时内存不足

**原因**: 项目过大或 AIDE 内存限制

**解决**:
```properties
# 在 gradle.properties 中添加
org.gradle.jvmargs=-Xmx1024m
```

### 4. 无法下载依赖

**原因**: 网络连接问题

**解决**:
1. 切换到稳定的 Wi-Fi
2. 使用国内镜像（阿里云或腾讯云）
3. 或手动下载依赖到本地仓库

### 5. JNI 编译失败

**原因**: NDK 未正确配置

**解决**:
1. Settings → Build and Run
2. 确认 "Native C/C++ Code" 已启用
3. 检查 NDK 路径
4. 重新安装 NDK 插件

## 优化建议

### 1. 使用外部键盘

- 连接蓝牙键盘
- 提升编码效率
- 快捷键操作更方便

### 2. 分屏显示

- 在平板上使用分屏
- 一边看代码一边编辑
- 或使用悬浮窗

### 3. 云同步

- 使用 Git 定期推送代码
- 防止数据丢失
- 多设备同步开发

### 4. 代码备份

- 定期导出项目
- 压缩并保存到云盘
- 或使用 Git 备份

## 导出 APK

### 调试版 APK

```bash
# 编译后自动保存在
app/build/outputs/apk/debug/app-debug.apk
```

### 发布版 APK

1. 生成签名密钥：
   ```bash
   keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 20000 -alias my-alias
   ```

2. 在 `gradle.properties` 配置签名

3. 编译 Release 版本：
   ```
   Build and Run → 选择 Release 模式
   ```

4. APK 位置：
   ```
   app/build/outputs/apk/release/app-release.apk
   ```

## 性能优化

### 1. 减少依赖

- 只使用必要的库
- 移除未使用的依赖
- 使用轻量级替代

### 2. 优化资源

- 压缩图片资源
- 删除未使用的资源
- 使用 WebP 格式

### 3. 代码优化

- 启用 ProGuard
- 移除无用代码
- 使用高效算法

## 最佳实践

1. **定期编译**: 每完成一个功能就编译一次，及时发现问题
2. **版本控制**: 使用 Git 管理代码版本
3. **代码审查**: 仔细阅读错误信息，逐步修复
4. **测试**: 在不同设备上测试应用
5. **备份**: 重要修改前先备份

## 学习资源

- [AIDE 官方教程](http://www.aide.info/doc/)
- [Android 开发者指南](https://developer.android.com/guide)
- [Gradle 构建指南](https://developer.android.com/studio/build)
- [JNI 开发教程](https://developer.android.com/ndk/guides)

## 技术支持

遇到问题时：
1. 查看编译日志
2. 搜索 AIDE 论坛
3. 查阅 Android 官方文档
4. 在 GitHub 提 Issue

## 下一步

完成环境配置后：
1. 运行应用测试基本功能
2. 调试和优化代码
3. 添加自定义功能
4. 准备发布 APK

祝你在 AIDE 上开发顺利！
