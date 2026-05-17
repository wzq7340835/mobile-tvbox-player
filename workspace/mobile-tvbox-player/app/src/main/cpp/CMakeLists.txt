cmake_minimum_required(VERSION 3.22.1)

project("tvboxplayer")

# 查找 mpv 库（需要在 APP 的 build.gradle 中配置）
find_library(mpv-lib mpv)

# 添加 JNI 源文件
add_library(
        mpv-jni
        SHARED
        mpv_jni.c
)

# 包含目录
include_directories(
        ${CMAKE_SOURCE_DIR}/include
)

# 链接库
target_link_libraries(
        mpv-jni
        ${mpv-lib}
        android
        log
        EGL
        GLESv2
        OpenSLES
)
