package com.player.tvbox.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.player.tvbox.R;
import com.player.tvbox.adapter.HistoryListAdapter;
import com.player.tvbox.db.FavoriteManager;
import com.player.tvbox.model.FavoriteItem;

import java.util.List;

/**
 * 收藏 Activity
 */
public class FavoriteActivity extends AppCompatActivity {
    
    private RecyclerView recyclerFavorite;
    private HistoryListAdapter favoriteAdapter;
    private FavoriteManager favoriteManager;
    private FloatingActionButton fabClear;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        
        favoriteManager = new FavoriteManager(this);
        
        initViews();
        setupAdapter();
        setupListeners();
        loadFavorites();
    }
    
    private void initViews() {
        recyclerFavorite = findViewById(R.id.recycler_favorite);
        fabClear = findViewById(R.id.fab_clear);
    }
    
    private void setupAdapter() {
        // 复用历史记录适配器（布局相似）
        favoriteAdapter = new HistoryListAdapter();
        recyclerFavorite.setLayoutManager(new LinearLayoutManager(this));
        recyclerFavorite.setAdapter(favoriteAdapter);
    }
    
    private void setupListeners() {
        favoriteAdapter.setOnHistoryClickListener(new HistoryListAdapter.OnHistoryClickListener() {
            @Override
            public void onHistoryClick(com.player.tvbox.model.HistoryItem item) {
                // 收藏项使用不同的模型，这里只是示例
                Toast.makeText(FavoriteActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onDeleteClick(com.player.tvbox.model.HistoryItem item) {
                // 删除收藏
                favoriteManager.removeFavorite(item.getTitle());
                loadFavorites();
                Toast.makeText(FavoriteActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
            }
        });
        
        fabClear.setOnClickListener(v -> showClearConfirmDialog());
    }
    
    private void loadFavorites() {
        List<FavoriteItem> favorites = favoriteManager.getAllFavorites();
        
        if (favorites.isEmpty()) {
            findViewById(R.id.text_empty).setVisibility(View.VISIBLE);
            recyclerFavorite.setVisibility(View.GONE);
        } else {
            findViewById(R.id.text_empty).setVisibility(View.GONE);
            recyclerFavorite.setVisibility(View.VISIBLE);
            // TODO: 转换为 HistoryItem 显示
            // favoriteAdapter.setHistoryList(convertToHistoryItems(favorites));
        }
    }
    
    private void showClearConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.clear_favorite)
                .setMessage(R.string.clear_favorite_confirm)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    favoriteManager.clearFavorites();
                    loadFavorites();
                    Toast.makeText(this, "收藏已清空", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (favoriteManager != null) {
            favoriteManager.close();
        }
    }
}
