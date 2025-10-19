package com.hm.cursorpracticetwse.ui.industry

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.cursorpracticetwse.domain.model.Industry
import com.hm.cursorpracticetwse.domain.usecase.GetIndustriesUseCase
import com.hm.cursorpracticetwse.ui.state.UiState
import com.hm.cursorpracticetwse.ui.state.getDataOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Industry Category ViewModel
 * 
 * 負責處理產業分類畫面的業務邏輯
 * 包括產業分類載入、搜尋和導航
 */
@HiltViewModel
class IndustryCategoryViewModel @Inject constructor(
    private val getIndustriesUseCase: GetIndustriesUseCase
) : ViewModel() {
    
    /**
     * 產業分類 UI 狀態
     */
    private val _uiState = MutableStateFlow<UiState<List<Industry>>>(UiState.Initial)
    val uiState: StateFlow<UiState<List<Industry>>> = _uiState.asStateFlow()
    
    /**
     * 搜尋關鍵字
     */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    /**
     * 篩選後的產業分類列表
     */
    private val _filteredIndustries = MutableStateFlow<List<Industry>>(emptyList())
    val filteredIndustries: StateFlow<List<Industry>> = _filteredIndustries.asStateFlow()
    
    /**
     * 是否正在載入
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
     */
    fun loadIndustries() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                Log.d("IndustryCategoryViewModel", "twse] Starting to fetch industries...")
                val result = getIndustriesUseCase()
                
                _isLoading.value = false
                
                when {
                    result.isSuccess -> {
                        val industries = result.getOrNull() ?: emptyList()
                        Log.d("IndustryCategoryViewModel", "twse] Industries loaded successfully, count: ${industries.size}")
                        
                        _uiState.value = UiState.Success(industries)
                        _filteredIndustries.value = industries
                    }
                    result.isFailure -> {
                        val error = result.exceptionOrNull()
                        val errorMessage = error?.message ?: "載入產業分類失敗"
                        Log.e("IndustryCategoryViewModel", "twse] Error fetching industries: $errorMessage")
                        _errorMessage.value = errorMessage
                        _uiState.value = UiState.Error(errorMessage, error)
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
                val errorMessage = e.message ?: "載入產業分類失敗"
                Log.e("IndustryCategoryViewModel", "twse] Exception in loadIndustries: $errorMessage", e)
                _errorMessage.value = errorMessage
                _uiState.value = UiState.Error(errorMessage, e)
            }
        }
    }
    
    /**
     * 更新搜尋關鍵字
     * 
     * @param query 搜尋關鍵字
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterIndustries()
    }
    
    /**
     * 篩選產業分類
     */
    private fun filterIndustries() {
        val currentIndustries = _uiState.value.getDataOrNull() ?: emptyList()
        val query = _searchQuery.value.trim()
        
        val filtered = if (query.isEmpty()) {
            currentIndustries
        } else {
            currentIndustries.filter { industry ->
                industry.name.contains(query, ignoreCase = true) ||
                industry.code.contains(query, ignoreCase = true)
            }
        }
        
        _filteredIndustries.value = filtered
    }
    
    /**
     * 重新載入資料
     */
    fun refreshData() {
        _searchQuery.value = ""
        _filteredIndustries.value = emptyList()
        loadIndustries()
    }
    
    /**
     * 清除錯誤訊息
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
}
