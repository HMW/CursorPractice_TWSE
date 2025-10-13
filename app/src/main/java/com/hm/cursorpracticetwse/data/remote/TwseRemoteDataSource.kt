package com.hm.cursorpracticetwse.data.remote

import com.hm.cursorpracticetwse.data.remote.dto.CompanyDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TWSE Remote Data Source
 * 
 * 實作遠端資料源，負責從 TWSE API 取得資料
 * 處理網路請求和錯誤處理
 */
@Singleton
class TwseRemoteDataSource @Inject constructor(
    private val apiService: TwseApiService
) {
    
    /**
     * 從 TWSE API 取得公司資料
     * 
     * @return List<CompanyDto> 公司資料列表
     * @throws Exception 當網路請求失敗時拋出例外
     */
    suspend fun fetchCompanies(): List<CompanyDto> {
        return try {
            apiService.getCompanies()
        } catch (e: Exception) {
            throw RemoteDataSourceException("Failed to fetch companies from TWSE API", e)
        }
    }
}

/**
 * Remote Data Source 例外類別
 */
class RemoteDataSourceException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
