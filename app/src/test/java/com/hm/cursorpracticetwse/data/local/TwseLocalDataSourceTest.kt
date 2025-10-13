package com.hm.cursorpracticetwse.data.local

import com.hm.cursorpracticetwse.data.local.entities.CompanyEntity
import com.hm.cursorpracticetwse.data.local.entities.IndustryEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * TwseLocalDataSource 測試
 */
class TwseLocalDataSourceTest {
    
    private lateinit var dao: TwseDao
    private lateinit var localDataSource: TwseLocalDataSource
    
    @Before
    fun setUp() {
        dao = mockk()
        localDataSource = TwseLocalDataSource(dao)
    }
    
    @Test
    fun `getCompanies should return companies from dao`() = runTest {
        // Given
        val expectedCompanies = listOf(
            createTestCompanyEntity("1234", "台積電"),
            createTestCompanyEntity("5678", "聯發科")
        )
        every { dao.getAllCompanies() } returns flowOf(expectedCompanies)
        
        // When
        val result = localDataSource.getCompanies().toList()
        
        // Then
        assertEquals(expectedCompanies, result.first())
    }
    
    @Test
    fun `getCompaniesByIndustry should return filtered companies`() = runTest {
        // Given
        val industryCode = "01"
        val expectedCompanies = listOf(createTestCompanyEntity("1234", "台積電"))
        every { dao.getCompaniesByIndustry(industryCode) } returns flowOf(expectedCompanies)
        
        // When
        val result = localDataSource.getCompaniesByIndustry(industryCode).toList()
        
        // Then
        assertEquals(expectedCompanies, result.first())
    }
    
    @Test
    fun `searchCompanies should return search results`() = runTest {
        // Given
        val searchTerm = "台積"
        val expectedCompanies = listOf(createTestCompanyEntity("1234", "台積電"))
        every { dao.searchCompanies(searchTerm) } returns flowOf(expectedCompanies)
        
        // When
        val result = localDataSource.searchCompanies(searchTerm).toList()
        
        // Then
        assertEquals(expectedCompanies, result.first())
    }
    
    @Test
    fun `insertCompanies should call dao insertAllCompanies`() = runTest {
        // Given
        val companies = listOf(
            createTestCompanyEntity("1234", "台積電"),
            createTestCompanyEntity("5678", "聯發科")
        )
        coEvery { dao.insertAllCompanies(companies) } returns Unit
        
        // When
        localDataSource.insertCompanies(companies)
        
        // Then - 如果沒有例外就表示成功
        coVerify { dao.insertAllCompanies(companies) }
    }
    
    @Test
    fun `clearAllCompanies should call dao deleteAllCompanies`() = runTest {
        // Given
        coEvery { dao.deleteAllCompanies() } returns Unit
        
        // When
        localDataSource.clearAllCompanies()
        
        // Then - 如果沒有例外就表示成功
        coVerify { dao.deleteAllCompanies() }
    }
    
    @Test
    fun `getIndustries should return industries from dao`() = runTest {
        // Given
        val expectedIndustries = listOf(
            createTestIndustryEntity("01", "水泥工業", 30),
            createTestIndustryEntity("02", "食品工業", 25)
        )
        every { dao.getAllIndustries() } returns flowOf(expectedIndustries)
        
        // When
        val result = localDataSource.getIndustries().toList()
        
        // Then
        assertEquals(expectedIndustries, result.first())
    }
    
    @Test
    fun `insertIndustries should call dao insertAllIndustries`() = runTest {
        // Given
        val industries = listOf(
            createTestIndustryEntity("01", "水泥工業", 30),
            createTestIndustryEntity("02", "食品工業", 25)
        )
        coEvery { dao.insertAllIndustries(industries) } returns Unit
        
        // When
        localDataSource.insertIndustries(industries)
        
        // Then - 如果沒有例外就表示成功
        coVerify { dao.insertAllIndustries(industries) }
    }
    
    @Test
    fun `getCompanyCount should return count from dao`() = runTest {
        // Given
        val expectedCount = 100
        every { dao.getCompanyCount() } returns flowOf(expectedCount)
        
        // When
        val result = localDataSource.getCompanyCount().toList()
        
        // Then
        assertEquals(expectedCount, result.first())
    }
    
    private fun createTestCompanyEntity(code: String, name: String): CompanyEntity {
        return CompanyEntity(
            公司代號 = code,
            出表日期 = "2023-01-01",
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
    
    private fun createTestIndustryEntity(code: String, name: String, count: Int): IndustryEntity {
        return IndustryEntity(
            code = code,
            name = name,
            companyCount = count
        )
    }
}
