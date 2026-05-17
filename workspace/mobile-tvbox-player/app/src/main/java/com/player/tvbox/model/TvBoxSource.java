package com.player.tvbox.model;

import java.util.List;

/**
 * TVBox 源数据模型
 * 表示一个完整的 TVBox 配置，包含多个视频站点
 */
public class TvBoxSource {
    
    /**
     * 源名称
     */
    private String name;
    
    /**
     * 源 URL 地址
     */
    private String url;
    
    /**
     * 视频站点列表
     */
    private List<VideoSite> sites;
    
    /**
     * 导入时间戳
     */
    private long importTime;
    
    public TvBoxSource() {
        this.importTime = System.currentTimeMillis();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public List<VideoSite> getSites() {
        return sites;
    }
    
    public void setSites(List<VideoSite> sites) {
        this.sites = sites;
    }
    
    public long getImportTime() {
        return importTime;
    }
    
    public void setImportTime(long importTime) {
        this.importTime = importTime;
    }
    
    @Override
    public String toString() {
        return "TvBoxSource{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", sites=" + (sites != null ? sites.size() : 0) +
                ", importTime=" importTime +
                '}';
    }
}
