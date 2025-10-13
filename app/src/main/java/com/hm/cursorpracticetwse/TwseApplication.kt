package com.hm.cursorpracticetwse

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * TWSE Android Application
 * 
 * 使用 Hilt 進行依賴注入的 Application 類別
 * @HiltAndroidApp 標註會觸發 Hilt 的程式碼生成
 */
@HiltAndroidApp
class TwseApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 初始化應用程式
        // Hilt 會自動處理依賴注入的初始化
    }
}
