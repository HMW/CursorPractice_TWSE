package com.hm.cursorpracticetwse.data.remote

import com.hm.cursorpracticetwse.data.remote.dto.CompanyDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * TwseRemoteDataSource 測試
 */
class TwseRemoteDataSourceTest {
    
    private lateinit var apiService: TwseApiService
    private lateinit var remoteDataSource: TwseRemoteDataSource
    
    @Before
    fun setUp() {
        apiService = mockk()
        remoteDataSource = TwseRemoteDataSource(apiService)
    }
    
    @Test
    fun `fetchCompanies should return companies when API call succeeds`() = runTest {
        // Given
        val expectedCompanies = listOf(
            createTestCompanyDto("1234", "台積電"),
            createTestCompanyDto("5678", "聯發科")
        )
        coEvery { apiService.getCompanies() } returns expectedCompanies
        
        // When
        val result = remoteDataSource.fetchCompanies()
        
        // Then
        assertEquals(expectedCompanies, result)
        assertEquals(2, result.size)
        assertEquals("台積電", result[0].公司名稱)
        assertEquals("聯發科", result[1].公司名稱)
    }
    
    @Test
    fun `fetchCompanies should throw RemoteDataSourceException when API call fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { apiService.getCompanies() } throws exception
        
        // When & Then
        try {
            remoteDataSource.fetchCompanies()
            fail("Expected RemoteDataSourceException to be thrown")
        } catch (e: RemoteDataSourceException) {
            assertEquals("Failed to fetch companies from TWSE API", e.message)
            assertEquals(exception, e.cause)
        }
    }
    
    @Test
    fun `fetchCompanies should handle empty response`() = runTest {
        // Given
        coEvery { apiService.getCompanies() } returns emptyList()
        
        // When
        val result = remoteDataSource.fetchCompanies()
        
        // Then
        assertTrue(result.isEmpty())
    }
    
    private fun createTestCompanyDto(code: String, name: String): CompanyDto {
        return CompanyDto(
            出表日期 = "2023-01-01",
            公司代號 = code,
            公司名稱 = name,
            公司簡稱 = name,
            外國企業註冊地國 = "",
            產業別 = "01:測試產業",
            住址 = "台北市",
            營利事業統一編號 = "12345678",
            董事長 = "張三",
            總經理 = "李四",
            發言人 = "王五",
            發言人職稱 = "財務長",
            代理發言人 = "",
            總機電話 = "02-12345678",
            成立日期 = "2020-01-01",
            上市日期 = "2023-01-01",
            普通股每股面額 = "10",
            實收資本額 = "1000000000",
            已發行普通股數或TDR原股發行股數 = "100000000",
            私募股數 = "0",
            特別股 = "0",
            編製財務報告類型 = "1",
            股票過戶機構 = "台灣集中保管結算所",
            過戶電話 = "02-23456789",
            過戶地址 = "台北市",
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
