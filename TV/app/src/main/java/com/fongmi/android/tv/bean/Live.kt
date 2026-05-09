package com.fongmi.android.tv.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Live(
    @SerializedName("name") var name: String = "",
    @SerializedName("url") var url: String = "",
    @SerializedName("type") var type: Int = 0,
    @SerializedName("epg") var epg: String = "",
    @SerializedName("logo") var logo: String = "",
    @SerializedName("group") var group: String = "",
    var index: Int = 0,
    var groups: List<Group> = emptyList()
) : Parcelable {

    fun isXml(): Boolean = type == TYPE_XML
    fun isText(): Boolean = type == TYPE_TEXT

    companion object {
        const val TYPE_XML = 0
        const val TYPE_TEXT = 1

        fun xml(url: String): Live = Live(url = url, type = TYPE_XML)
        fun text(url: String): Live = Live(url = url, type = TYPE_TEXT)
    }
}

@Parcelize
data class Group(
    @SerializedName("name") var name: String = "",
    @SerializedName("channels") var channels: List<Channel> = emptyList(),
    var selected: Boolean = false,
    var index: Int = 0
) : Parcelable

@Parcelize
data class Channel(
    @SerializedName("name") var name: String = "",
    @SerializedName("urls") var urls: List<String> = emptyList(),
    @SerializedName("logo") var logo: String = "",
    @SerializedName("epg") var epg: String = "",
    var number: Int = 0,
    var currentUrl: Int = 0,
    var selected: Boolean = false
) : Parcelable {

    fun getUrl(): String = if (urls.isNotEmpty()) urls[currentUrl.coerceIn(urls.indices)] else ""
    fun nextUrl(): Boolean {
        if (urls.size <= 1) return false
        currentUrl = (currentUrl + 1) % urls.size
        return true
    }
}
