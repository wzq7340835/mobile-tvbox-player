package com.fongmi.android.tv.db

import android.content.Context
import androidx.room.*
import com.fongmi.android.tv.bean.*

@Database(
    entities = [
        Config::class, Keep::class, History::class, Collect::class,
        Word::class, Doh::class, Proxy::class
    ],
    version = 35,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun configDao(): ConfigDao
    abstract fun keepDao(): KeepDao
    abstract fun historyDao(): HistoryDao
    abstract fun collectDao(): CollectDao
    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tv.db"
                )
                    .addMigrations(*Migrations.ALL)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}

@Dao
interface ConfigDao {
    @Query("SELECT * FROM Config WHERE type = :type AND active = 1 LIMIT 1")
    suspend fun getActive(type: Int): Config?

    @Query("SELECT * FROM Config WHERE type = :type")
    suspend fun getAll(type: Int): List<Config>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg configs: Config)

    @Delete
    suspend fun delete(vararg configs: Config)

    @Query("DELETE FROM Config WHERE type = :type")
    suspend fun deleteAll(type: Int)

    @Query("UPDATE Config SET active = :active WHERE uid = :uid")
    suspend fun setActive(uid: Long, active: Boolean)
}

@Dao
interface KeepDao {
    @Query("SELECT * FROM Keep ORDER BY time DESC")
    suspend fun getAll(): List<Keep>

    @Query("SELECT * FROM Keep WHERE vodId = :vodId AND sourceKey = :sourceKey LIMIT 1")
    suspend fun find(vodId: String, sourceKey: String): Keep?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg keeps: Keep)

    @Delete
    suspend fun delete(vararg keeps: Keep)

    @Query("DELETE FROM Keep")
    suspend fun deleteAll()
}

@Dao
interface HistoryDao {
    @Query("SELECT * FROM History ORDER BY time DESC")
    suspend fun getAll(): List<History>

    @Query("SELECT * FROM History WHERE vodId = :vodId AND sourceKey = :sourceKey LIMIT 1")
    suspend fun find(vodId: String, sourceKey: String): History?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg histories: History)

    @Delete
    suspend fun delete(vararg histories: History)

    @Query("DELETE FROM History")
    suspend fun deleteAll()
}

@Dao
interface CollectDao {
    @Query("SELECT * FROM Collect ORDER BY time DESC")
    suspend fun getAll(): List<Collect>

    @Query("SELECT * FROM Collect WHERE url = :url LIMIT 1")
    suspend fun find(url: String): Collect?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg collects: Collect)

    @Delete
    suspend fun delete(vararg collects: Collect)

    @Query("DELETE FROM Collect")
    suspend fun deleteAll()
}

@Dao
interface WordDao {
    @Query("SELECT * FROM Word ORDER BY time DESC")
    suspend fun getAll(): List<Word>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg words: Word)

    @Delete
    suspend fun delete(vararg words: Word)

    @Query("DELETE FROM Word")
    suspend fun deleteAll()
}

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toStringList(value: String): List<String> =
        if (value.isBlank()) emptyList() else value.split(",")
}

object Migrations {
    val ALL = emptyArray<Migration>()
}
