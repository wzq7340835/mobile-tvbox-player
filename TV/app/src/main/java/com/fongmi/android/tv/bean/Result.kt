package com.fongmi.android.tv.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    @SerializedName("list") var list: List<Vod> = emptyList(),
    @SerializedName("filters") var filters: Map<String, List<Filter>> = emptyMap(),
    @SerializedName("header") var header: Map<String, String> = emptyMap(),
    @SerializedName("url") var url: String = "",
    @SerializedName("parse") var parse: Int = 0,
    @SerializedName("jxFrom") var jxFrom: String = "",
    @SerializedName("msg") var msg: String = "",
    @SerializedName("flag") var flag: String = ""
) : Parcelable {

    fun isEmpty(): Boolean = list.isEmpty() && url.isBlank()
    fun hasUrl(): Boolean = url.isNotBlank()
    fun hasParse(): Boolean = parse == 1
    fun hasVod(): Boolean = list.isNotEmpty()
}

@Parcelize
data class Filter(
    @SerializedName("key") var key: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("value") var value: Value = Value()
) : Parcelable

@Parcelize
data class Value(
    @SerializedName("key") var key: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("v") var v: String = ""
) : Parcelable
