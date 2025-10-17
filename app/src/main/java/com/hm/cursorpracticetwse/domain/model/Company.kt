package com.hm.cursorpracticetwse.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 公司領域模型
 * 
 * 代表台灣證券交易所的公開發行公司基本資料
 * 包含公司代號、名稱、產業別、地址等 40+ 個欄位
 */
@Parcelize
data class Company(
    val 出表日期: String,
    val 公司代號: String,
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
) : Parcelable {
    
    /**
     * 取得公司顯示名稱
     * 優先使用公司簡稱，如果為空則使用公司名稱
     */
    fun getDisplayName(): String {
        return if (公司簡稱.isNotBlank()) 公司簡稱 else 公司名稱
    }
    
    /**
     * 檢查是否為上市公司
     */
    fun isListed(): Boolean {
        return 上市日期.isNotBlank()
    }
    
    /**
     * 取得產業別代碼
     * 從產業別字串中提取數字代碼
     */
    fun getIndustryCode(): String {
        val parts = 產業別.split(":")
        return if (parts.size >= 2) parts[0] else ""
    }
    
    /**
     * 取得產業別名稱
     * 根據產業別代碼對應到實際的產業名稱
     */
    fun getIndustryName(): String {
        return getIndustryNameByCode(產業別)
    }
    
    /**
     * 根據產業別代碼取得產業名稱
     */
    private fun getIndustryNameByCode(code: String): String {
        return when (code) {
            "01" -> "水泥工業"
            "02" -> "食品工業"
            "03" -> "塑膠工業"
            "04" -> "紡織纖維"
            "05" -> "電機機械"
            "06" -> "電器電纜"
            "08" -> "玻璃陶瓷"
            "09" -> "造紙工業"
            "10" -> "鋼鐵工業"
            "11" -> "橡膠工業"
            "12" -> "汽車工業"
            "14" -> "建材營造"
            "15" -> "航運業"
            "16" -> "觀光餐旅"
            "17" -> "金融保險"
            "18" -> "貿易百貨"
            "19" -> "綜合"
            "20" -> "其他"
            "21" -> "化學工業"
            "22" -> "生技醫療業"
            "23" -> "油電燃氣業"
            "24" -> "半導體業"
            "25" -> "電腦及週邊設備業"
            "26" -> "光電業"
            "27" -> "通信網路業"
            "28" -> "電子零組件業"
            "29" -> "電子通路業"
            "30" -> "資訊服務業"
            "31" -> "其他電子業"
            "32" -> "文化創意業"
            "33" -> "農業科技業"
            "34" -> "電子商務"
            "35" -> "綠能環保"
            "36" -> "數位雲端"
            "37" -> "運動休閒"
            "38" -> "居家生活"
            "80" -> "管理股票"
            "98" -> "期貨"
            "XX" -> "證券"
            else -> "未知產業"
        }
    }
}
