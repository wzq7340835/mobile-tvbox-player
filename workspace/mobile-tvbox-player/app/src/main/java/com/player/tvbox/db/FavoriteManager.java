package com.player.tvbox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.player.tvbox.model.FavoriteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏管理器
 * 负责视频收藏的增删改查
 */
public class FavoriteManager {
    
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    
    public FavoriteManager(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }
    
    /**
     * 添加收藏
     * @param title 视频标题
     * @param url 视频 URL
     * @param siteName 站点名称
     * @param thumbnail 缩略图 URL
     * @return 收藏 ID，如果已存在返回 -1
     */
    public long addFavorite(String title, String url, String siteName, String thumbnail) {
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.FAVORITE_TITLE, title);
            values.put(DatabaseHelper.FAVORITE_URL, url);
            values.put(DatabaseHelper.FAVORITE_SITE_NAME, siteName);
            values.put(DatabaseHelper.FAVORITE_THUMBNAIL, thumbnail);
            values.put(DatabaseHelper.FAVORITE_ADD_TIME, System.currentTimeMillis());
            
            // 使用 INSERT_OR_THROW，如果已存在会抛出异常
            return db.insertOrThrow(DatabaseHelper.TABLE_FAVORITES, null, values);
        } catch (SQLException e) {
            // 已存在
            return -1;
        }
    }
    
    /**
     * 移除收藏
     * @param title 视频标题
     * @return 是否移除成功
     */
    public boolean removeFavorite(String title) {
        try {
            return db.delete(DatabaseHelper.TABLE_FAVORITES,
                    DatabaseHelper.FAVORITE_TITLE + "=?",
                    new String[]{title}) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 检查是否已收藏
     * @param title 视频标题
     * @return 是否已收藏
     */
    public boolean isFavorite(String title) {
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_FAVORITES,
                new String[]{DatabaseHelper.FAVORITE_ID},
                DatabaseHelper.FAVORITE_TITLE + "=?",
                new String[]{title},
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
     * 检查 URL 是否已收藏
     * @param url 视频 URL
     * @return 是否已收藏
     */
    public boolean isFavoriteByUrl(String url) {
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_FAVORITES,
                new String[]{DatabaseHelper.FAVORITE_ID},
                DatabaseHelper.FAVORITE_URL + "=?",
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
     * 获取所有收藏
     * @return 收藏列表
     */
    public List<FavoriteItem> getAllFavorites() {
        List<FavoriteItem> list = new ArrayList<>();
        
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_FAVORITES,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.FAVORITE_ADD_TIME + " DESC"
        )) {
            int idIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_ID);
            int titleIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_TITLE);
            int urlIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_URL);
            int siteNameIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_SITE_NAME);
            int thumbnailIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_THUMBNAIL);
            int addTimeIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_ADD_TIME);
            
            while (cursor.moveToNext()) {
                FavoriteItem item = new FavoriteItem();
                item.setId(cursor.getLong(idIndex));
                item.setTitle(cursor.getString(titleIndex));
                item.setUrl(cursor.getString(urlIndex));
                item.setSiteName(cursor.getString(siteNameIndex));
                item.setThumbnail(cursor.getString(thumbnailIndex));
                item.setAddTime(cursor.getLong(addTimeIndex));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * 根据标题获取收藏
     * @param title 视频标题
     * @return 收藏项
     */
    public FavoriteItem getFavoriteByTitle(String title) {
        FavoriteItem item = null;
        
        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_FAVORITES,
                null,
                DatabaseHelper.FAVORITE_TITLE + "=?",
                new String[]{title},
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
     * 清空所有收藏
     */
    public void clearFavorites() {
        db.delete(DatabaseHelper.TABLE_FAVORITES, null, null);
    }
    
    /**
     * 获取收藏总数
     * @return 总数
     */
    public int getCount() {
        try (Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_FAVORITES,
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
     * Cursor 转 FavoriteItem
     */
    private FavoriteItem cursorToItem(Cursor cursor) {
        FavoriteItem item = new FavoriteItem();
        int idIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_ID);
        int titleIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_TITLE);
        int urlIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_URL);
        int siteNameIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_SITE_NAME);
        int thumbnailIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_THUMBNAIL);
        int addTimeIndex = cursor.getColumnIndex(DatabaseHelper.FAVORITE_ADD_TIME);
        
        item.setId(cursor.getLong(idIndex));
        item.setTitle(cursor.getString(titleIndex));
        item.setUrl(cursor.getString(urlIndex));
        item.setSiteName(cursor.getString(siteNameIndex));
        item.setThumbnail(cursor.getString(thumbnailIndex));
        item.setAddTime(cursor.getLong(addTimeIndex));
        
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
