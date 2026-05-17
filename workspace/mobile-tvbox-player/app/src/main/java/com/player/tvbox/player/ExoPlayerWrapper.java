package com.player.tvbox.player;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2 TRACK_TYPE_VIDEO;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionOverride;

/**
 * ExoPlayer 播放器包装器
 * 实现 IVideoPlayer 接口，作为 libmpv 的备用方案
 */
public class ExoPlayerWrapper implements IVideoPlayer {
    
    private static final String TAG = "ExoPlayerWrapper";
    
    private Context context;
    private ExoPlayer exoPlayer;
    private IVideoPlayer.PlayerListener listener;
    private Surface surface;
    private float volume = 1.0f;
    
    public ExoPlayerWrapper(Context context) {
        this.context = context;
        initializePlayer();
    }
    
    private void initializePlayer() {
        try {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(context);
            
            exoPlayer = new ExoPlayer.Builder(context)
                    .setTrackSelector(trackSelector)
                    .build();
            
            exoPlayer.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int state) {
                    handlePlaybackState(state);
                }
                
                @Override
                public void onPlayerError(PlaybackException error) {
                    if (listener != null) {
                        listener.onError(error.getMessage());
                    }
                }
            });
            
            Log.i(TAG, "ExoPlayer initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize ExoPlayer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    private void handlePlaybackState(int state) {
        switch (state) {
            case Player.STATE_READY:
                if (listener != null) {
                    listener.onPrepared();
                }
                break;
                
            case Player.STATE_ENDED:
                if (listener != null) {
                    listener.onCompletion();
                }
                break;
                
            case Player.STATE_BUFFERING:
                // 缓冲中
                break;
                
            case Player.STATE_IDLE:
                // 空闲状态
                break;
        }
    }
    
    @Override
    public void setSurface(Surface surface) {
        this.surface = surface;
        if (exoPlayer != null) {
            exoPlayer.setVideoSurface(surface);
        }
    }
    
    @Override
    public void play(String url) {
        if (exoPlayer == null) {
            initializePlayer();
        }
        
        try {
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.play();
            
            Log.i(TAG, "Playing: " + url);
        } catch (Exception e) {
            Log.e(TAG, "Failed to play: " + e.getMessage());
            if (listener != null) {
                listener.onError("播放失败：" + e.getMessage());
            }
        }
    }
    
    @Override
    public void pause() {
        if (exoPlayer != null && exoPlayer.isPlaying()) {
            exoPlayer.pause();
        }
    }
    
    @Override
    public void resume() {
        if (exoPlayer != null && !exoPlayer.isPlaying()) {
            exoPlayer.play();
        }
    }
    
    @Override
    public void stop() {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
    }
    
    @Override
    public void seekTo(long position) {
        if (exoPlayer != null) {
            exoPlayer.seekTo(position);
        }
    }
    
    @Override
    public long getCurrentPosition() {
        return exoPlayer != null ? exoPlayer.getCurrentPosition() : 0;
    }
    
    @Override
    public long getDuration() {
        return exoPlayer != null ? exoPlayer.getDuration() : 0;
    }
    
    @Override
    public boolean isPlaying() {
        return exoPlayer != null && exoPlayer.isPlaying();
    }
    
    @Override
    public void setVolume(float volume) {
        this.volume = volume;
        if (exoPlayer != null) {
            exoPlayer.setVolume(volume);
        }
    }
    
    @Override
    public float getVolume() {
        return volume;
    }
    
    @Override
    public void release() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
            surface = null;
            Log.i(TAG, "ExoPlayer released");
        }
    }
    
    @Override
    public void setPlayerListener(PlayerListener listener) {
        this.listener = listener;
    }
}
