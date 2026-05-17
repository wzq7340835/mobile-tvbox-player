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
import com.player.tvbox.db.HistoryManager;
import com.player.tvbox.model.HistoryItem;

import java.util.List;

/**
 * 历史记录 Activity
 */
public class HistoryActivity extends AppCompatActivity {
    
    private RecyclerView recyclerHistory;
    private HistoryListAdapter historyAdapter;
    private HistoryManager historyManager;
    private FloatingActionButton fabClear;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        
        historyManager = new HistoryManager(this);
        
        initViews();
        setupAdapter();
        setupListeners();
        loadHistory();
    }
    
    private void initViews() {
        recyclerHistory = findViewById(R.id.recycler_history);
        fabClear = findViewById(R.id.fab_clear);
    }
    
    private void setupAdapter() {
        historyAdapter = new HistoryListAdapter();
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerHistory.setAdapter(historyAdapter);
    }
    
    private void setupListeners() {
        historyAdapter.setOnHistoryClickListener(new HistoryListAdapter.OnHistoryClickListener() {
            @Override
            public void onHistoryClick(HistoryItem item) {
                // 跳转到播放
                playVideo(item);
            }
            
            @Override
            public void onDeleteClick(HistoryItem item) {
                // 删除历史记录
                deleteHistory(item);
            }
        });
        
        fabClear.setOnClickListener(v -> showClearConfirmDialog());
    }
    
    private void loadHistory() {
        List<HistoryItem> history = historyManager.getHistory(50);
        
        if (history.isEmpty()) {
            // 显示空状态
            findViewById(R.id.text_empty).setVisibility(View.VISIBLE);
            recyclerHistory.setVisibility(View.GONE);
        } else {
            findViewById(R.id.text_empty).setVisibility(View.GONE);
            recyclerHistory.setVisibility(View.VISIBLE);
            historyAdapter.setHistoryList(history);
        }
    }
    
    private void playVideo(HistoryItem item) {
        // TODO: 跳转到播放界面
        Toast.makeText(this, "播放：" + item.getTitle(), Toast.LENGTH_SHORT).show();
    }
    
    private void deleteHistory(HistoryItem item) {
        historyManager.removeHistory(item.getId());
        loadHistory();
        Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
    }
    
    private void showClearConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.clear_history)
                .setMessage(R.string.clear_history_confirm)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    historyManager.clearHistory();
                    loadHistory();
                    Toast.makeText(this, "历史记录已清空", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (historyManager != null) {
            historyManager.close();
        }
    }
}
