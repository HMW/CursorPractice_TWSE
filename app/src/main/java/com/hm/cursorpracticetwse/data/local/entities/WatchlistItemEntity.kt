package com.hm.cursorpracticetwse.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 追蹤列表項目實體
 * 
 * 代表資料庫中的追蹤列表項目
 */
@Entity(tableName = "watchlist_items")
data class WatchlistItemEntity(
    @PrimaryKey
    val companyCode: String,
    val companyName: String,
    val companyShortName: String,
    val industryCode: String,
    val industryName: String,
    val addedDate: String
)
