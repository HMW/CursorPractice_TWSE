package com.hm.cursorpracticetwse.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 追蹤列表項目領域模型
 * 
 * 代表用戶追蹤的公司項目
 */
@Parcelize
data class WatchlistItem(
    val companyCode: String,
    val companyName: String,
    val companyShortName: String,
    val industryCode: String,
    val industryName: String,
    val addedDate: String
) : Parcelable {
    
    /**
     * 取得顯示名稱
     * 優先使用公司簡稱，如果為空則使用公司名稱
     */
    fun getDisplayName(): String {
        return if (companyShortName.isNotBlank()) companyShortName else companyName
    }
    
    /**
     * 取得完整顯示名稱
     * 包含公司代碼和名稱
     */
    fun getFullDisplayName(): String {
        return "$companyCode ${getDisplayName()}"
    }
}
