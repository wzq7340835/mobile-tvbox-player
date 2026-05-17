package com.player.tvbox.model;

/**
 * 历史记录项数据模型
 */
public class HistoryItem {
    
    /**
     * 记录 ID
     */
    private long id;
    
    /**
     * 视频标题
     */
    private String title;
    
    /**
     * 视频 URL
     */
    private String url;
    
    /**
     * 站点名称
     */
    private String siteName;
    
    /**
     * 观看进度（毫秒）
     */
    private long position;
    
    /**
     * 视频总时长（毫秒）
     */
    private long duration;
    
    /**
     * 观看时间戳
     */
    private long watchTime;
    
    public HistoryItem() {
        this.watchTime = System.currentTimeMillis();
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getSiteName() {
        return siteName;
    }
    
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    public long getPosition() {
        return position;
    }
    
    public void setPosition(long position) {
        this.position = position;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    public long getWatchTime() {
        return watchTime;
    }
    
    public void setWatchTime(long watchTime) {
        this.watchTime = watchTime;
    }
    
    /**
     * 获取格式化的观看时间
     */
    public String getFormattedWatchTime() {
        long seconds = (System.currentTimeMillis() - watchTime) / 1000;
        if (seconds < 60) {
            return seconds + "秒前";
        } else if (seconds < 3600) {
            return (seconds / 60) + "分钟前";
        } else if (seconds < 86400) {
            return (seconds / 3600) + "小时前";
        } else {
            return (seconds / 86400) + "天前";
        }
    }
    
    /**
     * 获取格式化的进度
     */
    public String getProgressPercent() {
        if (duration <= 0) {
            return "0%";
        }
        int percent = (int) ((position * 100) / duration);
        return percent + "%";
    }
    
    @Override
    public String toString() {
        return "HistoryItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", position=" + position +
                ", duration=" + duration +
                '}';
    }
}
