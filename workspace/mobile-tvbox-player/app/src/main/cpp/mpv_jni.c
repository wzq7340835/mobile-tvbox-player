#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include <string.h>

// MPV 头文件（需要安装 libmpv 开发库）
#include <mpv/client.h>

#define LOG_TAG "MpvJNI"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

// 全局 mpv 句柄
static mpv_handle *g_mpv = NULL;

/**
 * 创建 MPV 实例
 */
JNIEXPORT jlong JNICALL
Java_com_player_tvbox_player_MpvPlayer_mpvCreate(JNIEnv *env, jobject thiz) {
    LOGI("Creating MPV instance");
    
    g_mpv = mpv_create();
    if (!g_mpv) {
        LOGE("Failed to create MPV instance");
        return 0;
    }
    
    return (jlong)g_mpv;
}

/**
 * 初始化 MPV
 */
JNIEXPORT jint JNICALL
Java_com_player_tvbox_player_MpvPlayer_mpvInitialize(JNIEnv *env, jobject thiz, 
                                                      jlong handle) {
    mpv_handle *mpv = (mpv_handle *)handle;
    
    LOGI("Initializing MPV");
    
    int ret = mpv_initialize(mpv);
    if (ret < 0) {
        LOGE("Failed to initialize MPV: %s", mpv_error_string(ret));
        return ret;
    }
    
    LOGI("MPV initialized successfully");
    return 0;
}

/**
 * 设置字符串选项
 */
JNIEXPORT void JNICALL
Java_com_player_tvbox_player_MpvPlayer_mpvSetOptionString(JNIEnv *env, jobject thiz,
                                                           jlong handle, 
                                                           jstring name, 
                                                           jstring value) {
    mpv_handle *mpv = (mpv_handle *)handle;
    
    const char *n = (*env)->GetStringUTFChars(env, name, 0);
    const char *v = (*env)->GetStringUTFChars(env, value, 0);
    
    LOGI("Setting option: %s = %s", n, v);
    
    int ret = mpv_set_option_string(mpv, n, v);
    if (ret < 0) {
        LOGE("Failed to set option %s: %s", n, mpv_error_string(ret));
    }
    
    (*env)->ReleaseStringUTFChars(env, name, n);
    (*env)->ReleaseStringUTFChars(env, value, v);
}

/**
 * 执行 MPV 命令
 */
JNIEXPORT jint JNICALL
Java_com_player_tvbox_player_MpvPlayer_mpvCommand(JNIEnv *env, jobject thiz,
                                                   jlong handle, 
                                                   jstring command) {
    mpv_handle *mpv = (mpv_handle *)handle;
    
    const char *cmd = (*env)->GetStringUTFChars(env, command, 0);
    
    LOGI("Executing command: %s", cmd);
    
    // 解析命令
    const char *argv[3];
    argv[0] = "loadfile";
    argv[1] = cmd;
    argv[2] = NULL;
    
    int ret = mpv_command(mpv, argv);
    if (ret < 0) {
        LOGE("Command failed: %s", mpv_error_string(ret));
    }
    
    (*env)->ReleaseStringUTFChars(env, command, cmd);
    
    return ret;
}

/**
 * 设置字符串属性
 */
JNIEXPORT void JNICALL
Java_com_player_tvbox_player_MpvPlayer_mpvSetPropertyString(JNIEnv *env, jobject thiz,
                                                             jlong handle, 
                                                             jstring name, 
                                                             jstring value) {
    mpv_handle *mpv = (mpv_handle *)handle;
    
    const char *n = (*env)->GetStringUTFChars(env, name, 0);
    const char *v = (*env)->GetStringUTFChars(env, value, 0);
    
    int ret = mpv_set_property_string(mpv, n, v);
    if (ret < 0) {
        LOGE("Failed to set property %s: %s", n, mpv_error_string(ret));
    }
    
    (*env)->ReleaseStringUTFChars(env, name, n);
    (*env)->ReleaseStringUTFChars(env, value, v);
}

/**
 * 获取字符串属性
 */
JNIEXPORT jstring JNICALL
Java_com_player_tvbox_player_MpvPlayer_mpvGetPropertyString(JNIEnv *env, jobject thiz,
                                                             jlong handle, 
                                                             jstring name) {
    mpv_handle *mpv = (mpv_handle *)handle;
    
    const char *n = (*env)->GetStringUTFChars(env, name, 0);
    
    char *value = NULL;
    int ret = mpv_get_property_string(mpv, n, &value);
    
    (*env)->ReleaseStringUTFChars(env, name, n);
    
    if (ret < 0 || !value) {
        return NULL;
    }
    
    jstring result = (*env)->NewStringUTF(env, value);
    mpv_free(value);
    
    return result;
}

/**
 * 获取长整型属性
 */
JNIEXPORT jlong JNICALL
Java_com_player_tvbox_player_MpvPlayer_mpvGetPropertyLong(JNIEnv *env, jobject thiz,
                                                           jlong handle, 
                                                           jstring name) {
    mpv_handle *mpv = (mpv_handle *)handle;
    
    const char *n = (*env)->GetStringUTFChars(env, name, 0);
    
    int64_t value;
    int ret = mpv_get_property(mpv, n, MPV_FORMAT_INT64, &value);
    
    (*env)->ReleaseStringUTFChars(env, name, n);
    
    if (ret < 0) {
        return 0;
    }
    
    return (jlong)value;
}

/**
 * 获取双精度浮点属性
 */
JNIEXPORT jdouble JNICALL
Java_com_player_tvbox_player_MpvPlayer_mpvGetPropertyDouble(JNIEnv *env, jobject thiz,
                                                             jlong handle, 
                                                             jstring name) {
    mpv_handle *mpv = (mpv_handle *)handle;
    
    const char *n = (*env)->GetStringUTFChars(env, name, 0);
    
    double value;
    int ret = mpv_get_property(mpv, n, MPV_FORMAT_DOUBLE, &value);
    
    (*env)->ReleaseStringUTFChars(env, name, n);
    
    if (ret < 0) {
        return 0.0;
    }
    
    return value;
}

/**
 * 销毁 MPV 实例
 */
JNIEXPORT void JNICALL
Java_com_player_tvbox_player_MpvPlayer_mpvDestroy(JNIEnv *env, jobject thiz,
                                                   jlong handle) {
    mpv_handle *mpv = (mpv_handle *)handle;
    
    LOGI("Destroying MPV instance");
    
    if (mpv) {
        mpv_terminate_destroy(mpv);
        g_mpv = NULL;
    }
}
