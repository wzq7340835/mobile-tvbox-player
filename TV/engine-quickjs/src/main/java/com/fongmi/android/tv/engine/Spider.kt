package com.fongmi.android.tv.engine

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class Spider {

    open fun init(context: android.content.Context, extend: String) {}
    open fun homeContent(filter: Boolean): String = "{}"
    open fun homeVideoContent(): String = "{}"
    open fun category(tid: String, pg: String, filter: Boolean, extend: Map<String, String>): String = "{}"
    open fun detail(id: String): String = "{}"
    open fun play(flag: String, id: List<String>, vipFlags: List<String>): String = "{}"
    open fun search(keyword: String, quick: Boolean): String = "{}"
    open fun isVideoFormat(url: String): Boolean = false
    open fun manualVideoCheck(): Boolean = false
    open fun destroy() {}
}

class JsSpider(
    private val spiderKey: String
) : Spider() {

    private val gson = Gson()

    override fun init(context: android.content.Context, extend: String) {
        callMethod("init", gson.toJson(mapOf("extend" to extend)))
    }

    override fun homeContent(filter: Boolean): String {
        return callMethod("homeContent", gson.toJson(mapOf("filter" to filter))) ?: "{}"
    }

    override fun homeVideoContent(): String {
        return callMethod("homeVideoContent", "") ?: "{}"
    }

    override fun category(tid: String, pg: String, filter: Boolean, extend: Map<String, String>): String {
        val args = gson.toJson(mapOf("tid" to tid, "pg" to pg, "filter" to filter, "extend" to extend))
        return callMethod("category", args) ?: "{}"
    }

    override fun detail(id: String): String {
        return callMethod("detail", gson.toJson(mapOf("id" to id))) ?: "{}"
    }

    override fun play(flag: String, id: List<String>, vipFlags: List<String>): String {
        val args = gson.toJson(mapOf("flag" to flag, "id" to id, "vipFlags" to vipFlags))
        return callMethod("play", args) ?: "{}"
    }

    override fun search(keyword: String, quick: Boolean): String {
        return callMethod("search", gson.toJson(mapOf("keyword" to keyword, "quick" to quick))) ?: "{}"
    }

    override fun isVideoFormat(url: String): Boolean {
        val result = callMethod("isVideoFormat", gson.toJson(mapOf("url" to url))) ?: "false"
        return result.toBoolean()
    }

    override fun manualVideoCheck(): Boolean {
        val result = callMethod("manualVideoCheck", "") ?: "false"
        return result.toBoolean()
    }

    override fun destroy() {
        QuickJsNative.removeSpider(spiderKey)
    }

    private fun callMethod(method: String, args: String): String? {
        return QuickJsNative.callMethod(spiderKey, method, args)
    }
}

object SpiderLoader {

    private val spiders = mutableMapOf<String, Spider>()
    private val gson = Gson()

    fun load(spiderKey: String, jsCode: String): Spider {
        val existing = spiders[spiderKey]
        if (existing != null) return existing

        val success = QuickJsNative.createSpider(spiderKey, jsCode)
        if (!success) return SpiderNull

        val spider = JsSpider(spiderKey)
        spiders[spiderKey] = spider
        return spider
    }

    fun get(spiderKey: String): Spider {
        return spiders[spiderKey] ?: SpiderNull
    }

    fun remove(spiderKey: String) {
        spiders.remove(spiderKey)?.destroy()
    }

    fun clear() {
        spiders.values.forEach { it.destroy() }
        spiders.clear()
        QuickJsNative.clear()
    }
}

object SpiderNull : Spider()
