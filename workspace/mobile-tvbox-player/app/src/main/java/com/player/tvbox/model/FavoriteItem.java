package com.player.tvbox.model;

/**
 * 收藏项数据模型
 */
public class FavoriteItem {
    
    /**
     * 收藏 ID
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
     * 缩略图 URL
     */
    private String thumbnail;
    
    /**
     * 添加时间戳
     */
    private long addTime;
    
    public FavoriteItem() {
        this.addTime = System.currentTimeMillis();
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
    
    public String getThumbnail() {
        return thumbnail;
    }
    
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    public long getAddTime() {
        return addTime;
    }
    
    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }
    
    /**
     * 获取格式化的添加时间
     */
    public String getFormattedAddTime() {
        long seconds = (System.currentTimeMillis() - addTime) / 1000;
        if (seconds < 60) {
            return "刚刚";
        } else if (seconds < 3600) {
            return (seconds / 60) + "分钟前";
        } else if (seconds < 86400) {
            return (seconds / 3600) + "小时前";
        } else if (seconds < 604800) {
            return (seconds / 86400) + "天前";
        } else {
            return addTime / 1000 + "秒";
        }
    }
    
    @Override
    public String toString() {
        return "FavoriteItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", siteName='" + siteName + '\'' +
                '}';
    }
}
