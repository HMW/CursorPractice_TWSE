package com.hm.cursorpracticetwse.ui.launch

import android.util.Log
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
        Log.d("LaunchViewModel", "twse] initializeData() called, current state: ${_uiState.value}")
        if (_uiState.value is LaunchUiState.Initial) {
            Log.d("LaunchViewModel", "twse] Starting to load initial data...")
            loadInitialData()
        } else {
            Log.d("LaunchViewModel", "twse] Already loading or loaded, current state: ${_uiState.value}")
        }
    }
    
    /**
     * 載入初始資料
     * 
     * 同時載入公司資料和產業分類
     */
    private fun loadInitialData() {
        Log.d("LaunchViewModel", "twse] loadInitialData() started")
        viewModelScope.launch {
            Log.d("LaunchViewModel", "twse] Setting loading state to true")
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                Log.d("LaunchViewModel", "twse] Starting to fetch companies and industries...")
                // 載入公司資料和產業分類
                Log.d("LaunchViewModel", "twse] Use cases called, starting to collect results...")
                
                // 並行執行兩個請求
                val companiesResult = getCompaniesUseCase()
                val industriesResult = getIndustriesUseCase()
                
                Log.d("LaunchViewModel", "twse] Companies result received: ${companiesResult.isSuccess}")
                Log.d("LaunchViewModel", "twse] Industries result received: ${industriesResult.isSuccess}")
                
                when {
                    companiesResult.isSuccess && industriesResult.isSuccess -> {
                        val companies = companiesResult.getOrNull() ?: emptyList()
                        val industries = industriesResult.getOrNull() ?: emptyList()
                        Log.d("LaunchViewModel", "twse] Both companies and industries loaded successfully")
                        Log.d("LaunchViewModel", "twse] Companies count: ${companies.size}, Industries count: ${industries.size}")
                        _uiState.value = LaunchUiState.DataLoaded(
                            companies = companies,
                            industries = industries
                        )
                        _isLoading.value = false
                        
                        // 自動觸發導航到主畫面
                        Log.d("LaunchViewModel", "twse] Auto-navigating to main screen")
                        navigateToMain()
                    }
                    companiesResult.isFailure -> {
                        val error = companiesResult.exceptionOrNull()?.message
                        Log.e("LaunchViewModel", "twse] Companies loading failed: $error")
                        _errorMessage.value = "載入公司資料失敗: $error"
                        _uiState.value = LaunchUiState.Error(error ?: "載入公司資料失敗")
                        _isLoading.value = false
                    }
                    industriesResult.isFailure -> {
                        val error = industriesResult.exceptionOrNull()?.message
                        Log.e("LaunchViewModel", "twse] Industries loading failed: $error")
                        _errorMessage.value = "載入產業分類失敗: $error"
                        _uiState.value = LaunchUiState.Error(error ?: "載入產業分類失敗")
                        _isLoading.value = false
                    }
                }
                
            } catch (e: Exception) {
                Log.e("LaunchViewModel", "twse] Exception in loadInitialData: ${e.message}", e)
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
        Log.d("LaunchViewModel", "twse] navigateToMain() called, current state: ${_uiState.value}")
        if (_uiState.value is LaunchUiState.DataLoaded) {
            Log.d("LaunchViewModel", "twse] Setting state to NavigateToMain")
            _uiState.value = LaunchUiState.NavigateToMain
        } else {
            Log.d("LaunchViewModel", "twse] Cannot navigate, current state is not DataLoaded: ${_uiState.value}")
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
