package com.fongmi.android.tv

import android.content.Context
import androidx.preference.PreferenceManager

object Setting {

    private fun getPrefs(): android.content.SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(App.get())
    }

    var configUrl: String
        get() = getPrefs().getString(Constant.CONFIG_URL, "") ?: ""
        set(value) = getPrefs().edit().putString(Constant.CONFIG_URL, value).apply()

    var configLive: String
        get() = getPrefs().getString(Constant.CONFIG_LIVE, "") ?: ""
        set(value) = getPrefs().edit().putString(Constant.CONFIG_LIVE, value).apply()

    var configEpg: String
        get() = getPrefs().getString(Constant.CONFIG_EPG, "") ?: ""
        set(value) = getPrefs().edit().putString(Constant.CONFIG_EPG, value).apply()

    var configDoh: String
        get() = getPrefs().getString(Constant.CONFIG_DOH, "") ?: ""
        set(value) = getPrefs().edit().putString(Constant.CONFIG_DOH, value).apply()

    var configProxy: String
        get() = getPrefs().getString(Constant.CONFIG_PROXY, "") ?: ""
        set(value) = getPrefs().edit().putString(Constant.CONFIG_PROXY, value).apply()

    var bootLive: Boolean
        get() = getPrefs().getBoolean("boot_live", false)
        set(value) = getPrefs().edit().putBoolean("boot_live", value).apply()

    var bootPass: Boolean
        get() = getPrefs().getBoolean("boot_pass", false)
        set(value) = getPrefs().edit().putBoolean("boot_pass", value).apply()

    var debugMode: Boolean
        get() = getPrefs().getBoolean("debug_mode", false)
        set(value) = getPrefs().edit().putBoolean("debug_mode", value).apply()

    var danmakuOn: Boolean
        get() = getPrefs().getBoolean("danmaku_on", true)
        set(value) = getPrefs().edit().putBoolean("danmaku_on", value).apply()

    var subtitleOn: Boolean
        get() = getPrefs().getBoolean("subtitle_on", true)
        set(value) = getPrefs().edit().putBoolean("subtitle_on", value).apply()

    var playerScale: String
        get() = getPrefs().getString("player_scale", "0") ?: "0"
        set(value) = getPrefs().edit().putString("player_scale", value).apply()

    var playerDecode: String
        get() = getPrefs().getString("player_decode", "0") ?: "0"
        set(value) = getPrefs().edit().putString("player_decode", value).apply()

    var playerRender: String
        get() = getPrefs().getString("player_render", "0") ?: "0"
        set(value) = getPrefs().edit().putString("player_render", value).apply()

    var liveScale: String
        get() = getPrefs().getString("live_scale", "0") ?: "0"
        set(value) = getPrefs().edit().putString("live_scale", value).apply()

    var liveDecode: String
        get() = getPrefs().getString("live_decode", "0") ?: "0"
        set(value) = getPrefs().edit().putString("live_decode", value).apply()

    var liveRender: String
        get() = getPrefs().getString("live_render", "0") ?: "0"
        set(value) = getPrefs().edit().putString("live_render", value).apply()

    var liveEpg: Boolean
        get() = getPrefs().getBoolean("live_epg", true)
        set(value) = getPrefs().edit().putBoolean("live_epg", value).apply()

    var liveNumber: Boolean
        get() = getPrefs().getBoolean("live_number", false)
        set(value) = getPrefs().edit().putBoolean("live_number", value).apply()

    var liveTime: Boolean
        get() = getPrefs().getBoolean("live_time", true)
        set(value) = getPrefs().edit().putBoolean("live_time", value).apply()

    var liveSpeed: Boolean
        get() = getPrefs().getBoolean("live_speed", false)
        set(value) = getPrefs().edit().putBoolean("live_speed", value).apply()

    var searchKeyword: Boolean
        get() = getPrefs().getBoolean("search_keyword", true)
        set(value) = getPrefs().edit().putBoolean("search_keyword", value).apply()

    var homeRecall: Boolean
        get() = getPrefs().getBoolean("home_recall", false)
        set(value) = getPrefs().edit().putBoolean("home_recall", value).apply()

    var homeVod: Boolean
        get() = getPrefs().getBoolean("home_vod", true)
        set(value) = getPrefs().edit().putBoolean("home_vod", value).apply()

    var homeSite: Boolean
        get() = getPrefs().getBoolean("home_site", true)
        set(value) = getPrefs().edit().putBoolean("home_site", value).apply()

    var homeKeep: Boolean
        get() = getPrefs().getBoolean("home_keep", true)
        set(value) = getPrefs().edit().putBoolean("home_keep", value).apply()

    var homeHistory: Boolean
        get() = getPrefs().getBoolean("home_history", true)
        set(value) = getPrefs().edit().putBoolean("home_history", value).apply()

    var syncUpload: Boolean
        get() = getPrefs().getBoolean("sync_upload", false)
        set(value) = getPrefs().edit().putBoolean("sync_upload", value).apply()

    var syncDownload: Boolean
        get() = getPrefs().getBoolean("sync_download", false)
        set(value) = getPrefs().edit().putBoolean("sync_download", value).apply()

    fun clear() {
        getPrefs().edit().clear().apply()
    }
}
