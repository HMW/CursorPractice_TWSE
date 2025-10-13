package com.hm.cursorpracticetwse.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * 公司資料傳輸物件 (DTO)
 * 
 * 對應 TWSE API 回應的 JSON 結構
 * 使用 Gson 序列化註解處理中文字段名稱
 */
data class CompanyDto(
    @SerializedName("出表日期")
    val 出表日期: String,
    
    @SerializedName("公司代號")
    val 公司代號: String,
    
    @SerializedName("公司名稱")
    val 公司名稱: String,
    
    @SerializedName("公司簡稱")
    val 公司簡稱: String,
    
    @SerializedName("外國企業註冊地國")
    val 外國企業註冊地國: String,
    
    @SerializedName("產業別")
    val 產業別: String,
    
    @SerializedName("住址")
    val 住址: String,
    
    @SerializedName("營利事業統一編號")
    val 營利事業統一編號: String,
    
    @SerializedName("董事長")
    val 董事長: String,
    
    @SerializedName("總經理")
    val 總經理: String,
    
    @SerializedName("發言人")
    val 發言人: String,
    
    @SerializedName("發言人職稱")
    val 發言人職稱: String,
    
    @SerializedName("代理發言人")
    val 代理發言人: String,
    
    @SerializedName("總機電話")
    val 總機電話: String,
    
    @SerializedName("成立日期")
    val 成立日期: String,
    
    @SerializedName("上市日期")
    val 上市日期: String,
    
    @SerializedName("普通股每股面額")
    val 普通股每股面額: String,
    
    @SerializedName("實收資本額")
    val 實收資本額: String,
    
    @SerializedName("已發行普通股數或TDR原股發行股數")
    val 已發行普通股數或TDR原股發行股數: String,
    
    @SerializedName("私募股數")
    val 私募股數: String,
    
    @SerializedName("特別股")
    val 特別股: String,
    
    @SerializedName("編製財務報告類型")
    val 編製財務報告類型: String,
    
    @SerializedName("股票過戶機構")
    val 股票過戶機構: String,
    
    @SerializedName("過戶電話")
    val 過戶電話: String,
    
    @SerializedName("過戶地址")
    val 過戶地址: String,
    
    @SerializedName("簽證會計師事務所")
    val 簽證會計師事務所: String,
    
    @SerializedName("簽證會計師1")
    val 簽證會計師1: String,
    
    @SerializedName("簽證會計師2")
    val 簽證會計師2: String,
    
    @SerializedName("英文簡稱")
    val 英文簡稱: String,
    
    @SerializedName("英文通訊地址")
    val 英文通訊地址: String,
    
    @SerializedName("傳真機號碼")
    val 傳真機號碼: String,
    
    @SerializedName("電子郵件信箱")
    val 電子郵件信箱: String,
    
    @SerializedName("網址")
    val 網址: String
)
