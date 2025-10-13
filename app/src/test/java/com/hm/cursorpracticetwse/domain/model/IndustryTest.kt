package com.hm.cursorpracticetwse.domain.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Industry 領域模型測試
 */
class IndustryTest {
    
    @Test
    fun `getDisplayName should return formatted name with company count`() {
        // Given
        val industry = Industry(code = "01", name = "水泥工業", companyCount = 30)
        
        // When
        val displayName = industry.getDisplayName()
        
        // Then
        assertEquals("水泥工業(30)", displayName)
    }
    
    @Test
    fun `hasCompanies should return true when company count is greater than zero`() {
        // Given
        val industry = Industry(code = "01", name = "水泥工業", companyCount = 5)
        
        // When
        val hasCompanies = industry.hasCompanies()
        
        // Then
        assertTrue(hasCompanies)
    }
    
    @Test
    fun `hasCompanies should return false when company count is zero`() {
        // Given
        val industry = Industry(code = "01", name = "水泥工業", companyCount = 0)
        
        // When
        val hasCompanies = industry.hasCompanies()
        
        // Then
        assertFalse(hasCompanies)
    }
    
    @Test
    fun `getFullInfo should return formatted full information`() {
        // Given
        val industry = Industry(code = "01", name = "水泥工業", companyCount = 30)
        
        // When
        val fullInfo = industry.getFullInfo()
        
        // Then
        assertEquals("01: 水泥工業(30)", fullInfo)
    }
    
    @Test
    fun `industry with zero companies should display correctly`() {
        // Given
        val industry = Industry(code = "99", name = "其他", companyCount = 0)
        
        // When
        val displayName = industry.getDisplayName()
        val hasCompanies = industry.hasCompanies()
        val fullInfo = industry.getFullInfo()
        
        // Then
        assertEquals("其他(0)", displayName)
        assertFalse(hasCompanies)
        assertEquals("99: 其他(0)", fullInfo)
    }
}
