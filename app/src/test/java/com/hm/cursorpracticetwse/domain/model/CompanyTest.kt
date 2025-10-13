package com.hm.cursorpracticetwse.domain.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Company 領域模型測試
 */
class CompanyTest {
    
    @Test
    fun `getDisplayName should return company short name when available`() {
        // Given
        val company = createTestCompany(公司簡稱 = "台積電")
        
        // When
        val displayName = company.getDisplayName()
        
        // Then
        assertEquals("台積電", displayName)
    }
    
    @Test
    fun `getDisplayName should return company name when short name is blank`() {
        // Given
        val company = createTestCompany(公司簡稱 = "", 公司名稱 = "台灣積體電路製造股份有限公司")
        
        // When
        val displayName = company.getDisplayName()
        
        // Then
        assertEquals("台灣積體電路製造股份有限公司", displayName)
    }
    
    @Test
    fun `isListed should return true when listing date is not blank`() {
        // Given
        val company = createTestCompany(上市日期 = "1994-09-05")
        
        // When
        val isListed = company.isListed()
        
        // Then
        assertTrue(isListed)
    }
    
    @Test
    fun `isListed should return false when listing date is blank`() {
        // Given
        val company = createTestCompany(上市日期 = "")
        
        // When
        val isListed = company.isListed()
        
        // Then
        assertFalse(isListed)
    }
    
    @Test
    fun `getIndustryCode should extract code from industry string`() {
        // Given
        val company = createTestCompany(產業別 = "01:水泥工業")
        
        // When
        val industryCode = company.getIndustryCode()
        
        // Then
        assertEquals("01", industryCode)
    }
    
    @Test
    fun `getIndustryName should extract name from industry string`() {
        // Given
        val company = createTestCompany(產業別 = "01:水泥工業")
        
        // When
        val industryName = company.getIndustryName()
        
        // Then
        assertEquals("水泥工業", industryName)
    }
    
    @Test
    fun `getIndustryCode should return empty string when industry is malformed`() {
        // Given
        val company = createTestCompany(產業別 = "水泥工業")
        
        // When
        val industryCode = company.getIndustryCode()
        
        // Then
        assertEquals("", industryCode)
    }
    
    private fun createTestCompany(
        公司簡稱: String = "測試公司",
        公司名稱: String = "測試股份有限公司",
        上市日期: String = "2023-01-01",
        產業別: String = "01:測試產業"
    ): Company {
        return Company(
            出表日期 = "2023-01-01",
            公司代號 = "1234",
            公司名稱 = 公司名稱,
            公司簡稱 = 公司簡稱,
            外國企業註冊地國 = "",
            產業別 = 產業別,
            住址 = "台北市信義區",
            營利事業統一編號 = "12345678",
            董事長 = "張三",
            總經理 = "李四",
            發言人 = "王五",
            發言人職稱 = "財務長",
            代理發言人 = "",
            總機電話 = "02-12345678",
            成立日期 = "2020-01-01",
            上市日期 = 上市日期,
            普通股每股面額 = "10",
            實收資本額 = "1000000000",
            已發行普通股數或TDR原股發行股數 = "100000000",
            私募股數 = "0",
            特別股 = "0",
            編製財務報告類型 = "1",
            股票過戶機構 = "台灣集中保管結算所",
            過戶電話 = "02-23456789",
            過戶地址 = "台北市信義區",
            簽證會計師事務所 = "勤業眾信",
            簽證會計師1 = "會計師A",
            簽證會計師2 = "會計師B",
            英文簡稱 = "Test Corp",
            英文通訊地址 = "Taipei, Taiwan",
            傳真機號碼 = "02-12345679",
            電子郵件信箱 = "test@example.com",
            網址 = "https://www.example.com"
        )
    }
}
