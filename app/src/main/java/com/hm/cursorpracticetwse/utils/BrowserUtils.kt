package com.hm.cursorpracticetwse.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

/**
 * 瀏覽器工具類
 * 
 * 負責處理瀏覽器開啟相關操作
 */
object BrowserUtils {
    
    /**
     * 開啟 URL 在瀏覽器中
     * 
     * @param context Android Context
     * @param urlString URL 字串
     * @return 是否成功開啟
     */
    fun openUrlInBrowser(context: Context, urlString: String): Boolean {
        return try {
            if (urlString.isBlank()) {
                Log.w("BrowserUtils", "twse] URL is blank")
                return false
            }
            
            val formattedUrl = UrlUtils.formatUrl(urlString)
            if (formattedUrl == null) {
                Log.w("BrowserUtils", "twse] Invalid URL format: $urlString")
                return false
            }
            
            val uri = Uri.parse(formattedUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            
            // 檢查是否有應用程式可以處理這個 Intent
//            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                Log.d("BrowserUtils", "twse] Successfully opened URL: $formattedUrl")
                true
//            } else {
//                Log.w("BrowserUtils", "twse] No app can handle URL: $formattedUrl")
//                false
//            }
        } catch (e: Exception) {
            Log.e("BrowserUtils", "twse] Failed to open URL: $urlString", e)
            false
        }
    }
    
    /**
     * 開啟安全的 HTTPS URL
     * 
     * @param context Android Context
     * @param urlString 原始 URL 字串
     * @return 是否成功開啟
     */
    fun openSecureUrlInBrowser(context: Context, urlString: String): Boolean {
        return try {
            val secureUrl = UrlUtils.getSecureUrl(urlString)
            if (secureUrl == null) {
                Log.w("BrowserUtils", "twse] Invalid URL for secure opening: $urlString")
                return false
            }
            
            openUrlInBrowser(context, secureUrl)
        } catch (e: Exception) {
            Log.e("BrowserUtils", "twse] Failed to open secure URL: $urlString", e)
            false
        }
    }
    
    /**
     * 檢查是否有瀏覽器可以開啟 URL
     * 
     * @param context Android Context
     * @param urlString URL 字串
     * @return 是否有瀏覽器可以處理
     */
    fun canOpenUrl(context: Context, urlString: String): Boolean {
        return try {
            val formattedUrl = UrlUtils.formatUrl(urlString)
            if (formattedUrl == null) {
                return false
            }
            
            val uri = Uri.parse(formattedUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.resolveActivity(context.packageManager) != null
        } catch (e: Exception) {
            Log.e("BrowserUtils", "twse] Failed to check if URL can be opened: $urlString", e)
            false
        }
    }
}
