package com.fongmi.android.tv.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.fongmi.android.tv.Setting
import com.fongmi.android.tv.bean.Episode
import com.fongmi.android.tv.bean.Sub
import com.fongmi.android.tv.bean.Track

object PlayerHelper {

    fun getUserAgent(): String {
        return "Mozilla/5.0 (Linux; Android 12) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    }

    fun getSubtitleType(url: String): Int {
        return when {
            url.endsWith(".srt") || url.endsWith(".srt?") -> MimeTypes.TEXT_SUBRIP
            url.endsWith(".ass") || url.endsWith(".ssa") -> MimeTypes.TEXT_SSA
            url.endsWith(".vtt") || url.endsWith(".vtt?") -> MimeTypes.TEXT_VTT
            url.endsWith(".ttml") || url.endsWith(".dfxp") -> MimeTypes.APPLICATION_TTML
            else -> MimeTypes.TEXT_SUBRIP
        }
    }

    fun getShareText(title: String, url: String): String {
        return "$title\n$url"
    }

    fun createPlayer(context: Context): ExoPlayer {
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                50000,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
            )
            .build()

        return ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .build()
    }

    fun buildMediaItem(url: String, headers: Map<String, String> = emptyMap(), subtitles: List<Sub> = emptyList()): MediaItem {
        val builder = MediaItem.Builder()
            .setUri(Uri.parse(url))

        if (subtitles.isNotEmpty()) {
            val subtitleMediaItems = subtitles.map { sub ->
                MediaItem.SubtitleConfiguration.Builder(Uri.parse(sub.url))
                    .setMimeType(getSubtitleType(sub.url))
                    .setLanguage(sub.lang)
                    .setLabel(sub.name)
                    .build()
            }
            builder.setSubtitleConfigurations(subtitleMediaItems)
        }

        return builder.build()
    }
}

class PlayerManager {

    private var player: ExoPlayer? = null
    private var currentUrl: String = ""
    private var currentHeaders: Map<String, String> = emptyMap()
    private var speed: Float = 1.0f

    fun init(context: Context) {
        release()
        player = PlayerHelper.createPlayer(context)
    }

    fun release() {
        player?.release()
        player = null
    }

    fun play(url: String, headers: Map<String, String> = emptyMap(), position: Long = 0) {
        currentUrl = url
        currentHeaders = headers
        val mediaItem = PlayerHelper.buildMediaItem(url, headers)
        player?.apply {
            setMediaItem(mediaItem)
            prepare()
            if (position > 0) seekTo(position)
            playWhenReady = true
        }
    }

    fun stop() {
        player?.stop()
    }

    fun pause() {
        player?.pause()
    }

    fun resume() {
        player?.play()
    }

    fun seekTo(position: Long) {
        player?.seekTo(position)
    }

    fun getPosition(): Long = player?.currentPosition ?: 0
    fun getDuration(): Long = player?.duration ?: 0
    fun getBufferedPosition(): Long = player?.bufferedPosition ?: 0
    fun isPlaying(): Boolean = player?.isPlaying == true

    fun setSpeed(speed: Float) {
        this.speed = speed
        player?.playbackParameters = PlaybackParameters(speed)
    }

    fun getSpeed(): Float = speed

    fun getPlayer(): ExoPlayer? = player

    fun setVolume(volume: Float) {
        player?.volume = volume.coerceIn(0f, 1f)
    }

    fun getVolume(): Float = player?.volume ?: 1f

    fun getVideoWidth(): Int = player?.videoSize?.width ?: 0
    fun getVideoHeight(): Int = player?.videoSize?.height ?: 0
}
