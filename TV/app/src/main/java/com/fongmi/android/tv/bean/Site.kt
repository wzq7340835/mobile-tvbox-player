package com.fongmi.android.tv.bean

import android.os.Parcelable
import com.fongmi.android.tv.engine.Spider
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Site(
    @SerializedName("key") var key: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("type") var type: Int = 0,
    @SerializedName("api") var api: String = "",
    @SerializedName("ext") var ext: String = "",
    @SerializedName("jar") var jar: String = "",
    @SerializedName("playerType") var playerType: Int = 0,
    @SerializedName("searchable") var searchable: Int = 1,
    @SerializedName("quickSearch") var quickSearch: Int = 1,
    @SerializedName("filterable") var filterable: Int = 1,
    @SerializedName("changeable") var changeable: Int = 1,
    @SerializedName("timeout") var timeout: Int = 0,
    @SerializedName("categories") var categories: String = "",
    @SerializedName("group") var group: String = "",
    @SerializedName("live") var live: String = "",
    var index: Int = 0
) : Parcelable, Comparable<Site> {

    @Transient
    var spider: Spider? = null

    fun isJs(): Boolean = type == TYPE_JS
    fun isJar(): Boolean = type == TYPE_JAR
    fun isXpath(): Boolean = type == TYPE_XPATH
    fun isCsp(): Boolean = type == TYPE_CSP
    fun isPy(): Boolean = type == TYPE_PY

    fun canSearch(): Boolean = searchable == 1
    fun canQuickSearch(): Boolean = quickSearch == 1
    fun canFilter(): Boolean = filterable == 1
    fun canChange(): Boolean = changeable == 1

    override fun compareTo(other: Site): Int = index.compareTo(other.index)

    companion object {
        const val TYPE_JS = 3
        const val TYPE_JAR = 1
        const val TYPE_XPATH = 0
        const val TYPE_CSP = 3
        const val TYPE_PY = 4
    }
}
