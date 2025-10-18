package com.hm.cursorpracticetwse.di

import android.content.Context
import com.hm.cursorpracticetwse.data.local.TwseDao
import com.hm.cursorpracticetwse.data.local.TwseDatabase
import com.hm.cursorpracticetwse.data.local.TwseLocalDataSource
import com.hm.cursorpracticetwse.data.local.dao.WatchlistDao
import com.hm.cursorpracticetwse.data.local.WatchlistLocalDataSource
import com.hm.cursorpracticetwse.data.local.WatchlistLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Database Module for Hilt Dependency Injection
 * 
 * 提供資料庫相關的依賴注入配置
 * 包含 Room Database, DAO, Local Data Source 等
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * 提供 TWSE Database
     * 使用 Room 創建資料庫實例
     */
    @Provides
    @Singleton
    fun provideTwseDatabase(@ApplicationContext context: Context): TwseDatabase {
        return TwseDatabase.create(context)
    }
    
    /**
     * 提供 TWSE DAO
     * 從 Database 取得 DAO 介面
     */
    @Provides
    @Singleton
    fun provideTwseDao(database: TwseDatabase): TwseDao {
        return database.twseDao()
    }
    
    /**
     * 提供 TWSE Local Data Source
     * 注入 DAO 依賴
     */
    @Provides
    @Singleton
    fun provideTwseLocalDataSource(dao: TwseDao): TwseLocalDataSource {
        return TwseLocalDataSource(dao)
    }
    
    /**
     * 提供 Watchlist DAO
     * 從 Database 取得 Watchlist DAO 介面
     */
    @Provides
    @Singleton
    fun provideWatchlistDao(database: TwseDatabase): WatchlistDao {
        return database.watchlistDao()
    }
    
    /**
     * 提供 Watchlist Local Data Source
     * 注入 Watchlist DAO 依賴
     */
    @Provides
    @Singleton
    fun provideWatchlistLocalDataSource(dao: WatchlistDao): WatchlistLocalDataSource {
        return WatchlistLocalDataSourceImpl(dao)
    }
}
