package com.hm.cursorpracticetwse.ui.industry

import app.cash.turbine.test
import com.hm.cursorpracticetwse.domain.model.Industry
import com.hm.cursorpracticetwse.domain.usecase.GetIndustriesUseCase
import com.hm.cursorpracticetwse.ui.state.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * IndustryCategoryViewModel 測試
 */
class IndustryCategoryViewModelTest {
    
    private lateinit var getIndustriesUseCase: GetIndustriesUseCase
    private lateinit var viewModel: IndustryCategoryViewModel
    
    @Before
    fun setUp() {
        getIndustriesUseCase = mockk()
        viewModel = IndustryCategoryViewModel(getIndustriesUseCase)
    }
    
    @Test
    fun `loadIndustries should emit success state when use case returns success`() = runTest {
        // Given
        val industries = listOf(
            Industry("01", "水泥工業", 5),
            Industry("02", "食品工業", 12)
        )
        coEvery { getIndustriesUseCase() } returns flowOf(kotlin.Result.success(industries))
        
        // When
        viewModel.loadIndustries()
        
        // Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState is UiState.Initial)
            
            val loadingState = awaitItem()
            assertTrue(loadingState is UiState.Loading)
            
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            assertEquals(industries, (successState as UiState.Success).data)
            
            awaitComplete()
        }
    }
    
    @Test
    fun `loadIndustries should emit error state when use case returns failure`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { getIndustriesUseCase() } returns flowOf(kotlin.Result.failure(Exception(errorMessage)))
        
        // When
        viewModel.loadIndustries()
        
        // Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState is UiState.Initial)
            
            val loadingState = awaitItem()
            assertTrue(loadingState is UiState.Loading)
            
            val errorState = awaitItem()
            assertTrue(errorState is UiState.Error)
            assertEquals(errorMessage, (errorState as UiState.Error).message)
            
            awaitComplete()
        }
    }
    
    @Test
    fun `updateSearchQuery should filter industries correctly`() = runTest {
        // Given
        val industries = listOf(
            Industry("01", "水泥工業", 5),
            Industry("02", "食品工業", 12),
            Industry("26", "半導體業", 8)
        )
        coEvery { getIndustriesUseCase() } returns flowOf(kotlin.Result.success(industries))
        
        // When
        viewModel.loadIndustries()
        viewModel.updateSearchQuery("水泥")
        
        // Then
        viewModel.filteredIndustries.test {
            val allIndustries = awaitItem()
            assertEquals(industries, allIndustries)
            
            val filteredIndustries = awaitItem()
            assertEquals(1, filteredIndustries.size)
            assertEquals("水泥工業", filteredIndustries[0].name)
            
            awaitComplete()
        }
    }
    
    @Test
    fun `updateSearchQuery should show all industries when query is empty`() = runTest {
        // Given
        val industries = listOf(
            Industry("01", "水泥工業", 5),
            Industry("02", "食品工業", 12)
        )
        coEvery { getIndustriesUseCase() } returns flowOf(kotlin.Result.success(industries))
        
        // When
        viewModel.loadIndustries()
        viewModel.updateSearchQuery("水泥")
        viewModel.updateSearchQuery("")
        
        // Then
        viewModel.filteredIndustries.test {
            val allIndustries = awaitItem()
            assertEquals(industries, allIndustries)
            
            val filteredIndustries = awaitItem()
            assertEquals(1, filteredIndustries.size)
            
            val allIndustriesAgain = awaitItem()
            assertEquals(industries, allIndustriesAgain)
            
            awaitComplete()
        }
    }
    
    @Test
    fun `refreshData should reload industries`() = runTest {
        // Given
        val industries = listOf(
            Industry("01", "水泥工業", 5),
            Industry("02", "食品工業", 12)
        )
        coEvery { getIndustriesUseCase() } returns flowOf(kotlin.Result.success(industries))
        
        // When
        viewModel.loadIndustries()
        viewModel.refreshData()
        
        // Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState is UiState.Initial)
            
            val loadingState = awaitItem()
            assertTrue(loadingState is UiState.Loading)
            
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            assertEquals(industries, (successState as UiState.Success).data)
            
            // 重新載入後應該再次看到初始狀態
            val initialStateAgain = awaitItem()
            assertTrue(initialStateAgain is UiState.Initial)
            
            awaitComplete()
        }
    }
}
