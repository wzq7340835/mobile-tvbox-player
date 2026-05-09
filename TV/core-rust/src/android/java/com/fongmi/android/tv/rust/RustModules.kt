package com.fongmi.android.tv.rust

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RustDecoder {

    fun decodeAes(data: String, key: String, iv: String): ByteArray? {
        return TvCoreNative.decodeAes(data, key, iv)
    }

    fun decodeBase64(data: String): ByteArray? {
        return TvCoreNative.decodeBase64(data)
    }

    fun decodeAesString(data: String, key: String, iv: String): String? {
        val bytes = decodeAes(data, key, iv) ?: return null
        return String(bytes, Charsets.UTF_8)
    }

    fun decodeBase64String(data: String): String? {
        val bytes = decodeBase64(data) ?: return null
        return String(bytes, Charsets.UTF_8)
    }
}

object RustCache {

    fun put(key: String, data: ByteArray): Boolean {
        return TvCoreNative.cachePut(key, data)
    }

    fun put(key: String, data: String): Boolean {
        return TvCoreNative.cachePut(key, data.toByteArray(Charsets.UTF_8))
    }

    fun get(key: String): ByteArray? {
        return TvCoreNative.cacheGet(key)
    }

    fun getString(key: String): String? {
        val bytes = get(key) ?: return null
        return String(bytes, Charsets.UTF_8)
    }
}

object RustNetwork {

    suspend fun get(url: String, headers: String = "", timeoutMs: Int = 15000): ByteArray? {
        return withContext(Dispatchers.IO) {
            TvCoreNative.networkGet(url, headers, timeoutMs)
        }
    }

    suspend fun post(url: String, body: String, headers: String = "", timeoutMs: Int = 15000): ByteArray? {
        return withContext(Dispatchers.IO) {
            TvCoreNative.networkPost(url, body, headers, timeoutMs)
        }
    }

    suspend fun getString(url: String, headers: String = "", timeoutMs: Int = 15000): String? {
        val bytes = get(url, headers, timeoutMs) ?: return null
        return String(bytes, Charsets.UTF_8)
    }

    suspend fun postString(url: String, body: String, headers: String = "", timeoutMs: Int = 15000): String? {
        val bytes = post(url, body, headers, timeoutMs) ?: return null
        return String(bytes, Charsets.UTF_8)
    }
}

object RustSniffer {

    fun match(url: String, patterns: Array<String>): Boolean {
        return TvCoreNative.snifferMatch(url, patterns)
    }
}
