package com.hm.cursorpracticetwse.data.local

import androidx.room.*
import com.hm.cursorpracticetwse.data.local.entities.CompanyEntity
import com.hm.cursorpracticetwse.data.local.entities.IndustryEntity
import kotlinx.coroutines.flow.Flow

/**
 * TWSE DAO (Data Access Object)
 * 
 * 定義資料庫存取操作
 * 包含公司資料和產業分類的 CRUD 操作
 */
@Dao
interface TwseDao {
    
    // ========== 公司資料操作 ==========
    
    /**
     * 取得所有公司資料
     * 
     * @return Flow<List<CompanyEntity>> 公司資料流
     */
    @Query("SELECT * FROM companies ORDER BY 公司名稱")
    fun getAllCompanies(): Flow<List<CompanyEntity>>
    
    /**
     * 根據產業代碼取得公司列表
     * 
     * @param industryCode 產業代碼
     * @return Flow<List<CompanyEntity>> 該產業的公司列表
     */
    @Query("SELECT * FROM companies WHERE 產業別 LIKE :industryCode || '%' ORDER BY 公司名稱")
    fun getCompaniesByIndustry(industryCode: String): Flow<List<CompanyEntity>>
    
    /**
     * 根據公司代號取得單一公司
     * 
     * @param companyCode 公司代號
     * @return Flow<CompanyEntity?> 公司資料
     */
    @Query("SELECT * FROM companies WHERE 公司代號 = :companyCode")
    fun getCompanyByCode(companyCode: String): Flow<CompanyEntity?>
    
    /**
     * 搜尋公司（根據名稱或代號）
     * 
     * @param searchTerm 搜尋關鍵字
     * @return Flow<List<CompanyEntity>> 搜尋結果
     */
    @Query("SELECT * FROM companies WHERE 公司名稱 LIKE '%' || :searchTerm || '%' OR 公司代號 LIKE '%' || :searchTerm || '%' ORDER BY 公司名稱")
    fun searchCompanies(searchTerm: String): Flow<List<CompanyEntity>>
    
    /**
     * 插入或更新公司資料
     * 
     * @param companies 公司資料列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCompanies(companies: List<CompanyEntity>)
    
    /**
     * 插入或更新單一公司資料
     * 
     * @param company 公司資料
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompany(company: CompanyEntity)
    
    /**
     * 刪除所有公司資料
     */
    @Query("DELETE FROM companies")
    suspend fun deleteAllCompanies()
    
    /**
     * 根據公司代號刪除公司
     * 
     * @param companyCode 公司代號
     */
    @Query("DELETE FROM companies WHERE 公司代號 = :companyCode")
    suspend fun deleteCompanyByCode(companyCode: String)
    
    // ========== 產業分類操作 ==========
    
    /**
     * 取得所有產業分類
     * 
     * @return Flow<List<IndustryEntity>> 產業分類流
     */
    @Query("SELECT * FROM industries ORDER BY code")
    fun getAllIndustries(): Flow<List<IndustryEntity>>
    
    /**
     * 根據產業代碼取得產業資訊
     * 
     * @param industryCode 產業代碼
     * @return Flow<IndustryEntity?> 產業資訊
     */
    @Query("SELECT * FROM industries WHERE code = :industryCode")
    fun getIndustryByCode(industryCode: String): Flow<IndustryEntity?>
    
    /**
     * 插入或更新產業分類
     * 
     * @param industries 產業分類列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllIndustries(industries: List<IndustryEntity>)
    
    /**
     * 插入或更新單一產業分類
     * 
     * @param industry 產業分類
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIndustry(industry: IndustryEntity)
    
    /**
     * 刪除所有產業分類
     */
    @Query("DELETE FROM industries")
    suspend fun deleteAllIndustries()
    
    // ========== 統計查詢 ==========
    
    /**
     * 取得公司總數
     * 
     * @return Flow<Int> 公司總數
     */
    @Query("SELECT COUNT(*) FROM companies")
    fun getCompanyCount(): Flow<Int>
    
    /**
     * 取得產業總數
     * 
     * @return Flow<Int> 產業總數
     */
    @Query("SELECT COUNT(*) FROM industries")
    fun getIndustryCount(): Flow<Int>
    
    /**
     * 根據產業代碼取得公司數量
     * 
     * @param industryCode 產業代碼
     * @return Flow<Int> 該產業的公司數量
     */
    @Query("SELECT COUNT(*) FROM companies WHERE 產業別 LIKE :industryCode || '%'")
    fun getCompanyCountByIndustry(industryCode: String): Flow<Int>
}
