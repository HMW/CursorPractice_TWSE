package com.hm.cursorpracticetwse.ui.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.cursorpracticetwse.domain.usecase.GetCompaniesUseCase
import com.hm.cursorpracticetwse.domain.usecase.GetIndustriesUseCase
import com.hm.cursorpracticetwse.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Launch Screen ViewModel
 * 
 * 負責處理 Launch Screen 的業務邏輯
 * 包括資料載入、狀態管理和導航邏輯
 */
@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val getCompaniesUseCase: GetCompaniesUseCase,
    private val getIndustriesUseCase: GetIndustriesUseCase
) : ViewModel() {
    
    /**
     * Launch Screen 的 UI 狀態
     */
    private val _uiState = MutableStateFlow<LaunchUiState>(LaunchUiState.Initial)
    val uiState: StateFlow<LaunchUiState> = _uiState.asStateFlow()
    
    /**
     * 載入狀態
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    /**
     * 錯誤訊息
     */
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    /**
     * 初始化資料載入
     * 
     * 在 Launch Screen 顯示時自動載入必要的資料
     */
    fun initializeData() {
        if (_uiState.value is LaunchUiState.Initial) {
            loadInitialData()
        }
    }
    
    /**
     * 載入初始資料
     * 
     * 同時載入公司資料和產業分類
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                // 載入公司資料
                val companiesResult = getCompaniesUseCase()
                val industriesResult = getIndustriesUseCase()
                
                // 等待兩個請求完成
                var companiesLoaded = false
                var industriesLoaded = false
                var companies: List<com.hm.cursorpracticetwse.domain.model.Company>? = null
                var industries: List<com.hm.cursorpracticetwse.domain.model.Industry>? = null
                
                // 監聽公司資料載入
                companiesResult.collect { result ->
                    when {
                        result.isSuccess -> {
                            companies = result.getOrNull()
                            companiesLoaded = true
                        }
                        result.isFailure -> {
                            _errorMessage.value = "載入公司資料失敗: ${result.exceptionOrNull()?.message}"
                            companiesLoaded = true
                        }
                    }
                    
                    if (companiesLoaded && industriesLoaded) {
                        _uiState.value = LaunchUiState.DataLoaded(
                            companies = companies ?: emptyList(),
                            industries = industries ?: emptyList()
                        )
                        _isLoading.value = false
                    }
                }
                
                // 監聽產業分類載入
                industriesResult.collect { result ->
                    when {
                        result.isSuccess -> {
                            industries = result.getOrNull()
                            industriesLoaded = true
                        }
                        result.isFailure -> {
                            _errorMessage.value = "載入產業分類失敗: ${result.exceptionOrNull()?.message}"
                            industriesLoaded = true
                        }
                    }
                    
                    if (companiesLoaded && industriesLoaded) {
                        _uiState.value = LaunchUiState.DataLoaded(
                            companies = companies ?: emptyList(),
                            industries = industries ?: emptyList()
                        )
                        _isLoading.value = false
                    }
                }
                
            } catch (e: Exception) {
                _uiState.value = LaunchUiState.Error(e.message ?: "未知錯誤")
                _isLoading.value = false
                _errorMessage.value = e.message
            }
        }
    }
    
    /**
     * 重新載入資料
     * 
     * 當使用者手動重新整理時調用
     */
    fun refreshData() {
        _uiState.value = LaunchUiState.Initial
        loadInitialData()
    }
    
    /**
     * 清除錯誤訊息
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
    /**
     * 導航到主畫面
     * 
     * 當資料載入完成後調用
     */
    fun navigateToMain() {
        if (_uiState.value is LaunchUiState.DataLoaded) {
            _uiState.value = LaunchUiState.NavigateToMain
        }
    }
}

/**
 * Launch Screen 的 UI 狀態
 */
sealed class LaunchUiState {
    
    /**
     * 初始狀態
     */
    object Initial : LaunchUiState()
    
    /**
     * 資料載入完成
     * 
     * @param companies 公司資料列表
     * @param industries 產業分類列表
     */
    data class DataLoaded(
        val companies: List<com.hm.cursorpracticetwse.domain.model.Company>,
        val industries: List<com.hm.cursorpracticetwse.domain.model.Industry>
    ) : LaunchUiState()
    
    /**
     * 錯誤狀態
     * 
     * @param message 錯誤訊息
     */
    data class Error(val message: String) : LaunchUiState()
    
    /**
     * 導航到主畫面
     */
    object NavigateToMain : LaunchUiState()
}
