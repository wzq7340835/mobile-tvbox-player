package com.player.tvbox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.player.tvbox.R;
import com.player.tvbox.model.VideoSite;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频站点列表适配器
 */
public class SiteListAdapter extends RecyclerView.Adapter<SiteListAdapter.SiteViewHolder> {
    
    private List<VideoSite> sites = new ArrayList<>();
    private OnSiteClickListener listener;
    
    /**
     * 站点点击监听器
     */
    public interface OnSiteClickListener {
        void onSiteClick(VideoSite site);
        void onSitePlay(VideoSite site);
    }
    
    public void setOnSiteClickListener(OnSiteClickListener listener) {
        this.listener = listener;
    }
    
    /**
     * 设置站点列表
     */
    public void setSites(List<VideoSite> sites) {
        this.sites.clear();
        if (sites != null) {
            this.sites.addAll(sites);
        }
        notifyDataSetChanged();
    }
    
    @Override
    @NonNull
    public SiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_site, parent, false);
        return new SiteViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SiteViewHolder holder, int position) {
        VideoSite site = sites.get(position);
        
        // 设置站点名称
        holder.tvName.setText(site.getName());
        
        // 设置类型
        String typeText = "类型：" + site.getType();
        holder.tvType.setText(typeText);
        
        // 加载图标
        if (site.getIcon() != null && !site.getIcon().isEmpty()) {
            Glide.with(holder.ivIcon.getContext())
                    .load(site.getIcon())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivIcon);
        }
        
        // 点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSiteClick(site);
            }
        });
        
        holder.btnPlay.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSitePlay(site);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return sites.size();
    }
    
    /**
     * ViewHolder
     */
    static class SiteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvType;
        ImageButton btnPlay;
        
        public SiteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
            btnPlay = itemView.findViewById(R.id.btn_play);
        }
    }
}
