package com.hm.cursorpracticetwse.ui.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.usecase.AddToWatchlistUseCase
import com.hm.cursorpracticetwse.domain.usecase.RemoveFromWatchlistUseCase
import com.hm.cursorpracticetwse.domain.usecase.IsInWatchlistUseCase
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
    private val addToWatchlistUseCase: AddToWatchlistUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase,
    private val isInWatchlistUseCase: IsInWatchlistUseCase
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
                // 檢查是否在追蹤列表中
                val isInWatchlistResult = isInWatchlistUseCase(company.公司代號)
                if (isInWatchlistResult.isSuccess) {
                    _isWatched.value = isInWatchlistResult.getOrNull() ?: false
                }
                
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
                val currentState = _uiState.value
                if (currentState is CompanyDetailUiState.Success) {
                    val company = currentState.company
                    val currentWatched = _isWatched.value
                    
                    if (currentWatched) {
                        // 從追蹤列表中移除
                        val result = removeFromWatchlistUseCase(company.公司代號)
                        if (result.isSuccess) {
                            _isWatched.value = false
                        }
                    } else {
                        // 加入追蹤列表
                        val result = addToWatchlistUseCase(company)
                        if (result.isSuccess) {
                            _isWatched.value = true
                        }
                    }
                    
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
