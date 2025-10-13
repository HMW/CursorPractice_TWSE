package com.hm.cursorpracticetwse.ui.state

import org.junit.Assert.*
import org.junit.Test

/**
 * UiState 測試
 */
class UiStateTest {
    
    @Test
    fun `isLoading should return true for Loading state`() {
        // Given
        val state: UiState<String> = UiState.Loading
        
        // When
        val result = state.isLoading()
        
        // Then
        assertTrue(result)
    }
    
    @Test
    fun `isLoading should return false for non-Loading state`() {
        // Given
        val state: UiState<String> = UiState.Success("test")
        
        // When
        val result = state.isLoading()
        
        // Then
        assertFalse(result)
    }
    
    @Test
    fun `isSuccess should return true for Success state`() {
        // Given
        val state: UiState<String> = UiState.Success("test")
        
        // When
        val result = state.isSuccess()
        
        // Then
        assertTrue(result)
    }
    
    @Test
    fun `isSuccess should return false for non-Success state`() {
        // Given
        val state: UiState<String> = UiState.Loading
        
        // When
        val result = state.isSuccess()
        
        // Then
        assertFalse(result)
    }
    
    @Test
    fun `isError should return true for Error state`() {
        // Given
        val state: UiState<String> = UiState.Error("test error")
        
        // When
        val result = state.isError()
        
        // Then
        assertTrue(result)
    }
    
    @Test
    fun `isError should return false for non-Error state`() {
        // Given
        val state: UiState<String> = UiState.Success("test")
        
        // When
        val result = state.isError()
        
        // Then
        assertFalse(result)
    }
    
    @Test
    fun `getDataOrNull should return data for Success state`() {
        // Given
        val expectedData = "test data"
        val state: UiState<String> = UiState.Success(expectedData)
        
        // When
        val result = state.getDataOrNull()
        
        // Then
        assertEquals(expectedData, result)
    }
    
    @Test
    fun `getDataOrNull should return null for non-Success state`() {
        // Given
        val state: UiState<String> = UiState.Loading
        
        // When
        val result = state.getDataOrNull()
        
        // Then
        assertNull(result)
    }
    
    @Test
    fun `getErrorMessageOrNull should return message for Error state`() {
        // Given
        val expectedMessage = "test error"
        val state: UiState<String> = UiState.Error(expectedMessage)
        
        // When
        val result = state.getErrorMessageOrNull()
        
        // Then
        assertEquals(expectedMessage, result)
    }
    
    @Test
    fun `getErrorMessageOrNull should return null for non-Error state`() {
        // Given
        val state: UiState<String> = UiState.Success("test")
        
        // When
        val result = state.getErrorMessageOrNull()
        
        // Then
        assertNull(result)
    }
}
