package com.hm.cursorpracticetwse.ui.company

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.model.Industry
import com.hm.cursorpracticetwse.domain.usecase.GetCompaniesByIndustryUseCase
import com.hm.cursorpracticetwse.ui.state.UiState
import com.hm.cursorpracticetwse.ui.state.getDataOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Company List ViewModel
 * 
 * 負責處理公司列表畫面的業務邏輯
 * 包括公司資料載入、搜尋和篩選
 */
@HiltViewModel
class CompanyListViewModel @Inject constructor(
    private val getCompaniesByIndustryUseCase: GetCompaniesByIndustryUseCase
) : ViewModel() {
    
    /**
     * 公司列表 UI 狀態
     */
    private val _uiState = MutableStateFlow<UiState<List<Company>>>(UiState.Initial)
    val uiState: StateFlow<UiState<List<Company>>> = _uiState.asStateFlow()
    
    
    /**
     * 搜尋關鍵字
     */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    /**
     * 篩選後的公司列表
     */
    private val _filteredCompanies = MutableStateFlow<List<Company>>(emptyList())
    val filteredCompanies: StateFlow<List<Company>> = _filteredCompanies.asStateFlow()
    
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
     * 載入指定產業的公司列表
     * 
     * @param industry 產業分類
     */
    fun loadCompaniesByIndustry(industry: Industry) {
        _searchQuery.value = ""
        _filteredCompanies.value = emptyList()
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                Log.d("CompanyListViewModel", "twse] Starting to fetch companies for industry: ${industry.code}")
                val result = getCompaniesByIndustryUseCase(industry.code)
                
                _isLoading.value = false
                
                when {
                    result.isSuccess -> {
                        val companies = result.getOrNull() ?: emptyList()
                        Log.d("CompanyListViewModel", "twse] Companies loaded successfully, count: ${companies.size}")
                        _uiState.value = UiState.Success(companies)
                        _filteredCompanies.value = companies
                    }
                    result.isFailure -> {
                        val error = result.exceptionOrNull()
                        val errorMessage = error?.message ?: "載入公司資料失敗"
                        Log.e("CompanyListViewModel", "twse] Error fetching companies: $errorMessage")
                        _errorMessage.value = errorMessage
                        _uiState.value = UiState.Error(errorMessage, error)
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
                val errorMessage = e.message ?: "載入公司資料失敗"
                Log.e("CompanyListViewModel", "twse] Exception in loadCompaniesByIndustry: $errorMessage", e)
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
        filterCompanies()
    }
    
    /**
     * 篩選公司列表
     */
    private fun filterCompanies() {
        val currentCompanies = _uiState.value.getDataOrNull() ?: emptyList()
        val query = _searchQuery.value.trim()
        
        val filtered = if (query.isEmpty()) {
            currentCompanies
        } else {
            currentCompanies.filter { company ->
                company.公司名稱.contains(query, ignoreCase = true) ||
                company.公司代號.contains(query, ignoreCase = true) ||
                company.公司簡稱.contains(query, ignoreCase = true)
            }
        }
        
        _filteredCompanies.value = filtered
    }
    
    /**
     * 重新載入資料
     */
    fun refreshData(industry: Industry) {
        loadCompaniesByIndustry(industry)
    }
    
    /**
     * 清除錯誤訊息
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
}
