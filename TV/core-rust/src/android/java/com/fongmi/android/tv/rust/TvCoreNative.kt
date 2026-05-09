package com.fongmi.android.tv.rust

object TvCoreNative {

    init {
        System.loadLibrary("tv_core")
    }

    fun init() {
        nativeInit()
    }

    fun decodeAes(data: String, key: String, iv: String): ByteArray? {
        return nativeDecodeAes(data, key, iv)
    }

    fun decodeBase64(data: String): ByteArray? {
        return nativeDecodeBase64(data)
    }

    fun cachePut(key: String, data: ByteArray): Boolean {
        return nativeCachePut(key, data)
    }

    fun cacheGet(key: String): ByteArray? {
        return nativeCacheGet(key)
    }

    fun networkGet(url: String, headers: String, timeoutMs: Int): ByteArray? {
        return nativeNetworkGet(url, headers, timeoutMs)
    }

    fun networkPost(url: String, body: String, headers: String, timeoutMs: Int): ByteArray? {
        return nativeNetworkPost(url, body, headers, timeoutMs)
    }

    fun playerCreate(): Long {
        return nativePlayerCreate()
    }

    fun playerRelease(handle: Long) {
        nativePlayerRelease(handle)
    }

    fun snifferMatch(url: String, patterns: Array<String>): Boolean {
        return nativeSnifferMatch(url, patterns)
    }

    private external fun nativeInit()
    private external fun nativeDecodeAes(data: String, key: String, iv: String): ByteArray?
    private external fun nativeDecodeBase64(data: String): ByteArray?
    private external fun nativeCachePut(key: String, data: ByteArray): Boolean
    private external fun nativeCacheGet(key: String): ByteArray?
    private external fun nativeNetworkGet(url: String, headers: String, timeoutMs: Int): ByteArray?
    private external fun nativeNetworkPost(url: String, body: String, headers: String, timeoutMs: Int): ByteArray?
    private external fun nativePlayerCreate(): Long
    private external fun nativePlayerRelease(handle: Long)
    private external fun nativeSnifferMatch(url: String, patterns: Array<String>): Boolean
}
