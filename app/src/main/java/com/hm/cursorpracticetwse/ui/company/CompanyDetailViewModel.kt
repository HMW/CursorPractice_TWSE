package com.hm.cursorpracticetwse.ui.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.cursorpracticetwse.domain.model.Company
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Company Detail ViewModel
 * 
 * 負責處理公司詳細資料畫面的業務邏輯
 * 包括公司資料載入、追蹤功能等
 */
@HiltViewModel
class CompanyDetailViewModel @Inject constructor(
    // TODO: 注入追蹤列表相關的 Use Case
) : ViewModel() {
    
    /**
     * UI 狀態
     */
    private val _uiState = MutableStateFlow<CompanyDetailUiState>(CompanyDetailUiState.Initial)
    val uiState: StateFlow<CompanyDetailUiState> = _uiState.asStateFlow()
    
    /**
     * 是否已追蹤
     */
    private val _isWatched = MutableStateFlow(false)
    val isWatched: StateFlow<Boolean> = _isWatched.asStateFlow()
    
    /**
     * 載入公司詳細資料
     * 
     * @param company 公司資料
     */
    fun loadCompanyDetail(company: Company) {
        viewModelScope.launch {
            _uiState.value = CompanyDetailUiState.Loading
            
            try {
                // TODO: 檢查是否在追蹤列表中
                // val isInWatchlist = checkIfInWatchlist(company)
                // _isWatched.value = isInWatchlist
                
                _uiState.value = CompanyDetailUiState.Success(company)
            } catch (e: Exception) {
                _uiState.value = CompanyDetailUiState.Error(e.message ?: "載入公司資料失敗")
            }
        }
    }
    
    /**
     * 切換追蹤狀態
     */
    fun toggleWatchlist() {
        val currentState = _uiState.value
        if (currentState is CompanyDetailUiState.Success) {
            _uiState.value = currentState.copy(showWatchlistDialog = true)
        }
    }
    
    /**
     * 確認追蹤狀態切換
     */
    fun confirmWatchlistToggle() {
        viewModelScope.launch {
            try {
                val currentWatched = _isWatched.value
                
                if (currentWatched) {
                    // TODO: 從追蹤列表中移除
                    // removeFromWatchlist()
                    _isWatched.value = false
                } else {
                    // TODO: 加入追蹤列表
                    // addToWatchlist()
                    _isWatched.value = true
                }
                
                val currentState = _uiState.value
                if (currentState is CompanyDetailUiState.Success) {
                    _uiState.value = currentState.copy(showWatchlistDialog = false)
                }
            } catch (e: Exception) {
                // 處理錯誤
                val currentState = _uiState.value
                if (currentState is CompanyDetailUiState.Success) {
                    _uiState.value = currentState.copy(showWatchlistDialog = false)
                }
            }
        }
    }
    
    /**
     * 關閉追蹤對話框
     */
    fun dismissWatchlistDialog() {
        val currentState = _uiState.value
        if (currentState is CompanyDetailUiState.Success) {
            _uiState.value = currentState.copy(showWatchlistDialog = false)
        }
    }
}

/**
 * Company Detail UI 狀態
 */
sealed class CompanyDetailUiState {
    object Initial : CompanyDetailUiState()
    object Loading : CompanyDetailUiState()
    data class Success(val company: Company, val showWatchlistDialog: Boolean = false) : CompanyDetailUiState()
    data class Error(val message: String) : CompanyDetailUiState()
}
