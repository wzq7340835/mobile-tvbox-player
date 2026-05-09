package com.fongmi.android.tv.api

import com.fongmi.android.tv.App
import com.fongmi.android.tv.bean.*
import com.fongmi.android.tv.engine.Spider
import com.fongmi.android.tv.engine.SpiderLoader
import com.fongmi.android.tv.rust.RustDecoder
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Decoder {

    fun aes(data: String, key: String, iv: String): String? {
        return RustDecoder.decodeAesString(data, key, iv)
    }

    fun base64(data: String): String? {
        return RustDecoder.decodeBase64String(data)
    }

    fun checkUrl(url: String): Boolean {
        return url.startsWith("http") || url.startsWith("rtmp") || url.startsWith("rtsp")
    }
}

object SiteApi {

    private val gson = Gson()

    suspend fun homeContent(site: Site, filter: Boolean): Result {
        return withContext(Dispatchers.IO) {
            try {
                val spider = getSpider(site)
                val json = spider.homeContent(filter)
                gson.fromJson(json, Result::class.java) ?: Result()
            } catch (e: Exception) {
                Result()
            }
        }
    }

    suspend fun homeVideoContent(site: Site): Result {
        return withContext(Dispatchers.IO) {
            try {
                val spider = getSpider(site)
                val json = spider.homeVideoContent()
                gson.fromJson(json, Result::class.java) ?: Result()
            } catch (e: Exception) {
                Result()
            }
        }
    }

    suspend fun category(site: Site, tid: String, pg: String, filter: Boolean, extend: Map<String, String>): Result {
        return withContext(Dispatchers.IO) {
            try {
                val spider = getSpider(site)
                val json = spider.category(tid, pg, filter, extend)
                gson.fromJson(json, Result::class.java) ?: Result()
            } catch (e: Exception) {
                Result()
            }
        }
    }

    suspend fun detail(site: Site, id: String): Result {
        return withContext(Dispatchers.IO) {
            try {
                val spider = getSpider(site)
                val json = spider.detail(id)
                gson.fromJson(json, Result::class.java) ?: Result()
            } catch (e: Exception) {
                Result()
            }
        }
    }

    suspend fun play(site: Site, flag: String, id: List<String>, vipFlags: List<String>): Result {
        return withContext(Dispatchers.IO) {
            try {
                val spider = getSpider(site)
                val json = spider.play(flag, id, vipFlags)
                gson.fromJson(json, Result::class.java) ?: Result()
            } catch (e: Exception) {
                Result()
            }
        }
    }

    suspend fun search(site: Site, keyword: String, quick: Boolean): Result {
        return withContext(Dispatchers.IO) {
            try {
                val spider = getSpider(site)
                val json = spider.search(keyword, quick)
                gson.fromJson(json, Result::class.java) ?: Result()
            } catch (e: Exception) {
                Result()
            }
        }
    }

    private fun getSpider(site: Site): Spider {
        site.spider?.let { return it }
        val spider = SpiderLoader.load(site.key, site.api)
        spider.init(App.get(), site.ext)
        site.spider = spider
        return spider
    }
}

object LiveApi {

    private val gson = Gson()

    suspend fun parse(url: String, type: Int): List<Group> {
        return withContext(Dispatchers.IO) {
            try {
                when (type) {
                    Live.TYPE_XML -> parseXml(url)
                    Live.TYPE_TEXT -> parseText(url)
                    else -> emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    private suspend fun parseXml(url: String): List<Group> {
        val content = com.fongmi.android.tv.rust.RustNetwork.getString(url)
            ?: return emptyList()
        return parseXmlContent(content)
    }

    private fun parseXmlContent(content: String): List<Group> {
        val groups = mutableListOf<Group>()
        val groupPattern = Regex("""<group\s+name="([^"]*)"(?:\s+logo="([^"]*)")?""")
        val channelPattern = Regex("""<channel\s+name="([^"]*)"(?:\s+logo="([^"]*)")?(?:\s+epg="([^"]*)")?>([\s\S]*?)</channel>""")
        val urlPattern = Regex("""<url\s+name="([^"]*)">(.*?)</url>""")

        var currentGroup: Group? = null
        val lines = content.lines()

        for (line in lines) {
            groupPattern.find(line)?.let { match ->
                currentGroup = Group(name = match.groupValues[1], logo = match.groupValues[2])
                groups.add(currentGroup!!)
            }
            channelPattern.find(line)?.let { match ->
                val channel = Channel(
                    name = match.groupValues[1],
                    logo = match.groupValues[2],
                    epg = match.groupValues[3]
                )
                val urls = urlPattern.findAll(match.groupValues[4])
                    .map { it.groupValues[2] }.toList()
                currentGroup?.let { g ->
                    val channels = g.channels.toMutableList()
                    channels.add(channel.copy(urls = urls))
                    g.channels = channels
                }
            }
        }
        return groups
    }

    private suspend fun parseText(url: String): List<Group> {
        val content = com.fongmi.android.tv.rust.RustNetwork.getString(url)
            ?: return emptyList()
        return parseTextContent(content)
    }

    private fun parseTextContent(content: String): List<Group> {
        val groupMap = linkedMapOf<String, MutableList<Channel>>()
        val lines = content.lines()

        for (line in lines) {
            if (line.isBlank()) continue
            if (line.contains("#genre#")) {
                val groupName = line.replace(",#genre#", "").trim()
                if (!groupMap.containsKey(groupName)) {
                    groupMap[groupName] = mutableListOf()
                }
                continue
            }
            val parts = line.split(",")
            if (parts.size >= 2) {
                val name = parts[0].trim()
                val channelUrl = parts[1].trim()
                val currentGroup = groupMap.keys.lastOrNull() ?: ""
                val channels = groupMap.getOrPut(currentGroup) { mutableListOf() }
                val existing = channels.find { it.name == name }
                if (existing != null) {
                    val urls = existing.urls.toMutableList()
                    urls.add(channelUrl)
                    val idx = channels.indexOf(existing)
                    channels[idx] = existing.copy(urls = urls)
                } else {
                    channels.add(Channel(name = name, urls = listOf(channelUrl)))
                }
            }
        }

        return groupMap.map { (name, channels) ->
            Group(name = name, channels = channels)
        }
    }
}
