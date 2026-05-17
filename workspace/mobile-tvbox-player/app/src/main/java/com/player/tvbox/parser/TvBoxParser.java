package com.player.tvbox.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.player.tvbox.model.TvBoxSource;
import com.player.tvbox.model.VideoSite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * TVBox 源解析器
 * 负责解析 TVBox JSON 配置格式
 */
public class TvBoxParser {
    
    private static final String TAG = "TvBoxParser";
    
    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build();
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    /**
     * 解析回调接口
     */
    public interface ParseCallback {
        void onSuccess(TvBoxSource source);
        void onError(String error);
    }
    
    /**
     * 从 URL 解析 TVBox 源
     * @param url 配置 URL
     * @param callback 回调
     */
    public static void parseFromUrl(String url, ParseCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onError("网络请求失败：" + e.getMessage());
                }
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if (callback != null) {
                        callback.onError("HTTP 错误：" + response.code());
                    }
                    return;
                }
                
                String body = response.body().string();
                try {
                    TvBoxSource source = parseJson(body);
                    source.setUrl(url);
                    if (callback != null) {
                        callback.onSuccess(source);
                    }
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onError("解析失败：" + e.getMessage());
                    }
                }
            }
        });
    }
    
    /**
     * 从本地文件解析 TVBox 源
     * @param file JSON 文件
     * @param callback 回调
     */
    public static void parseFromFile(File file, ParseCallback callback) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                
                TvBoxSource source = parseJson(sb.toString());
                if (callback != null) {
                    callback.onSuccess(source);
                }
            } catch (IOException e) {
                if (callback != null) {
                    callback.onError("文件读取失败：" + e.getMessage());
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError("解析失败：" + e.getMessage());
                }
            }
        }).start();
    }
    
    /**
     * 解析 JSON 字符串为 TvBoxSource 对象
     * @param json JSON 字符串
     * @return TvBoxSource 对象
     * @throws Exception 解析异常
     */
    public static TvBoxSource parseJson(String json) throws Exception {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        TvBoxSource source = new TvBoxSource();
        
        // 解析名称
        if (obj.has("name")) {
            source.setName(obj.get("name").getAsString());
        } else {
            source.setName("TVBox 源");
        }
        
        // 解析站点列表
        List<VideoSite> sites = new ArrayList<>();
        if (obj.has("sites") && obj.get("sites").isJsonArray()) {
            JsonArray sitesArray = obj.getAsJsonArray("sites");
            for (int i = 0; i < sitesArray.size(); i++) {
                try {
                    JsonObject siteObj = sitesArray.get(i).getAsJsonObject();
                    VideoSite site = new VideoSite();
                    
                    if (siteObj.has("key")) {
                        site.setKey(siteObj.get("key").getAsString());
                    }
                    if (siteObj.has("name")) {
                        site.setName(siteObj.get("name").getAsString());
                    }
                    if (siteObj.has("type")) {
                        site.setType(siteObj.get("type").getAsInt());
                    }
                    if (siteObj.has("api")) {
                        site.setApi(siteObj.get("api").getAsString());
                    }
                    if (siteObj.has("searchable")) {
                        site.setSearchable(siteObj.get("searchable").getAsInt());
                    }
                    if (siteObj.has("changeable")) {
                        site.setChangeable(siteObj.get("changeable").getAsInt());
                    }
                    if (siteObj.has("icon")) {
                        site.setIcon(siteObj.get("icon").getAsString());
                    }
                    if (siteObj.has("ext")) {
                        site.setExt(siteObj.get("ext").getAsString());
                    }
                    
                    sites.add(site);
                } catch (Exception e) {
                    // 跳过无效的站点
                }
            }
        }
        
        source.setSites(sites);
        return source;
    }
    
    /**
     * 将 TvBoxSource 对象转换为 JSON 字符串
     * @param source TvBoxSource 对象
     * @return JSON 字符串
     */
    public static String toJson(TvBoxSource source) {
        return gson.toJson(source);
    }
    
    /**
     * 同步从 URL 获取（不推荐在主线程使用）
     * @param url 配置 URL
     * @return TvBoxSource 对象
     * @throws IOException 网络异常
     */
    public static TvBoxSource parseFromUrlSync(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP error: " + response.code());
            }
            
            String body = response.body().string();
            try {
                TvBoxSource source = parseJson(body);
                source.setUrl(url);
                return source;
            } catch (Exception e) {
                throw new IOException("Parse error: " + e.getMessage());
            }
        }
    }
}
