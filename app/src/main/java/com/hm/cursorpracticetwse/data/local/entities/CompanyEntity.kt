package com.hm.cursorpracticetwse.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 公司資料庫實體
 * 
 * 對應 Room Database 中的 companies 表格
 * 使用 公司代號 作為主鍵
 */
@Entity(tableName = "companies")
data class CompanyEntity(
    @PrimaryKey
    val 公司代號: String,
    
    val 出表日期: String,
    val 公司名稱: String,
    val 公司簡稱: String,
    val 外國企業註冊地國: String,
    val 產業別: String,
    val 住址: String,
    val 營利事業統一編號: String,
    val 董事長: String,
    val 總經理: String,
    val 發言人: String,
    val 發言人職稱: String,
    val 代理發言人: String,
    val 總機電話: String,
    val 成立日期: String,
    val 上市日期: String,
    val 普通股每股面額: String,
    val 實收資本額: String,
    val 已發行普通股數或TDR原股發行股數: String,
    val 私募股數: String,
    val 特別股: String,
    val 編製財務報告類型: String,
    val 股票過戶機構: String,
    val 過戶電話: String,
    val 過戶地址: String,
    val 簽證會計師事務所: String,
    val 簽證會計師1: String,
    val 簽證會計師2: String,
    val 英文簡稱: String,
    val 英文通訊地址: String,
    val 傳真機號碼: String,
    val 電子郵件信箱: String,
    val 網址: String
)
