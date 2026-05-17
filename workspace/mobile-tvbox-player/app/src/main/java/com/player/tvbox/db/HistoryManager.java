package com.player.tvbox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.player.tvbox.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史记录管理器
 * 负责观看历史记录的增删改查
 */
public class HistoryManager {
    
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    
    public HistoryManager(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }
    
    /**
     * 添加历史记录
     * @param title 视频标题
     * @param url 视频 URL
     * @param siteName 站点名称
     * @param position 观看进度（毫秒）
     * @param duration 视频总时长（毫秒）
     * @return 记录 ID，如果失败返回 -1
     */
    public long addHistory(String title, String url, String siteName, 
                          long position, long duration) {
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.HISTORY_TITLE, title);
            values.put(DatabaseHelper.HISTORY_URL, url);
            values.put(DatabaseHelper.HISTORY_SITE_NAME, siteName);
            values.put(DatabaseHelper.HISTORY_POSITION, position);
            values.put(DatabaseHelper.HISTORY_DURATION, duration);
            values.put(DatabaseHelper.HISTORY_WATCH_TIME, System.currentTimeMillis());
            
            // 使用 INSERT_OR_REPLACE 策略，相同 URL 会更新
            return db.insertWithOnConflict(DatabaseHelper.TABLE_HISTORY, null, values,
                    SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * 更新观看进度
     * @param url 视频 URL
     * @param position 当前进度
     * @param duration 视频总时长
     */
    public void updateProgress(String url, long position, long duration) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.HISTORY_POSITION, position);
        values.put(DatabaseHelper.HISTORY_DURATION, duration);
        values.put(DatabaseHelper.HISTORY_WATCH_TIME, System.currentTimeMillis());
        
        db.update(DatabaseHelper.TABLE_HISTORY, values,
                DatabaseHelper.HISTORY_URL + "=?",
                new String[]{url});
    }
    
    /**
     * 获取观看历史列表
     * @param limit 最多返回的数量
     * @return 历史记录列表
     */
    public List<HistoryItem> getHistory(int limit) {
        List<HistoryItem> list = new ArrayList<>();
        
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_HISTORY,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.HISTORY_WATCH_TIME + " DESC",
                String.valueOf(limit)
        )) {
            int idIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_ID);
            int titleIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_TITLE);
            int urlIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_URL);
            int siteNameIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_SITE_NAME);
            int positionIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_POSITION);
            int durationIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_DURATION);
            int watchTimeIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_WATCH_TIME);
            
            while (cursor.moveToNext()) {
                HistoryItem item = new HistoryItem();
                item.setId(cursor.getLong(idIndex));
                item.setTitle(cursor.getString(titleIndex));
                item.setUrl(cursor.getString(urlIndex));
                item.setSiteName(cursor.getString(siteNameIndex));
                item.setPosition(cursor.getLong(positionIndex));
                item.setDuration(cursor.getLong(durationIndex));
                item.setWatchTime(cursor.getLong(watchTimeIndex));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * 根据 URL 获取历史记录
     * @param url 视频 URL
     * @return 历史记录项，不存在返回 null
     */
    public HistoryItem getHistoryByUrl(String url) {
        HistoryItem item = null;
        
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_HISTORY,
                null,
                DatabaseHelper.HISTORY_URL + "=?",
                new String[]{url},
                null,
                null,
                null,
                "1"
        )) {
            if (cursor.moveToFirst()) {
                item = cursorToItem(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return item;
    }
    
    /**
     * 删除单条历史记录
     * @param id 记录 ID
     * @return 是否删除成功
     */
    public boolean removeHistory(long id) {
        try {
            return db.delete(DatabaseHelper.TABLE_HISTORY,
                    DatabaseHelper.HISTORY_ID + "=?",
                    new String[]{String.valueOf(id)}) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 删除指定 URL 的历史记录
     * @param url 视频 URL
     * @return 是否删除成功
     */
    public boolean removeHistoryByUrl(String url) {
        try {
            return db.delete(DatabaseHelper.TABLE_HISTORY,
                    DatabaseHelper.HISTORY_URL + "=?",
                    new String[]{url}) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 清空所有历史记录
     */
    public void clearHistory() {
        db.delete(DatabaseHelper.TABLE_HISTORY, null, null);
    }
    
    /**
     * 清理 30 天前的历史记录
     */
    public void cleanupOldHistory() {
        long thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000);
        db.delete(DatabaseHelper.TABLE_HISTORY,
                DatabaseHelper.HISTORY_WATCH_TIME + " < ?",
                new String[]{String.valueOf(thirtyDaysAgo)});
    }
    
    /**
     * 检查是否存在某条历史记录
     * @param url 视频 URL
     * @return 是否存在
     */
    public boolean exists(String url) {
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_HISTORY,
                new String[]{DatabaseHelper.HISTORY_ID},
                DatabaseHelper.HISTORY_URL + "=?",
                new String[]{url},
                null,
                null,
                null,
                "1"
        )) {
            return cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 获取历史记录总数
     * @return 总数
     */
    public int getCount() {
        try (Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_HISTORY,
                null
        )) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Cursor 转 HistoryItem
     */
    private HistoryItem cursorToItem(Cursor cursor) {
        HistoryItem item = new HistoryItem();
        int idIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_ID);
        int titleIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_TITLE);
        int urlIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_URL);
        int siteNameIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_SITE_NAME);
        int positionIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_POSITION);
        int durationIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_DURATION);
        int watchTimeIndex = cursor.getColumnIndex(DatabaseHelper.HISTORY_WATCH_TIME);
        
        item.setId(cursor.getLong(idIndex));
        item.setTitle(cursor.getString(titleIndex));
        item.setUrl(cursor.getString(urlIndex));
        item.setSiteName(cursor.getString(siteNameIndex));
        item.setPosition(cursor.getLong(positionIndex));
        item.setDuration(cursor.getLong(durationIndex));
        item.setWatchTime(cursor.getLong(watchTimeIndex));
        
        return item;
    }
    
    /**
     * 关闭数据库
     */
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
