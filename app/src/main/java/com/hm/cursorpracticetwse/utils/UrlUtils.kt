package com.hm.cursorpracticetwse.utils

import android.util.Log
import java.net.URL

/**
 * URL 工具類
 * 
 * 負責處理 URL 驗證、格式化和相關操作
 */
object UrlUtils {
    
    /**
     * 驗證 URL 是否有效
     * 
     * @param urlString URL 字串
     * @return 是否為有效的 URL
     */
    fun isValidUrl(urlString: String): Boolean {
        return try {
            if (urlString.isBlank()) {
                return false
            }
            
            // 如果沒有協議，添加 https://
            val url = if (urlString.startsWith("http://") || urlString.startsWith("https://")) {
                urlString
            } else {
                "https://$urlString"
            }
            
            URL(url)
            true
        } catch (e: Exception) {
            Log.d("UrlUtils", "twse] Invalid URL: $urlString", e)
            false
        }
    }
    
    /**
     * 格式化 URL，確保有正確的協議
     * 
     * @param urlString 原始 URL 字串
     * @return 格式化後的 URL，如果無效則返回 null
     */
    fun formatUrl(urlString: String): String? {
        if (urlString.isBlank()) {
            return null
        }
        
        return try {
            val trimmedUrl = urlString.trim()
            
            // 如果已經有協議，直接返回
            if (trimmedUrl.startsWith("http://") || trimmedUrl.startsWith("https://")) {
                trimmedUrl
            } else {
                // 添加 https:// 協議
                "https://$trimmedUrl"
            }
        } catch (e: Exception) {
            Log.e("UrlUtils", "twse] Failed to format URL: $urlString", e)
            null
        }
    }
    
    /**
     * 取得網站的顯示名稱
     * 
     * @param urlString URL 字串
     * @return 網站顯示名稱
     */
    fun getDisplayName(urlString: String): String {
        return try {
            val url = formatUrl(urlString) ?: return "網站"
            val urlObj = URL(url)
            val host = urlObj.host
            
            // 移除 www. 前綴
            if (host.startsWith("www.")) {
                host.substring(4)
            } else {
                host
            }
        } catch (e: Exception) {
            Log.e("UrlUtils", "twse] Failed to get display name for URL: $urlString", e)
            "網站"
        }
    }
    
    /**
     * 檢查 URL 是否為 HTTPS
     * 
     * @param urlString URL 字串
     * @return 是否為 HTTPS
     */
    fun isHttps(urlString: String): Boolean {
        return urlString.startsWith("https://")
    }
    
    /**
     * 取得安全的 URL（強制使用 HTTPS）
     * 
     * @param urlString 原始 URL 字串
     * @return 安全的 HTTPS URL
     */
    fun getSecureUrl(urlString: String): String? {
        val formattedUrl = formatUrl(urlString) ?: return null
        
        return if (isHttps(formattedUrl)) {
            formattedUrl
        } else {
            // 將 http:// 替換為 https://
            formattedUrl.replace("http://", "https://")
        }
    }
}
