# libmpv-android 集成教程

## 概述

本教程介绍如何在 AIDE 中集成 libmpv-android 播放器库。libmpv 是一个功能强大的跨平台媒体播放器内核，支持多种视频格式和硬件解码。

## 步骤一：下载 libmpv 库

### 方法 1：使用预编译库（推荐）

1. 访问 libmpv-android 官方 GitHub Release 页面：
   https://github.com/mpv-android/mpv-android/releases

2. 下载最新版本的 `libmpv.so` 文件
   - 选择对应架构的版本：`armeabi-v7a`, `arm64-v8a`, `x86`, `x86_64`
   - 建议下载所有架构以支持更多设备

3. 解压下载的压缩包

### 方法 2：自行编译

```bash
# 克隆源码
git clone https://github.com/mpv-android/mpv-android.git

# 进入目录
cd mpv-android

# 编译（需要 NDK 环境）
./buildall.sh
```

编译完成后，在 `build/outputs/` 目录下找到 `.so` 库文件。

## 步骤二：复制库文件到项目

将下载的 `.so` 文件按架构复制到项目目录：

```
mobile-tvbox-player/app/src/main/jniLibs/
├── armeabi-v7a/
│   └── libmpv.so
├── arm64-v8a/
│   └── libmpv.so
├── x86/
│   └── libmpv.so
└── x86_64/
    └── libmpv.so
```

**复制命令示例**：
```bash
# 假设下载文件在 ~/Downloads/mpv-android
cp ~/Downloads/mpv-android/armeabi-v7a/libmpv.so \
   /workspace/mobile-tvbox-player/app/src/main/jniLibs/armeabi-v7a/

cp ~/Downloads/mpv-android/arm64-v8a/libmpv.so \
   /workspace/mobile-tvbox-player/app/src/main/jniLibs/arm64-v8a/
```

## 步骤三：添加头文件（可选）

如果需要使用 mpv 的高级功能，可能需要头文件：

1. 从源码中获取头文件：
   ```bash
   # 从 mpv-android 源码复制
   cp -r mpv-android/app/src/main/jni/include \
       /workspace/mobile-tvbox-player/app/src/main/cpp/include
   ```

2. 头文件目录结构：
   ```
   app/src/main/cpp/include/
   └── mpv/
       ├── client.h
       ├── render.h
       ├── render_gl.h
       └── stream_cb.h
   ```

## 步骤四：配置 CMake

项目已经包含了 `CMakeLists.txt`，检查以下配置：

```cmake
# 查找 mpv 库
find_library(mpv-lib mpv)

# 链接到 JNI 库
target_link_libraries(
    mpv-jni
    ${mpv-lib}
    android
    log
    EGL
    GLESv2
    OpenSLES
)
```

## 步骤五：配置 build.gradle

在 `app/build.gradle` 中确保有以下配置：

```gradle
android {
    defaultConfig {
        externalNativeBuild {
            cmake {
                cppFlags ""
            }
        }
    }
    
    externalNativeBuild {
        cmake {
            path "src/main/cpp/CMakeLists.txt"
            version "3.22.1"
        }
    }
    
    ndk {
        # 支持的架构
        abiFilters 'armeabi-v7a', 'arm64-v8a', 'x86', 'x86_64'
    }
}
```

## 步骤六：编译 JNI 库

在 Android Studio 或 AIDE 中：

1. **Android Studio**:
   ```bash
   # 点击 Build > Make Project
   # 或使用命令行
   ./gradlew assembleDebug
   ```

2. **AIDE**:
   - 打开项目
   - 点击"构建和运行"
   - AIDE 会自动编译 JNI 代码

## 步骤七：验证集成

运行以下测试代码：

```java
public class MpvTest {
    static {
        try {
            System.loadLibrary("mpv");
            Log.i("MpvTest", "libmpv loaded successfully");
        } catch (UnsatisfiedLinkError e) {
            Log.e("MpvTest", "Failed to load libmpv: " + e.getMessage());
        }
    }
    
    public static boolean testCreate() {
        try {
            MpvPlayer player = new MpvPlayer(context);
            return player.getMpvHandle() != 0;
        } catch (Exception e) {
            Log.e("MpvTest", "Test failed: " + e.getMessage());
            return false;
        }
    }
}
```

## 常见问题

### 1. UnsatisfiedLinkError: Couldn't load libmpv

**原因**：未找到 `.so` 文件

**解决方法**：
- 检查 `jniLibs` 目录下是否有对应架构的 `.so` 文件
- 确认 `build.gradle` 中的 `abiFilters` 设置正确
- 清理并重新构建项目：`Build > Clean Project > Rebuild Project`

### 2. 编译错误：mpv/client.h: No such file or directory

**原因**：缺少头文件

**解决方法**：
- 下载或使用完整源码中的头文件
- 或简化 JNI 代码，减少对外部头文件的依赖

### 3. 运行崩溃：Illegal Instruction

**原因**：CPU 架构不匹配

**解决方法**：
- 确保下载了对应设备架构的 `.so` 文件
- 同时提供多种架构版本（至少包括 `armeabi-v7a` 和 `arm64-v8a`）

### 4. 播放失败：初始化错误

**原因**：Surface 设置不正确

**解决方法**：
- 确保 `setSurface()` 在 `play()` 之前调用
- 检查 Surface 是否有效：`surface.isValid()`

## AIDE 特别说明

### AIDE 编译设置

在 AIDE 中打开项目设置：
1. 进入 Settings > Build and Run
2. 启用"Native C/C++ Code"
3. 设置正确的 NDK 路径（如果 AIDE 未自动检测）

### AIDE 调试

- AIDE 支持原生代码调试
- 可以在 Java 代码中设置断点调试 JNI 调用
- 查看 Logcat 中的 C/C++ 日志输出

## 性能优化

### 1. 启用硬件解码

```java
mpvSetOptionString(handle, "hwdec", "mediacodec");
```

### 2. 配置缓存

```java
mpvSetOptionString(handle, "demuxer-max-bytes", "104857600");
mpvSetOptionString(handle, "demuxer-max-back-bytes", "52428800");
```

### 3. 设置视频输出

```java
mpvSetOptionString(handle, "vo", "android-surface");
```

## 替代方案：使用 ExoPlayer

如果遇到 libmpv 集成问题，可以完全使用 ExoPlayer：

```java
// 在 PlayerManager 中
public void initPlayer(PlayerType type) {
    // 始终使用 ExoPlayer
    currentPlayer = new ExoPlayerWrapper(context);
}
```

ExoPlayer 优点：
- Google 官方支持
- 与 Android 系统深度集成
- 文档齐全，易于调试

缺点：
- 某些视频格式支持不如 libmpv
- 高级功能需要自行实现

## 参考资料

- [libmpv-android 官方文档](https://github.com/mpv-android/mpv-android)
- [MPV API 参考](https://github.com/mpv-player/mpv/blob/master/DOCS/client-api-guide.md)
- [Android NDK 开发指南](https://developer.android.com/ndk/guides)
- [CMake 文档](https://developer.android.com/studio/projects/configure-cmake)

## 下一步

完成 libmpv 集成后，继续：
1. 测试基本播放功能
2. 优化播放性能
3. 添加播放控制（暂停、seek、音量等）
4. 实现字幕支持
5. 添加音频轨道切换
