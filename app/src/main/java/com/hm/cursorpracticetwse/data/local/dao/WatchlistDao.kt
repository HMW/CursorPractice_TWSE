package com.hm.cursorpracticetwse.data.local.dao

import androidx.room.*
import com.hm.cursorpracticetwse.data.local.entities.WatchlistItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * 追蹤列表 DAO
 * 
 * 定義追蹤列表的資料庫操作
 */
@Dao
interface WatchlistDao {
    
    /**
     * 取得所有追蹤項目
     * 
     * @return 追蹤項目列表的 Flow
     */
    @Query("SELECT * FROM watchlist_items ORDER BY addedDate DESC")
    fun getAllWatchlistItems(): Flow<List<WatchlistItemEntity>>
    
    /**
     * 插入追蹤項目
     * 
     * @param item 要插入的追蹤項目
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistItem(item: WatchlistItemEntity)
    
    /**
     * 刪除追蹤項目
     * 
     * @param companyCode 要刪除的公司代碼
     */
    @Query("DELETE FROM watchlist_items WHERE companyCode = :companyCode")
    suspend fun deleteWatchlistItem(companyCode: String)
    
    /**
     * 檢查追蹤項目是否存在
     * 
     * @param companyCode 要檢查的公司代碼
     * @return 是否存在
     */
    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_items WHERE companyCode = :companyCode)")
    suspend fun isWatchlistItemExists(companyCode: String): Boolean
    
    /**
     * 取得追蹤項目
     * 
     * @param companyCode 公司代碼
     * @return 追蹤項目
     */
    @Query("SELECT * FROM watchlist_items WHERE companyCode = :companyCode")
    suspend fun getWatchlistItem(companyCode: String): WatchlistItemEntity?
}
