package com.fongmi.android.tv.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Parse(
    @SerializedName("name") var name: String = "",
    @SerializedName("type") var type: Int = 0,
    @SerializedName("url") var url: String = "",
    @SerializedName("ext") var ext: Map<String, String> = emptyMap(),
    var selected: Boolean = false
) : Parcelable

@Parcelize
data class Track(
    @SerializedName("index") var index: Int = 0,
    @SerializedName("label") var label: String = "",
    @SerializedName("mimeType") var mimeType: String = "",
    @SerializedName("language") var language: String = "",
    @SerializedName("selected") var selected: Boolean = false
) : Parcelable

@Parcelize
data class Danmaku(
    @SerializedName("p") var p: String = "",
    @SerializedName("m") var m: String = ""
) : Parcelable {
    fun getTime(): Float = p.split(",").getOrNull(0)?.toFloatOrNull() ?: 0f
    fun getColor(): Int = p.split(",").getOrNull(2)?.toIntOrNull() ?: 0xFFFFFF
    fun getType(): Int = p.split(",").getOrNull(1)?.toIntOrNull() ?: 0
}

@Parcelize
data class History(
    @SerializedName("vodId") var vodId: String = "",
    @SerializedName("vodName") var vodName: String = "",
    @SerializedName("vodPic") var vodPic: String = "",
    @SerializedName("sourceKey") var sourceKey: String = "",
    @SerializedName("sourceName") var sourceName: String = "",
    @SerializedName("episodeName") var episodeName: String = "",
    @SerializedName("episodeUrl") var episodeUrl: String = "",
    @SerializedName("position") var position: Long = 0,
    @SerializedName("duration") var duration: Long = 0,
    @SerializedName("time") var time: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class Keep(
    @SerializedName("vodId") var vodId: String = "",
    @SerializedName("vodName") var vodName: String = "",
    @SerializedName("vodPic") var vodPic: String = "",
    @SerializedName("sourceKey") var sourceKey: String = "",
    @SerializedName("sourceName") var sourceName: String = "",
    @SerializedName("typeName") var typeName: String = "",
    @SerializedName("vodRemarks") var vodRemarks: String = "",
    @SerializedName("time") var time: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class Collect(
    @SerializedName("url") var url: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("group") var group: String = "",
    @SerializedName("logo") var logo: String = "",
    @SerializedName("time") var time: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class Backup(
    @SerializedName("configs") var configs: List<Config> = emptyList(),
    @SerializedName("keeps") var keeps: List<Keep> = emptyList(),
    @SerializedName("histories") var histories: List<History> = emptyList(),
    @SerializedName("collects") var collects: List<Collect> = emptyList(),
    @SerializedName("settings") var settings: Map<String, String> = emptyMap()
) : Parcelable

@Parcelize
data class Epg(
    @SerializedName("name") var name: String = "",
    @SerializedName("data") var data: List<EpgData> = emptyList()
) : Parcelable

@Parcelize
data class EpgData(
    @SerializedName("title") var title: String = "",
    @SerializedName("start") var start: String = "",
    @SerializedName("end") var end: String = "",
    @SerializedName("desc") var desc: String = ""
) : Parcelable

@Parcelize
data class Drm(
    @SerializedName("key") var key: String = "",
    @SerializedName("type") var type: String = "",
    @SerializedName("licenseUrl") var licenseUrl: String = ""
) : Parcelable

@Parcelize
data class ClearKey(
    @SerializedName("kid") var kid: String = "",
    @SerializedName("k") var k: String = ""
) : Parcelable

@Parcelize
data class Sub(
    @SerializedName("name") var name: String = "",
    @SerializedName("url") var url: String = "",
    @SerializedName("lang") var lang: String = "",
    @SerializedName("mimeType") var mimeType: String = ""
) : Parcelable

@Parcelize
data class Rule(
    @SerializedName("name") var name: String = "",
    @SerializedName("type") var type: Int = 0,
    @SerializedName("regex") var regex: String = ""
) : Parcelable

@Parcelize
data class Style(
    @SerializedName("name") var name: String = "",
    @SerializedName("size") var size: Float = 0f,
    @SerializedName("color") var color: Int = 0,
    @SerializedName("shadow") var shadow: Int = 0,
    @SerializedName("position") var position: Int = 0
) : Parcelable

@Parcelize
data class Device(
    @SerializedName("name") var name: String = "",
    @SerializedName("ip") var ip: String = "",
    @SerializedName("port") var port: Int = 0,
    @SerializedName("type") var type: String = "",
    @SerializedName("udn") var udn: String = ""
) : Parcelable

@Parcelize
data class Url(
    @SerializedName("url") var url: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("type") var type: Int = 0
) : Parcelable

@Parcelize
data class Word(
    @SerializedName("word") var word: String = "",
    @SerializedName("time") var time: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class Cate(
    @SerializedName("type_id") var typeId: String = "",
    @SerializedName("type_name") var typeName: String = ""
) : Parcelable

@Parcelize
data class Tv(
    @SerializedName("name") var name: String = "",
    @SerializedName("logo") var logo: String = "",
    @SerializedName("epg") var epg: String = "",
    @SerializedName("urls") var urls: List<String> = emptyList()
) : Parcelable

@Parcelize
data class Core(
    @SerializedName("sites") var sites: List<Site> = emptyList(),
    @SerializedName("lives") var lives: List<Live> = emptyList(),
    @SerializedName("parses") var parses: List<Parse> = emptyList(),
    @SerializedName("flags") var flags: List<String> = emptyList(),
    @SerializedName("ijk") var ijk: Map<String, String> = emptyMap(),
    @SerializedName("doH") var doh: List<Doh> = emptyList(),
    @SerializedName("proxy") var proxyList: List<Proxy> = emptyList(),
    @SerializedName("rules") var rules: List<Rule> = emptyList(),
    @SerializedName("lives") var liveUrls: List<Live> = emptyList()
) : Parcelable

@Parcelize
data class Doh(
    @SerializedName("name") var name: String = "",
    @SerializedName("url") var url: String = ""
) : Parcelable

@Parcelize
data class Proxy(
    @SerializedName("name") var name: String = "",
    @SerializedName("host") var host: String = "",
    @SerializedName("port") var port: Int = 0,
    @SerializedName("user") var user: String = "",
    @SerializedName("pass") var pass: String = ""
) : Parcelable

@Parcelize
data class Header(
    @SerializedName("key") var key: String = "",
    @SerializedName("value") var value: String = ""
) : Parcelable

@Parcelize
data class Depot(
    @SerializedName("name") var name: String = "",
    @SerializedName("type") var type: String = "",
    @SerializedName("url") var url: String = "",
    @SerializedName("ext") var ext: String = ""
) : Parcelable

@Parcelize
data class Catchup(
    @SerializedName("type") var type: String = "",
    @SerializedName("source") var source: String = "",
    @SerializedName("days") var days: String = ""
) : Parcelable

@Parcelize
data class Func(
    @SerializedName("name") var name: String = "",
    @SerializedName("type") var type: Int = 0
) : Parcelable
