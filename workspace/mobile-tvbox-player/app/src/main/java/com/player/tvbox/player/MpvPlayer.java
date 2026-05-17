package com.player.tvbox.player;

import android.content.Context;
import android.util.Log;
import android.view.Surface;

/**
 * libmpv-android 播放器封装
 * 通过 JNI 调用 libmpv 库实现视频播放
 */
public class MpvPlayer implements IVideoPlayer {
    
    private static final String TAG = "MpvPlayer";
    
    static {
        try {
            System.loadLibrary("mpv");
            Log.i(TAG, "libmpv loaded successfully");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Failed to load libmpv: " + e.getMessage());
            throw new RuntimeException("libmpv library not found", e);
        }
    }
    
    private Context context;
    private long mpvHandle;
    private IVideoPlayer.PlayerListener listener;
    private Surface surface;
    private float volume = 1.0f;
    private boolean isPrepared = false;
    
    public MpvPlayer(Context context) {
        this.context = context;
        initialize();
    }
    
    /**
     * 初始化 MPV 播放器
     */
    private void initialize() {
        mpvHandle = mpvCreate();
        if (mpvHandle == 0) {
            throw new RuntimeException("Failed to create mpv handle");
        }
        
        // 设置选项
        mpvSetOptionString(mpvHandle, "vo", "android-surface");
        mpvSetOptionString(mpvHandle, "hwdec", "mediacodec");
        mpvSetOptionString(mpvHandle, "cache", "yes");
        mpvSetOptionString(mpvHandle, "demuxer-max-bytes", "104857600"); // 100MB
        mpvSetOptionString(mpvHandle, "demuxer-max-back-bytes", "52428800"); // 50MB
        
        // 初始化
        int ret = mpvInitialize(mpvHandle);
        if (ret < 0) {
            throw new RuntimeException("Failed to initialize mpv: " + ret);
        }
        
        isPrepared = true;
        Log.i(TAG, "MPV player initialized");
    }
    
    // ============= Native Methods =============
    
    private native long mpvCreate();
    
    private native int mpvInitialize(long handle);
    
    private native void mpvSetOptionString(long handle, String name, String value);
    
    private native int mpvCommand(long handle, String command);
    
    private native void mpvSetPropertyString(long handle, String name, String value);
    
    private native String mpvGetPropertyString(long handle, String name);
    
    private native long mpvGetPropertyLong(long handle, String name);
    
    private native double mpvGetPropertyDouble(long handle, String name);
    
    private native void mpvObserveProperty(long handle, String name, int format);
    
    private native void mpvDestroy(long handle);
    
    // ==========================================
    
    @Override
    public void setSurface(Surface surface) {
        this.surface = surface;
        if (mpvHandle != 0 && surface != null) {
            mpvSetPropertyString(mpvHandle, "android-surface-surface", String.valueOf(surface));
        }
    }
    
    @Override
    public void play(String url) {
        if (!isPrepared) {
            initialize();
        }
        
        Log.i(TAG, "Playing: " + url);
        
        // 使用 loadfile 命令播放视频
        String command = "loadfile \"" + url + "\"";
        int ret = mpvCommand(mpvHandle, command);
        
        if (ret < 0) {
            Log.e(TAG, "Failed to play: " + ret);
            if (listener != null) {
                listener.onError("播放失败：" + ret);
            }
        }
    }
    
    @Override
    public void pause() {
        mpvSetPropertyString(mpvHandle, "pause", "yes");
    }
    
    @Override
    public void resume() {
        mpvSetPropertyString(mpvHandle, "pause", "no");
    }
    
    @Override
    public void stop() {
        mpvCommand(mpvHandle, "stop");
    }
    
    @Override
    public void seekTo(long position) {
        double seconds = position / 1000.0;
        mpvSetPropertyString(mpvHandle, "time-pos", String.valueOf(seconds));
    }
    
    @Override
    public long getCurrentPosition() {
        if (mpvHandle == 0) return 0;
        double pos = mpvGetPropertyDouble(mpvHandle, "time-pos");
        return (long) (pos * 1000);
    }
    
    @Override
    public long getDuration() {
        if (mpvHandle == 0) return 0;
        double duration = mpvGetPropertyDouble(mpvHandle, "duration");
        return (long) (duration * 1000);
    }
    
    @Override
    public boolean isPlaying() {
        if (mpvHandle == 0) return false;
        String pause = mpvGetPropertyString(mpvHandle, "pause");
        return pause == null || !pause.equals("yes");
    }
    
    @Override
    public void setVolume(float volume) {
        this.volume = volume;
        if (mpvHandle != 0) {
            int volPercent = (int) (volume * 100);
            mpvSetPropertyString(mpvHandle, "volume", String.valueOf(volPercent));
        }
    }
    
    @Override
    public float getVolume() {
        return volume;
    }
    
    @Override
    public void release() {
        if (mpvHandle != 0) {
            mpvCommand(mpvHandle, "stop");
            mpvDestroy(mpvHandle);
            mpvHandle = 0;
            surface = null;
            Log.i(TAG, "MPV player released");
        }
    }
    
    @Override
    public void setPlayerListener(PlayerListener listener) {
        this.listener = listener;
        // TODO: 设置属性观察器，监听播放状态变化
    }
    
    /**
     * 获取 MPV 句柄（用于高级操作）
     */
    public long getMpvHandle() {
        return mpvHandle;
    }
}
