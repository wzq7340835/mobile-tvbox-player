package com.player.tvbox.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * 管理数据库的创建和版本升级
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "tvbox_player.db";
    private static final int DATABASE_VERSION = 1;
    
    // 历史记录表
    public static final String TABLE_HISTORY = "history";
    public static final String HISTORY_ID = "id";
    public static final String HISTORY_TITLE = "title";
    public static final String HISTORY_URL = "video_url";
    public static final String HISTORY_SITE_NAME = "site_name";
    public static final String HISTORY_POSITION = "position";
    public static final String HISTORY_DURATION = "duration";
    public static final String HISTORY_WATCH_TIME = "watch_time";
    
    // 收藏表
    public static final String TABLE_FAVORITES = "favorites";
    public static final String FAVORITE_ID = "id";
    public static final String FAVORITE_TITLE = "title";
    public static final String FAVORITE_URL = "video_url";
    public static final String FAVORITE_SITE_NAME = "site_name";
    public static final String FAVORITE_THUMBNAIL = "thumbnail";
    public static final String FAVORITE_ADD_TIME = "add_time";
    
    private static DatabaseHelper instance;
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建历史记录表
        String createHistoryTable = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + " (" +
                HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HISTORY_TITLE + " TEXT NOT NULL, " +
                HISTORY_URL + " TEXT NOT NULL, " +
                HISTORY_SITE_NAME + " TEXT, " +
                HISTORY_POSITION + " INTEGER DEFAULT 0, " +
                HISTORY_DURATION + " INTEGER DEFAULT 0, " +
                HISTORY_WATCH_TIME + " INTEGER NOT NULL)";
        db.execSQL(createHistoryTable);
        
        // 创建收藏表
        String createFavoritesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITES + " (" +
                FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FAVORITE_TITLE + " TEXT UNIQUE NOT NULL, " +
                FAVORITE_URL + " TEXT NOT NULL, " +
                FAVORITE_SITE_NAME + " TEXT, " +
                FAVORITE_THUMBNAIL + " TEXT, " +
                FAVORITE_ADD_TIME + " INTEGER NOT NULL)";
        db.execSQL(createFavoritesTable);
        
        // 创建索引
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_watch_time ON " + TABLE_HISTORY + "(" + HISTORY_WATCH_TIME + " DESC)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS idx_url ON " + TABLE_HISTORY + "(" + HISTORY_URL + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_add_time ON " + TABLE_FAVORITES + "(" + FAVORITE_ADD_TIME + " DESC)");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // 版本升级逻辑
        }
    }
    
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 降级处理
    }
    
    /**
     * 清空所有数据（用于用户重置）
     */
    public void clearAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_HISTORY, null, null);
        db.delete(TABLE_FAVORITES, null, null);
    }
}
