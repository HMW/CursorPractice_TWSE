package com.hm.cursorpracticetwse.data.local

import com.hm.cursorpracticetwse.data.local.entities.CompanyEntity
import com.hm.cursorpracticetwse.data.local.entities.IndustryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TWSE Local Data Source
 * 
 * 實作本地資料源，負責 Room Database 操作
 * 提供公司資料和產業分類的本地存取
 */
@Singleton
class TwseLocalDataSource @Inject constructor(
    private val dao: TwseDao
) {
    
    // ========== 公司資料操作 ==========
    
    /**
     * 取得所有公司資料
     * 
     * @return Flow<List<CompanyEntity>> 公司資料流
     */
    fun getCompanies(): Flow<List<CompanyEntity>> {
        return dao.getAllCompanies()
    }
    
    /**
     * 根據產業代碼取得公司列表
     * 
     * @param industryCode 產業代碼
     * @return Flow<List<CompanyEntity>> 該產業的公司列表
     */
    fun getCompaniesByIndustry(industryCode: String): Flow<List<CompanyEntity>> {
        return dao.getCompaniesByIndustry(industryCode)
    }
    
    /**
     * 根據公司代號取得單一公司
     * 
     * @param companyCode 公司代號
     * @return Flow<CompanyEntity?> 公司資料
     */
    fun getCompanyByCode(companyCode: String): Flow<CompanyEntity?> {
        return dao.getCompanyByCode(companyCode)
    }
    
    /**
     * 搜尋公司
     * 
     * @param searchTerm 搜尋關鍵字
     * @return Flow<List<CompanyEntity>> 搜尋結果
     */
    fun searchCompanies(searchTerm: String): Flow<List<CompanyEntity>> {
        return dao.searchCompanies(searchTerm)
    }
    
    /**
     * 插入或更新公司資料
     * 
     * @param companies 公司資料列表
     */
    suspend fun insertCompanies(companies: List<CompanyEntity>) {
        dao.insertAllCompanies(companies)
    }
    
    /**
     * 插入或更新單一公司資料
     * 
     * @param company 公司資料
     */
    suspend fun insertCompany(company: CompanyEntity) {
        dao.insertCompany(company)
    }
    
    /**
     * 刪除所有公司資料
     */
    suspend fun clearAllCompanies() {
        dao.deleteAllCompanies()
    }
    
    /**
     * 根據公司代號刪除公司
     * 
     * @param companyCode 公司代號
     */
    suspend fun deleteCompanyByCode(companyCode: String) {
        dao.deleteCompanyByCode(companyCode)
    }
    
    // ========== 產業分類操作 ==========
    
    /**
     * 取得所有產業分類
     * 
     * @return Flow<List<IndustryEntity>> 產業分類流
     */
    fun getIndustries(): Flow<List<IndustryEntity>> {
        return dao.getAllIndustries()
    }
    
    /**
     * 根據產業代碼取得產業資訊
     * 
     * @param industryCode 產業代碼
     * @return Flow<IndustryEntity?> 產業資訊
     */
    fun getIndustryByCode(industryCode: String): Flow<IndustryEntity?> {
        return dao.getIndustryByCode(industryCode)
    }
    
    /**
     * 插入或更新產業分類
     * 
     * @param industries 產業分類列表
     */
    suspend fun insertIndustries(industries: List<IndustryEntity>) {
        dao.insertAllIndustries(industries)
    }
    
    /**
     * 插入或更新單一產業分類
     * 
     * @param industry 產業分類
     */
    suspend fun insertIndustry(industry: IndustryEntity) {
        dao.insertIndustry(industry)
    }
    
    /**
     * 刪除所有產業分類
     */
    suspend fun clearAllIndustries() {
        dao.deleteAllIndustries()
    }
    
    // ========== 統計查詢 ==========
    
    /**
     * 取得公司總數
     * 
     * @return Flow<Int> 公司總數
     */
    fun getCompanyCount(): Flow<Int> {
        return dao.getCompanyCount()
    }
    
    /**
     * 取得產業總數
     * 
     * @return Flow<Int> 產業總數
     */
    fun getIndustryCount(): Flow<Int> {
        return dao.getIndustryCount()
    }
    
    /**
     * 根據產業代碼取得公司數量
     * 
     * @param industryCode 產業代碼
     * @return Flow<Int> 該產業的公司數量
     */
    fun getCompanyCountByIndustry(industryCode: String): Flow<Int> {
        return dao.getCompanyCountByIndustry(industryCode)
    }
}
