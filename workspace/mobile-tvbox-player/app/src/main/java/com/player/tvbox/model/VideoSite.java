package com.player.tvbox.model;

/**
 * 视频站点数据模型
 * 表示 TVBox 源中的一个视频站点
 */
public class VideoSite {
    
    /**
     * 站点唯一标识
     */
    private String key;
    
    /**
     * 站点名称
     */
    private String name;
    
    /**
     * 站点类型：0=web, 1=cms, 2=jar 等
     */
    private int type;
    
    /**
     * API 地址
     */
    private String api;
    
    /**
     * 是否支持搜索：0=不支持，1=支持
     */
    private int searchable;
    
    /**
     * 是否支持切换：0=不支持，1=支持
     */
    private int changeable;
    
    /**
     * 站点图标
     */
    private String icon;
    
    /**
     * 扩展配置（JSON 字符串）
     */
    private String ext;
    
    public VideoSite() {
        this.searchable = 1;
        this.changeable = 1;
        this.type = 0;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public String getApi() {
        return api;
    }
    
    public void setApi(String api) {
        this.api = api;
    }
    
    public int getSearchable() {
        return searchable;
    }
    
    public void setSearchable(int searchable) {
        this.searchable = searchable;
    }
    
    public int getChangeable() {
        return changeable;
    }
    
    public void setChangeable(int changeable) {
        this.changeable = changeable;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public String getExt() {
        return ext;
    }
    
    public void setExt(String ext) {
        this.ext = ext;
    }
    
    @Override
    public String toString() {
        return "VideoSite{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", api='" + api + '\'' +
                ", searchable=" + searchable +
                ", changeable=" + changeable +
                '}';
    }
}
