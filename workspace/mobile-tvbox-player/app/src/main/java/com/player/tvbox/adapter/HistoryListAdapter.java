package com.player.tvbox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.player.tvbox.R;
import com.player.tvbox.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史记录列表适配器
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder> {
    
    private List<HistoryItem> historyList = new ArrayList<>();
    private OnHistoryClickListener listener;
    
    /**
     * 历史记录点击监听器
     */
    public interface OnHistoryClickListener {
        void onHistoryClick(HistoryItem item);
        void onDeleteClick(HistoryItem item);
    }
    
    public void setOnHistoryClickListener(OnHistoryClickListener listener) {
        this.listener = listener;
    }
    
    /**
     * 设置历史记录列表
     */
    public void setHistoryList(List<HistoryItem> list) {
        this.historyList.clear();
        if (list != null) {
            this.historyList.addAll(list);
        }
        notifyDataSetChanged();
    }
    
    @Override
    @NonNull
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem item = historyList.get(position);
        
        // 设置标题
        holder.tvTitle.setText(item.getTitle());
        
        // 设置站点名
        holder.tvSite.setText(item.getSiteName());
        
        // 设置时间
        holder.tvTime.setText(item.getFormattedWatchTime());
        
        // 设置进度
        int percent = 0;
        if (item.getDuration() > 0) {
            percent = (int) ((item.getPosition() * 100) / item.getDuration());
        }
        holder.progressBar.setProgress(percent);
        holder.tvProgress.setText(percent + "%");
        
        // 加载缩略图（如果有）
        // TODO: 实际使用时需要获取缩略图 URL
        
        // 点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHistoryClick(item);
            }
        });
        
        holder.btnPlay.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHistoryClick(item);
            }
        });
        
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(item);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return historyList.size();
    }
    
    /**
     * 删除指定项
     */
    public void removeItem(int position) {
        historyList.remove(position);
        notifyItemRemoved(position);
    }
    
    /**
     * ViewHolder
     */
    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvTitle;
        TextView tvSite;
        TextView tvTime;
        ProgressBar progressBar;
        TextView tvProgress;
        ImageButton btnPlay;
        ImageButton btnDelete;
        
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSite = itemView.findViewById(R.id.tv_site);
            tvTime = itemView.findViewById(R.id.tv_time);
            progressBar = itemView.findViewById(R.id.progress_bar);
            tvProgress = itemView.findViewById(R.id.tv_progress);
            btnPlay = itemView.findViewById(R.id.btn_play);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
