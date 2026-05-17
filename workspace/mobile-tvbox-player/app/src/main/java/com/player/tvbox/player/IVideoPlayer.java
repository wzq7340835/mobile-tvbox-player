package com.player.tvbox.player;

import android.view.Surface;

/**
 * 视频播放器统一接口
 * 所有播放器实现必须遵循此接口
 */
public interface IVideoPlayer {
    
    /**
     * 设置显示 Surface
     * @param surface 显示 Surface
     */
    void setSurface(Surface surface);
    
    /**
     * 播放视频
     * @param url 视频 URL
     */
    void play(String url);
    
    /**
     * 暂停播放
     */
    void pause();
    
    /**
     * 继续播放
     */
    void resume();
    
    /**
     * 停止播放
     */
    void stop();
    
    /**
     * 跳转到指定位置
     * @param position 位置（毫秒）
     */
    void seekTo(long position);
    
    /**
     * 获取当前位置
     * @return 当前位置（毫秒）
     */
    long getCurrentPosition();
    
    /**
     * 获取视频总时长
     * @return 总时长（毫秒）
     */
    long getDuration();
    
    /**
     * 是否正在播放
     * @return true=播放中，false=未播放
     */
    boolean isPlaying();
    
    /**
     * 设置音量
     * @param volume 音量值 (0.0 - 1.0)
     */
    void setVolume(float volume);
    
    /**
     * 获取音量
     * @return 音量值
     */
    float getVolume();
    
    /**
     * 释放播放器资源
     */
    void release();
    
    /**
     * 设置播放状态监听器
     * @param listener 监听器
     */
    void setPlayerListener(PlayerListener listener);
    
    /**
     * 播放器监听器接口
     */
    interface PlayerListener {
        /**
         * 播放器准备就绪
         */
        void onPrepared();
        
        /**
         * 播放完成
         */
        void onCompletion();
        
        /**
         * 播放错误
         * @param error 错误信息
         */
        void onError(String error);
        
        /**
         * 缓冲进度更新
         * @param percent 缓冲百分比 (0-100)
         */
        void onBufferingUpdate(int percent);
        
        /**
         * 视频尺寸变化
         * @param width 宽度
         * @param height 高度
         */
        void onVideoSizeChanged(int width, int height);
    }
}
