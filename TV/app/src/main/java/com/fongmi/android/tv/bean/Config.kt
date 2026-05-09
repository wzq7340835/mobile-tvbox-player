package com.fongmi.android.tv.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Config(
    @SerializedName("url") var url: String = "",
    @SerializedName("type") var type: Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("active") var active: Boolean = false,
    @SerializedName("home") var home: Boolean = false,
    @SerializedName("live") var live: Boolean = false,
    @SerializedName("epg") var epg: String = "",
    @SerializedName("doh") var doh: String = "",
    @SerializedName("proxy") var proxy: String = "",
    @SerializedName("header") var header: String = "",
    var uid: Long = 0
) : Parcelable {

    fun isVod(): Boolean = type == TYPE_VOD
    fun isLive(): Boolean = type == TYPE_LIVE
    fun isEpg(): Boolean = type == TYPE_EPG

    companion object {
        const val TYPE_VOD = 0
        const val TYPE_LIVE = 1
        const val TYPE_EPG = 2

        fun vod(url: String): Config = Config(url = url, type = TYPE_VOD, active = true)
        fun live(url: String): Config = Config(url = url, type = TYPE_LIVE, active = true)
        fun epg(url: String): Config = Config(url = url, type = TYPE_EPG, active = true)
    }
}
