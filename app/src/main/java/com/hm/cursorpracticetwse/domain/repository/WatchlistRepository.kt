package com.hm.cursorpracticetwse.domain.repository

import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.model.WatchlistItem

/**
 * 追蹤列表 Repository 介面
 * 
 * 定義追蹤列表相關的資料操作
 */
interface WatchlistRepository {
    
    /**
     * 取得追蹤列表
     * 
     * @return 追蹤列表結果
     */
    suspend fun getWatchlist(): Result<List<WatchlistItem>>
    
    /**
     * 將公司加入追蹤列表
     * 
     * @param company 要加入的公司
     * @return 操作結果
     */
    suspend fun addToWatchlist(company: Company): Result<Unit>
    
    /**
     * 從追蹤列表移除公司
     * 
     * @param companyCode 要移除的公司代碼
     * @return 操作結果
     */
    suspend fun removeFromWatchlist(companyCode: String): Result<Unit>
    
    /**
     * 檢查公司是否在追蹤列表中
     * 
     * @param companyCode 要檢查的公司代碼
     * @return 是否在追蹤列表中的結果
     */
    suspend fun isInWatchlist(companyCode: String): Result<Boolean>
}
