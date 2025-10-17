package com.hm.cursorpracticetwse.domain.repository

import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.model.Industry
import kotlinx.coroutines.flow.Flow

/**
 * TWSE Repository 介面
 * 
 * 定義資料存取的抽象介面，遵循依賴反轉原則
 * 實作類別在 Data Layer 中提供
 */
interface TwseRepository {
    
    /**
     * 取得所有公司資料
     * 
     * @return Result<List<Company>> 公司列表的結果
     */
    suspend fun getCompanies(): Result<List<Company>>
    
    /**
     * 取得產業分類列表
     * 
     * @return Result<List<Industry>> 產業列表的結果
     */
    suspend fun getIndustries(): Result<List<Industry>>
    
    /**
     * 根據產業代碼取得公司列表
     * 
     * @param industryCode 產業代碼
     * @return Result<List<Company>> 該產業的公司列表
     */
    suspend fun getCompaniesByIndustry(industryCode: String): Result<List<Company>>
    
    /**
     * 重新整理資料
     * 從遠端 API 取得最新資料並更新本地快取
     * 
     * @return Flow<Result<Unit>> 重新整理結果
     */
    suspend fun refreshData(): Flow<Result<Unit>>
}
