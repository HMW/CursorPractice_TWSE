package com.hm.cursorpracticetwse.domain.model

/**
 * 產業領域模型
 * 
 * 代表台灣證券交易所的產業分類
 * 包含產業代碼、名稱和公司數量
 */
data class Industry(
    val code: String,
    val name: String,
    val companyCount: Int
) {
    
    /**
     * 取得產業顯示名稱
     * 格式：產業名稱(公司數量)
     */
    fun getDisplayName(): String {
        return "$name($companyCount)"
    }
    
    /**
     * 檢查是否有公司
     */
    fun hasCompanies(): Boolean {
        return companyCount > 0
    }
    
    /**
     * 取得產業完整資訊
     * 格式：代碼: 產業名稱(公司數量)
     */
    fun getFullInfo(): String {
        return "$code: $name($companyCount)"
    }
}
