package com.hm.cursorpracticetwse.domain.usecase

import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.repository.TwseRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * GetCompaniesUseCase 測試
 */
class GetCompaniesUseCaseTest {
    
    private lateinit var repository: TwseRepository
    private lateinit var useCase: GetCompaniesUseCase
    
    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCompaniesUseCase(repository)
    }
    
    @Test
    fun `invoke should return companies from repository`() = runTest {
        // Given
        val expectedCompanies = listOf(
            createTestCompany("1234", "台積電"),
            createTestCompany("5678", "聯發科")
        )
        coEvery { repository.getCompanies() } returns flowOf(Result.success(expectedCompanies))
        
        // When
        val result = useCase().toList()
        
        // Then
        assertTrue(result.isNotEmpty())
        assertTrue(result.first().isSuccess)
        assertEquals(expectedCompanies, result.first().getOrNull())
    }
    
    @Test
    fun `invoke should handle repository failure`() = runTest {
        // Given
        val error = Exception("Network error")
        coEvery { repository.getCompanies() } returns flowOf(Result.failure(error))
        
        // When
        val result = useCase().toList()
        
        // Then
        assertTrue(result.isNotEmpty())
        assertTrue(result.first().isFailure)
        assertEquals(error, result.first().exceptionOrNull())
    }
    
    private fun createTestCompany(code: String, name: String): Company {
        return Company(
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
