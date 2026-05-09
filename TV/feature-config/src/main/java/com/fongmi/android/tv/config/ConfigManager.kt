package com.fongmi.android.tv.config

import android.content.Context
import com.fongmi.android.tv.bean.*
import com.fongmi.android.tv.engine.SpiderLoader
import com.fongmi.android.tv.rust.RustCache
import com.fongmi.android.tv.rust.RustNetwork
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ConfigManager {

    private val gson = Gson()

    suspend fun loadConfig(url: String): Core? {
        return withContext(Dispatchers.IO) {
            try {
                val json = RustNetwork.getString(url) ?: return@withContext null
                RustCache.put("config_$url", json)
                parseCore(json)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun loadConfigFromCache(url: String): Core? {
        return withContext(Dispatchers.IO) {
            try {
                val json = RustCache.getString("config_$url") ?: return@withContext null
                parseCore(json)
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun parseCore(json: String): Core? {
        return try {
            gson.fromJson(json, Core::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun initSpiders(sites: List<Site>) {
        withContext(Dispatchers.IO) {
            sites.forEach { site ->
                if (site.isJs() || site.isCsp()) {
                    try {
                        val jsCode = if (site.api.startsWith("http")) {
                            RustNetwork.getString(site.api) ?: ""
                        } else {
                            site.api
                        }
                        SpiderLoader.load(site.key, jsCode)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun clearSpiders() {
        SpiderLoader.clear()
    }

    fun clearCache() {
        RustCache.put("", ByteArray(0))
    }
}

object Init {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun get(): Context = appContext
}
