package com.player.tvbox.player;

import android.content.Context;
import android.util.Log;

/**
 * 播放器管理器
 * 统一管理 MPV 和 ExoPlayer 播放器，支持自动切换
 */
public class PlayerManager {
    
    private static final String TAG = "PlayerManager";
    
    private static PlayerManager instance;
    private IVideoPlayer currentPlayer;
    private Context context;
    private PlayerType currentType;
    
    /**
     * 播放器类型枚举
     */
    public enum PlayerType {
        MPV,      // libmpv-android
        EXO       // ExoPlayer
    }
    
    private PlayerManager(Context ctx) {
        this.context = ctx.getApplicationContext();
        this.currentType = PlayerType.MPV;
        // 延迟初始化，等待第一次播放请求
    }
    
    /**
     * 获取单例实例
     * @param ctx Context
     * @return PlayerManager 实例
     */
    public static synchronized PlayerManager getInstance(Context ctx) {
        if (instance == null) {
            instance = new PlayerManager(ctx);
        }
        return instance;
    }
    
    /**
     * 初始化指定类型的播放器
     * @param type 播放器类型
     */
    public void initPlayer(PlayerType type) {
        // 释放当前播放器
        if (currentPlayer != null) {
            currentPlayer.release();
            currentPlayer = null;
        }
        
        try {
            if (type == PlayerType.MPV) {
                currentPlayer = new MpvPlayer(context);
                currentType = PlayerType.MPV;
                Log.i(TAG, "MPV player initialized");
            } else {
                currentPlayer = new ExoPlayerWrapper(context);
                currentType = PlayerType.EXO;
                Log.i(TAG, "ExoPlayer initialized");
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize " + type + " player: " + e.getMessage());
            // 如果 MPV 失败，降级到 ExoPlayer
            if (type == PlayerType.MPV) {
                Log.w(TAG, "Falling back to ExoPlayer");
                currentPlayer = new ExoPlayerWrapper(context);
                currentType = PlayerType.EXO;
            } else {
                throw e;
            }
        }
    }
    
    /**
     * 获取当前播放器
     * @return IVideoPlayer 实例
     */
    public IVideoPlayer getPlayer() {
        if (currentPlayer == null) {
            initPlayer(currentType);
        }
        return currentPlayer;
    }
    
    /**
     * 获取当前播放器类型
     * @return 播放器类型
     */
    public PlayerType getCurrentPlayerType() {
        return currentType;
    }
    
    /**
     * 切换到另一种播放器
     */
    public void switchPlayer() {
        PlayerType newType = (currentType == PlayerType.MPV) ? PlayerType.EXO : PlayerType.MPV;
        Log.i(TAG, "Switching player from " + currentType + " to " + newType);
        initPlayer(newType);
    }
    
    /**
     * 重置管理器（用于 Activity 销毁）
     */
    public void reset() {
        if (currentPlayer != null) {
            currentPlayer.release();
            currentPlayer = null;
        }
        instance = null;
    }
}
