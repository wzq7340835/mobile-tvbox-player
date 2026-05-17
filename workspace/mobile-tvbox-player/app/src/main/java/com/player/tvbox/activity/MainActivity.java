package com.player.tvbox.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.player.tvbox.R;
import com.player.tvbox.adapter.SiteListAdapter;
import com.player.tvbox.db.HistoryManager;
import com.player.tvbox.db.FavoriteManager;
import com.player.tvbox.model.TvBoxSource;
import com.player.tvbox.model.VideoSite;
import com.player.tvbox.parser.TvBoxParser;

/**
 * 主 Activity
 * 自动检测设备类型并显示对应 UI
 */
public class MainActivity extends AppCompatActivity {
    
    private RecyclerView recyclerSites;
    private SiteListAdapter siteAdapter;
    private BottomNavigationView bottomNavigation;
    private MaterialButton btnImportUrl;
    private MaterialButton btnImportFile;
    private MaterialButton btnImportQr;
    private CircularProgressIndicator progressLoading;
    
    private HistoryManager historyManager;
    private FavoriteManager favoriteManager;
    
    private TvBoxSource currentSource;
    
    private boolean isTvDevice;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 检测设备类型
        isTvDevice = getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK);
        
        // 根据设备类型设置布局
        if (isTvDevice) {
            // TV 设备 - TODO: 使用 Leanback 布局
            setContentView(R.layout.activity_main_mobile); // 暂时使用手机布局
        } else {
            // 手机设备
            setContentView(R.layout.activity_main_mobile);
        }
        
        initViews();
        initManagers();
        setupAdapter();
        setupListeners();
    }
    
    private void initViews() {
        recyclerSites = findViewById(R.id.recycler_sites);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        btnImportUrl = findViewById(R.id.btn_import_url);
        btnImportFile = findViewById(R.id.btn_import_file);
        btnImportQr = findViewById(R.id.btn_import_qr);
        progressLoading = findViewById(R.id.progress_loading);
    }
    
    private void initManagers() {
        historyManager = new HistoryManager(this);
        favoriteManager = new FavoriteManager(this);
    }
    
    private void setupAdapter() {
        siteAdapter = new SiteListAdapter();
        recyclerSites.setLayoutManager(new LinearLayoutManager(this));
        recyclerSites.setAdapter(siteAdapter);
    }
    
    private void setupListeners() {
        // 网络导入
        btnImportUrl.setOnClickListener(v -> showImportUrlDialog());
        
        // 本地导入
        btnImportFile.setOnClickListener(v -> pickFileFromStorage());
        
        // 扫码导入
        btnImportQr.setOnClickListener(v -> startQrScan());
        
        // 站点列表点击
        siteAdapter.setOnSiteClickListener(new SiteListAdapter.OnSiteClickListener() {
            @Override
            public void onSiteClick(VideoSite site) {
                // TODO: 显示站点视频分类
                Toast.makeText(MainActivity.this, "点击：" + site.getName(), 
                        Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onSitePlay(VideoSite site) {
                // TODO: 播放站点内容
                Toast.makeText(MainActivity.this, "播放：" + site.getName(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        // 底部导航
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // 首页
                return true;
            } else if (itemId == R.id.nav_history) {
                // 历史记录
                startActivity(new Intent(this, HistoryActivity.class));
                return true;
            } else if (itemId == R.id.nav_favorite) {
                // 收藏
                startActivity(new Intent(this, FavoriteActivity.class));
                return true;
            } else if (itemId == R.id.nav_settings) {
                // 设置
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }
    
    /**
     * 显示导入 URL 对话框
     */
    private void showImportUrlDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_import_url, 
                this, false);
        TextInputEditText editText = dialogView.findViewById(R.id.et_url);
        
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.import_from_url)
                .setView(dialogView)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    String url = editText.getText().toString().trim();
                    if (!url.isEmpty()) {
                        loadTvBoxSource(url);
                    } else {
                        Toast.makeText(this, R.string.invalid_url, 
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
    
    /**
     * 从文件管理器选择文件
     */
    private void pickFileFromStorage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/json");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_IMPORT_FILE);
    }
    
    /**
     * 启动二维码扫描
     */
    private void startQrScan() {
        Intent intent = new Intent(this, QrScanActivity.class);
        startActivityForResult(intent, REQUEST_QR_SCAN);
    }
    
    /**
     * 加载 TVBox 源
     */
    private void loadTvBoxSource(String url) {
        showLoading(true);
        
        TvBoxParser.parseFromUrl(url, new TvBoxParser.ParseCallback() {
            @Override
            public void onSuccess(TvBoxSource source) {
                showLoading(false);
                currentSource = source;
                siteAdapter.setSites(source.getSites());
                
                Toast.makeText(MainActivity.this, 
                        getString(R.string.import_success) + " (" + source.getSites().size() + " 站点)",
                        Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(MainActivity.this, 
                        getString(R.string.import_failed) + ": " + error,
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * 从文件加载 TVBox 源
     */
    private void loadTvBoxFromFile(android.net.Uri uri) {
        showLoading(true);
        
        try {
            java.io.File file = new java.io.File(uri.getPath());
            TvBoxParser.parseFromFile(file, new TvBoxParser.ParseCallback() {
                @Override
                public void onSuccess(TvBoxSource source) {
                    showLoading(false);
                    currentSource = source;
                    siteAdapter.setSites(source.getSites());
                    
                    Toast.makeText(MainActivity.this, 
                            getString(R.string.import_success) + " (" + source.getSites().size() + " 站点)",
                            Toast.LENGTH_SHORT).show();
                }
                
                @Override
                public void onError(String error) {
                    showLoading(false);
                    Toast.makeText(MainActivity.this, 
                            getString(R.string.import_failed) + ": " + error,
                            Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            showLoading(false);
            Toast.makeText(this, R.string.import_failed + ": " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 显示/隐藏加载指示器
     */
    private void showLoading(boolean show) {
        runOnUiThread(() -> {
            progressLoading.setVisibility(show ? View.VISIBLE : View.GONE);
            recyclerSites.setVisibility(show ? View.GONE : View.VISIBLE);
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_IMPORT_FILE) {
                // 文件选择
                loadTvBoxFromFile(data.getData());
            } else if (requestCode == REQUEST_QR_SCAN) {
                // 二维码扫描结果
                String url = data.getStringExtra("url");
                if (url != null && !url.isEmpty()) {
                    loadTvBoxSource(url);
                }
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (historyManager != null) {
            historyManager.close();
        }
        if (favoriteManager != null) {
            favoriteManager.close();
        }
    }
    
    // 请求码常量
    private static final int REQUEST_IMPORT_FILE = 1001;
    private static final int REQUEST_QR_SCAN = 1002;
}
