package com.hm.cursorpracticetwse.data.remote

import com.hm.cursorpracticetwse.data.remote.dto.CompanyDto
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * TWSE API Service
 * 
 * 使用 Retrofit 定義台灣證券交易所 Open API 的介面
 * 端點：https://openapi.twse.com.tw/v1/opendata/t187ap03_P
 */
interface TwseApiService {
    
    /**
     * 取得公開發行公司基本資料
     * 
     * @return List<CompanyDto> 公司資料列表
     */
    @GET("/v1/opendata/t187ap03_P")
    @Headers(
        "Accept: application/json",
        "Cache-Control: no-cache",
        "Pragma: no-cache"
    )
    suspend fun getCompanies(): List<CompanyDto>
}
