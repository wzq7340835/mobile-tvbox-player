package com.fongmi.android.tv.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vod(
    @SerializedName("vod_id") var vodId: String = "",
    @SerializedName("vod_name") var vodName: String = "",
    @SerializedName("vod_pic") var vodPic: String = "",
    @SerializedName("vod_remarks") var vodRemarks: String = "",
    @SerializedName("vod_year") var vodYear: String = "",
    @SerializedName("vod_area") var vodArea: String = "",
    @SerializedName("vod_director") var vodDirector: String = "",
    @SerializedName("vod_actor") var vodActor: String = "",
    @SerializedName("vod_content") var vodContent: String = "",
    @SerializedName("vod_play_from") var vodPlayFrom: String = "",
    @SerializedName("vod_play_url") var vodPlayUrl: String = "",
    @SerializedName("type_name") var typeName: String = "",
    @SerializedName("type_id") var typeId: String = "",
    @SerializedName("source_key") var sourceKey: String = "",
    @SerializedName("source_name") var sourceName: String = ""
) : Parcelable {

    fun getFlags(): List<Flag> {
        if (vodPlayFrom.isBlank()) return emptyList()
        val fromList = vodPlayFrom.split("$$$")
        val urlList = vodPlayUrl.split("$$$")
        return fromList.mapIndexed { index, from ->
            val episodes = if (index < urlList.size) {
                urlList[index].split("#").mapNotNull { ep ->
                    val parts = ep.split("$")
                    if (parts.size >= 2) Episode(name = parts[0], url = parts[1])
                    else null
                }
            } else emptyList()
            Flag(name = from, episodes = episodes)
        }
    }

    companion object {
        fun empty(): Vod = Vod()
    }
}
