package com.player.tvbox.activity;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.player.tvbox.R;
import com.player.tvbox.db.HistoryManager;
import com.player.tvbox.player.IVideoPlayer;
import com.player.tvbox.player.PlayerManager;

/**
 * 视频播放 Activity
 * 全屏播放器界面
 */
public class PlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    
    private SurfaceView surfacePlayer;
    private ProgressBar progressLoading;
    private View layoutError;
    private TextView tvError;
    private Button btnRetry;
    private View layoutControls;
    private SeekBar seekbarProgress;
    private ImageButton btnPlayPause;
    private TextView tvPosition;
    private TextView tvDuration;
    
    private PlayerManager playerManager;
    private HistoryManager historyManager;
    
    private String videoUrl;
    private String videoTitle;
    private String siteName;
    
    private boolean isPlaying = false;
    private long lastPosition = 0;
    
    // 更新进度计时器
    private android.os.Handler handler = new android.os.Handler();
    private Runnable updateProgressRunnable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        
        // 获取参数
        videoUrl = getIntent().getStringExtra("url");
        videoTitle = getIntent().getStringExtra("title");
        siteName = getIntent().getStringExtra("siteName");
        lastPosition = getIntent().getLongExtra("position", 0);
        
        if (videoUrl == null || videoUrl.isEmpty()) {
            Toast.makeText(this, "无效的视频 URL", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initViews();
        initManagers();
        setupSurface();
        setupListeners();
        startUpdateProgress();
    }
    
    private void initViews() {
        surfacePlayer = findViewById(R.id.surface_player);
        progressLoading = findViewById(R.id.progress_loading);
        layoutError = findViewById(R.id.layout_error);
        tvError = findViewById(R.id.tv_error);
        btnRetry = findViewById(R.id.btn_retry);
        layoutControls = findViewById(R.id.layout_controls);
        seekbarProgress = findViewById(R.id.seekbar_progress);
        btnPlayPause = findViewById(R.id.btn_play_pause);
        tvPosition = findViewById(R.id.tv_position);
        tvDuration = findViewById(R.id.tv_duration);
    }
    
    private void initManagers() {
        playerManager = PlayerManager.getInstance(this);
        historyManager = new HistoryManager(this);
    }
    
    private void setupSurface() {
        surfacePlayer.getHolder().addCallback(this);
    }
    
    private void setupListeners() {
        // 重试按钮
        btnRetry.setOnClickListener(v -> {
            loadVideo();
        });
        
        // 播放/暂停按钮
        btnPlayPause.setOnClickListener(v -> {
            togglePlayPause();
        });
        
        // 进度条拖动
        seekbarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // 用户拖动
                    IVideoPlayer player = playerManager.getPlayer();
                    long duration = player.getDuration();
                    long newPosition = (progress * duration) / 100;
                    player.seekTo(newPosition);
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 开始拖动
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 停止拖动
            }
        });
    }
    
    private void togglePlayPause() {
        IVideoPlayer player = playerManager.getPlayer();
        if (player.isPlaying()) {
            player.pause();
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            isPlaying = false;
        } else {
            player.resume();
            btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            isPlaying = true;
        }
    }
    
    private void loadVideo() {
        // 显示加载指示器
        progressLoading.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
        
        IVideoPlayer player = playerManager.getPlayer();
        player.setSurface(surfacePlayer.getHolder().getSurface());
        
        // 设置播放器监听器
        player.setPlayerListener(new IVideoPlayer.PlayerListener() {
            @Override
            public void onPrepared() {
                runOnUiThread(() -> {
                    progressLoading.setVisibility(View.GONE);
                    layoutControls.setVisibility(View.VISIBLE);
                    
                    // 跳转到上次位置
                    if (lastPosition > 0) {
                        player.seekTo(lastPosition);
                        Toast.makeText(PlayerActivity.this, 
                                "从上次位置继续播放", Toast.LENGTH_SHORT).show();
                    }
                    
                    player.resume();
                    isPlaying = true;
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                });
            }
            
            @Override
            public void onCompletion() {
                runOnUiThread(() -> {
                    Toast.makeText(PlayerActivity.this, 
                            R.string.video_end, Toast.LENGTH_SHORT).show();
                    
                    // 记录完整观看历史
                    saveHistory();
                    finish();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    progressLoading.setVisibility(View.GONE);
                    layoutError.setVisibility(View.VISIBLE);
                    tvError.setText("播放失败：" + error);
                });
            }
            
            @Override
            public void onBufferingUpdate(int percent) {
                // 缓冲更新
            }
            
            @Override
            public void onVideoSizeChanged(int width, int height) {
                // 视频尺寸变化
            }
        });
        
        // 开始播放
        player.play(videoUrl);
    }
    
    private void startUpdateProgress() {
        updateProgressRunnable = new Runnable() {
            @Override
            public void run() {
                updateProgress();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updateProgressRunnable);
    }
    
    private void updateProgress() {
        IVideoPlayer player = playerManager.getPlayer();
        if (player == null || !player.isPlaying()) {
            return;
        }
        
        long position = player.getCurrentPosition();
        long duration = player.getDuration();
        
        if (duration > 0) {
            int progress = (int) ((position * 100) / duration);
            seekbarProgress.setProgress(progress);
            
            tvPosition.setText(formatTime(position));
            tvDuration.setText(formatTime(duration));
            
            // 保存进度到历史记录
            historyManager.updateProgress(videoUrl, position, duration);
        }
    }
    
    private void saveHistory() {
        IVideoPlayer player = playerManager.getPlayer();
        long position = player.getCurrentPosition();
        long duration = player.getDuration();
        
        historyManager.addHistory(videoTitle, videoUrl, siteName, position, duration);
    }
    
    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
        } else {
            return String.format("%02d:%02d", minutes, seconds % 60);
        }
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Surface 创建完成，开始加载视频
        loadVideo();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Surface 变化
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface 销毁，释放播放器
        if (playerManager != null) {
            playerManager.getPlayer().release();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // 暂停播放
        if (playerManager != null && playerManager.getPlayer() != null) {
            playerManager.getPlayer().pause();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // 继续播放
        if (playerManager != null && playerManager.getPlayer() != null) {
            playerManager.getPlayer().resume();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // 停止更新进度
        if (handler != null && updateProgressRunnable != null) {
            handler.removeCallbacks(updateProgressRunnable);
        }
        
        // 保存最后的进度
        saveHistory();
        
        // 关闭数据库
        if (historyManager != null) {
            historyManager.close();
        }
    }
    
    @Override
    public void onBackPressed() {
        // 保存进度并退出
        saveHistory();
        super.onBackPressed();
    }
}
