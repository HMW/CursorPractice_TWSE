package com.hm.cursorpracticetwse.data.mapper

import com.hm.cursorpracticetwse.data.local.entities.CompanyEntity
import com.hm.cursorpracticetwse.data.remote.dto.CompanyDto
import com.hm.cursorpracticetwse.domain.model.Company
import org.junit.Assert.*
import org.junit.Test

/**
 * CompanyMapper 測試
 */
class CompanyMapperTest {
    
    @Test
    fun `toDomain from DTO should map correctly`() {
        // Given
        val dto = createTestCompanyDto("1234", "台積電")
        
        // When
        val domain = CompanyMapper.toDomain(dto)
        
        // Then
        assertEquals(dto.公司代號, domain.公司代號)
        assertEquals(dto.公司名稱, domain.公司名稱)
        assertEquals(dto.公司簡稱, domain.公司簡稱)
        assertEquals(dto.產業別, domain.產業別)
    }
    
    @Test
    fun `toEntity from DTO should map correctly`() {
        // Given
        val dto = createTestCompanyDto("1234", "台積電")
        
        // When
        val entity = CompanyMapper.toEntity(dto)
        
        // Then
        assertEquals(dto.公司代號, entity.公司代號)
        assertEquals(dto.公司名稱, entity.公司名稱)
        assertEquals(dto.公司簡稱, entity.公司簡稱)
        assertEquals(dto.產業別, entity.產業別)
    }
    
    @Test
    fun `toDomain from Entity should map correctly`() {
        // Given
        val entity = createTestCompanyEntity("1234", "台積電")
        
        // When
        val domain = CompanyMapper.toDomain(entity)
        
        // Then
        assertEquals(entity.公司代號, domain.公司代號)
        assertEquals(entity.公司名稱, domain.公司名稱)
        assertEquals(entity.公司簡稱, domain.公司簡稱)
        assertEquals(entity.產業別, domain.產業別)
    }
    
    @Test
    fun `toDomainListFromDto from DTO list should map correctly`() {
        // Given
        val dtoList = listOf(
            createTestCompanyDto("1234", "台積電"),
            createTestCompanyDto("5678", "聯發科")
        )
        
        // When
        val domainList = CompanyMapper.toDomainListFromDto(dtoList)
        
        // Then
        assertEquals(2, domainList.size)
        assertEquals("台積電", domainList[0].公司名稱)
        assertEquals("聯發科", domainList[1].公司名稱)
    }
    
    @Test
    fun `toEntityList from DTO list should map correctly`() {
        // Given
        val dtoList = listOf(
            createTestCompanyDto("1234", "台積電"),
            createTestCompanyDto("5678", "聯發科")
        )
        
        // When
        val entityList = CompanyMapper.toEntityList(dtoList)
        
        // Then
        assertEquals(2, entityList.size)
        assertEquals("台積電", entityList[0].公司名稱)
        assertEquals("聯發科", entityList[1].公司名稱)
    }
    
    @Test
    fun `toDomainListFromEntity from Entity list should map correctly`() {
        // Given
        val entityList = listOf(
            createTestCompanyEntity("1234", "台積電"),
            createTestCompanyEntity("5678", "聯發科")
        )
        
        // When
        val domainList = CompanyMapper.toDomainListFromEntity(entityList)
        
        // Then
        assertEquals(2, domainList.size)
        assertEquals("台積電", domainList[0].公司名稱)
        assertEquals("聯發科", domainList[1].公司名稱)
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
}
