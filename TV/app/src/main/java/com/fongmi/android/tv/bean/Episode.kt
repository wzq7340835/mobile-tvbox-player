package com.fongmi.android.tv.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episode(
    @SerializedName("name") var name: String = "",
    @SerializedName("url") var url: String = "",
    var selected: Boolean = false
) : Parcelable

@Parcelize
data class Flag(
    @SerializedName("flag") var name: String = "",
    @SerializedName("urls") var episodes: List<Episode> = emptyList(),
    var selected: Boolean = false
) : Parcelable
