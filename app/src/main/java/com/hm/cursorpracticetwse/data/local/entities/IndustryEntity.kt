package com.hm.cursorpracticetwse.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 產業資料庫實體
 * 
 * 用於儲存產業分類統計結果
 * 包含產業代碼、名稱和公司數量
 */
@Entity(tableName = "industries")
data class IndustryEntity(
    @PrimaryKey
    val code: String,
    
    val name: String,
    val companyCount: Int
)
