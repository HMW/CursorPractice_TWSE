package com.hm.cursorpracticetwse.ui.state

/**
 * UI 狀態管理
 * 
 * 定義應用程式中各種 UI 狀態的通用介面
 * 使用 sealed class 來表示不同的狀態
 */

/**
 * 通用的 UI 狀態
 * 
 * @param T 資料類型
 */
sealed class UiState<out T> {
    
    /**
     * 初始狀態
     */
    object Initial : UiState<Nothing>()
    
    /**
     * 載入中狀態
     */
    object Loading : UiState<Nothing>()
    
    /**
     * 成功狀態
     * 
     * @param data 成功載入的資料
     */
    data class Success<T>(val data: T) : UiState<T>()
    
    /**
     * 錯誤狀態
     * 
     * @param message 錯誤訊息
     * @param throwable 例外物件（可選）
     */
    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : UiState<Nothing>()
}

/**
 * 載入狀態的擴展函數
 */
fun <T> UiState<T>.isLoading(): Boolean = this is UiState.Loading

/**
 * 成功狀態的擴展函數
 */
fun <T> UiState<T>.isSuccess(): Boolean = this is UiState.Success

/**
 * 錯誤狀態的擴展函數
 */
fun <T> UiState<T>.isError(): Boolean = this is UiState.Error

/**
 * 取得成功資料的擴展函數
 */
fun <T> UiState<T>.getDataOrNull(): T? = when (this) {
    is UiState.Success -> data
    else -> null
}

/**
 * 取得錯誤訊息的擴展函數
 */
fun <T> UiState<T>.getErrorMessageOrNull(): String? = when (this) {
    is UiState.Error -> message
    else -> null
}
