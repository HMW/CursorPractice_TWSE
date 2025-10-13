package com.hm.cursorpracticetwse.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.hm.cursorpracticetwse.data.local.entities.CompanyEntity
import com.hm.cursorpracticetwse.data.local.entities.IndustryEntity

/**
 * TWSE Room Database
 * 
 * 定義本地資料庫結構和版本管理
 * 包含公司資料和產業分類表格
 */
@Database(
    entities = [
        CompanyEntity::class,
        IndustryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters()
abstract class TwseDatabase : RoomDatabase() {
    
    /**
     * 取得 TWSE DAO
     * 
     * @return TwseDao DAO 介面
     */
    abstract fun twseDao(): TwseDao
    
    companion object {
        const val DATABASE_NAME = "twse_database"
        
        /**
         * 創建資料庫實例
         * 
         * @param context Android Context
         * @return TwseDatabase 資料庫實例
         */
        fun create(context: Context): TwseDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                TwseDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration() // 簡化版本遷移，生產環境應實作 Migration
            .build()
        }
    }
}
