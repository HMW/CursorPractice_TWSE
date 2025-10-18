package com.hm.cursorpracticetwse.data.local

import com.hm.cursorpracticetwse.data.local.entities.WatchlistItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * 追蹤列表本地資料源介面
 * 
 * 定義追蹤列表的本地資料操作
 */
interface WatchlistLocalDataSource {
    
    /**
     * 取得所有追蹤項目
     * 
     * @return 追蹤項目列表的 Flow
     */
    fun getWatchlistItems(): Flow<List<WatchlistItemEntity>>
    
    /**
     * 插入追蹤項目
     * 
     * @param item 要插入的追蹤項目
     */
    suspend fun insertWatchlistItem(item: WatchlistItemEntity)
    
    /**
     * 刪除追蹤項目
     * 
     * @param companyCode 要刪除的公司代碼
     */
    suspend fun deleteWatchlistItem(companyCode: String)
    
    /**
     * 檢查追蹤項目是否存在
     * 
     * @param companyCode 要檢查的公司代碼
     * @return 是否存在
     */
    suspend fun isWatchlistItemExists(companyCode: String): Boolean
}
