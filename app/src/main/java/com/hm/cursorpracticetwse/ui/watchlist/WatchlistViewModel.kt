package com.hm.cursorpracticetwse.ui.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.cursorpracticetwse.domain.model.WatchlistItem
import com.hm.cursorpracticetwse.domain.usecase.GetWatchlistUseCase
import com.hm.cursorpracticetwse.domain.usecase.RemoveFromWatchlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Watchlist ViewModel
 * 
 * 負責處理追蹤列表畫面的業務邏輯
 * 包括追蹤列表載入、搜尋和移除功能
 */
@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val getWatchlistUseCase: GetWatchlistUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase
) : ViewModel() {
    
    /**
     * 追蹤列表 UI 狀態
     */
    private val _uiState = MutableStateFlow<WatchlistUiState>(WatchlistUiState.Initial)
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()
    
    /**
     * 搜尋關鍵字
     */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    /**
     * 篩選後的追蹤列表
     */
    private val _filteredWatchlist = MutableStateFlow<List<WatchlistItem>>(emptyList())
    val filteredWatchlist: StateFlow<List<WatchlistItem>> = _filteredWatchlist.asStateFlow()
    
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
     * 載入追蹤列表
     */
    fun loadWatchlist() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val result = getWatchlistUseCase()
                if (result.isSuccess) {
                    val watchlist = result.getOrNull() ?: emptyList()
                    _uiState.value = WatchlistUiState.Success(watchlist)
                    updateFilteredWatchlist(watchlist)
                } else {
                    _uiState.value = WatchlistUiState.Error(result.exceptionOrNull()?.message ?: "載入追蹤列表失敗")
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "載入追蹤列表失敗"
                }
            } catch (e: Exception) {
                _uiState.value = WatchlistUiState.Error(e.message ?: "載入追蹤列表失敗")
                _errorMessage.value = e.message ?: "載入追蹤列表失敗"
            } finally {
                _isLoading.value = false
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
        val currentState = _uiState.value
        if (currentState is WatchlistUiState.Success) {
            updateFilteredWatchlist(currentState.watchlist)
        }
    }
    
    /**
     * 更新篩選後的追蹤列表
     * 
     * @param watchlist 原始追蹤列表
     */
    private fun updateFilteredWatchlist(watchlist: List<WatchlistItem>) {
        val query = _searchQuery.value.lowercase()
        val filtered = if (query.isBlank()) {
            watchlist
        } else {
            watchlist.filter { item ->
                item.companyCode.lowercase().contains(query) ||
                item.companyName.lowercase().contains(query) ||
                item.companyShortName.lowercase().contains(query) ||
                item.industryName.lowercase().contains(query)
            }
        }
        _filteredWatchlist.value = filtered
    }
    
    /**
     * 從追蹤列表移除項目
     * 
     * @param item 要移除的追蹤項目
     */
    fun removeFromWatchlist(item: WatchlistItem) {
        viewModelScope.launch {
            try {
                val result = removeFromWatchlistUseCase(item.companyCode)
                if (result.isSuccess) {
                    // 重新載入追蹤列表
                    loadWatchlist()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "移除失敗"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "移除失敗"
            }
        }
    }
    
    /**
     * 重新載入資料
     */
    fun refreshData() {
        loadWatchlist()
    }
    
    /**
     * 清除錯誤訊息
     */
    fun clearError() {
        _errorMessage.value = null
    }
}

/**
 * Watchlist UI 狀態
 */
sealed class WatchlistUiState {
    object Initial : WatchlistUiState()
    object Loading : WatchlistUiState()
    data class Success(val watchlist: List<WatchlistItem>) : WatchlistUiState()
    data class Error(val message: String) : WatchlistUiState()
}
