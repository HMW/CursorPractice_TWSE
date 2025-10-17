package com.hm.cursorpracticetwse.data.repository

import android.util.Log
import com.hm.cursorpracticetwse.data.local.TwseLocalDataSource
import com.hm.cursorpracticetwse.data.mapper.CompanyMapper
import com.hm.cursorpracticetwse.data.mapper.IndustryMapper
import com.hm.cursorpracticetwse.data.remote.TwseRemoteDataSource
import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.model.Industry
import com.hm.cursorpracticetwse.domain.repository.TwseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TWSE Repository 實作
 * 
 * 實作離線優先策略：
 * 1. 先從本地資料庫讀取資料（快速回應）
 * 2. 同時發起遠端 API 請求（更新資料）
 * 3. 將遠端資料儲存到本地資料庫
 * 4. 發送最新的資料給 UI
 */
@Singleton
class TwseRepositoryImpl @Inject constructor(
    private val remoteDataSource: TwseRemoteDataSource,
    private val localDataSource: TwseLocalDataSource
) : TwseRepository {
    
    override suspend fun getCompanies(): Result<List<Company>> {
        Log.d("TwseRepositoryImpl", "twse] getCompanies() called")
        
        // 1. 優先嘗試從遠端 API 取得最新資料
        Log.d("TwseRepositoryImpl", "twse] Starting to fetch companies from remote API")
        try {
            Log.d("TwseRepositoryImpl", "twse] About to call remoteDataSource.fetchCompanies()")
            val remoteCompanies = remoteDataSource.fetchCompanies()
            Log.d("TwseRepositoryImpl", "twse] Remote companies fetched successfully: ${remoteCompanies.size}")
            val companyEntities = CompanyMapper.toEntityList(remoteCompanies)
            
            // 2. 將遠端資料儲存到本地資料庫
            Log.d("TwseRepositoryImpl", "twse] Saving remote companies to local database")
            localDataSource.insertCompanies(companyEntities)
            
            // 3. 返回最新的遠端資料
            val companies = CompanyMapper.toDomainListFromEntity(companyEntities)
            Log.d("TwseRepositoryImpl", "twse] Returning remote companies: ${companies.size}")
            return Result.success(companies)
            
        } catch (e: Exception) {
            Log.e("TwseRepositoryImpl", "twse] Failed to fetch companies from remote, trying local database", e)
            
            // 4. 如果遠端請求失敗，嘗試從本地資料庫讀取
            try {
                Log.d("TwseRepositoryImpl", "twse] Starting to get companies from local database")
                val localCompanies = localDataSource.getCompanies().first()
                Log.d("TwseRepositoryImpl", "twse] Local companies found: ${localCompanies.size}")
                
                val companies = CompanyMapper.toDomainListFromEntity(localCompanies)
                Log.d("TwseRepositoryImpl", "twse] Returning local companies: ${companies.size}")
                return Result.success(companies)
                
            } catch (localException: Exception) {
                Log.e("TwseRepositoryImpl", "twse] Failed to get companies from both remote and local", localException)
                return Result.failure(localException)
            }
        }
    }
    
    override suspend fun getIndustries(): Result<List<Industry>> {
        Log.d("TwseRepositoryImpl", "twse] getIndustries() called")
        
        // 1. 優先嘗試從遠端 API 取得公司資料並計算產業分類
        Log.d("TwseRepositoryImpl", "twse] Starting to fetch companies for industries calculation")
        try {
            Log.d("TwseRepositoryImpl", "twse] About to call remoteDataSource.fetchCompanies() for industries")
            val remoteCompanies = remoteDataSource.fetchCompanies()
            val industries = calculateIndustriesFromCompanies(remoteCompanies)
            val industryEntities = IndustryMapper.toEntityList(industries)
            
            // 2. 更新本地產業分類
            Log.d("TwseRepositoryImpl", "twse] Saving industries to local database")
            localDataSource.insertIndustries(industryEntities)
            
            // 3. 返回最新的產業分類
            Log.d("TwseRepositoryImpl", "twse] Returning remote industries: ${industries.size}")
            return Result.success(industries)
            
        } catch (e: Exception) {
            Log.e("TwseRepositoryImpl", "twse] Failed to fetch industries from remote, trying local database", e)
            
            // 4. 如果遠端請求失敗，嘗試從本地資料庫讀取
            try {
                Log.d("TwseRepositoryImpl", "twse] Starting to get industries from local database")
                val localIndustries = localDataSource.getIndustries().first()
                Log.d("TwseRepositoryImpl", "twse] Local industries found: ${localIndustries.size}")
                
                val industries = IndustryMapper.toDomainList(localIndustries)
                Log.d("TwseRepositoryImpl", "twse] Returning local industries: ${industries.size}")
                return Result.success(industries)
                
            } catch (localException: Exception) {
                Log.e("TwseRepositoryImpl", "twse] Failed to get industries from both remote and local", localException)
                return Result.failure(localException)
            }
        }
    }
    
    override suspend fun getCompaniesByIndustry(industryCode: String): Result<List<Company>> {
        Log.d("TwseRepositoryImpl", "twse] getCompaniesByIndustry() called for industry: $industryCode")
        
        // 1. 優先嘗試從遠端 API 取得並篩選
        Log.d("TwseRepositoryImpl", "twse] Starting to fetch companies from remote API")
        try {
            val remoteCompanies = remoteDataSource.fetchCompanies()
            val filteredCompanies = remoteCompanies.filter { company ->
                company.產業別.startsWith(industryCode)
            }
            val companyEntities = CompanyMapper.toEntityList(filteredCompanies)
            
            // 2. 更新本地資料庫
            Log.d("TwseRepositoryImpl", "twse] Saving filtered companies to local database")
            localDataSource.insertCompanies(companyEntities)
            
            // 3. 返回最新的篩選結果
            val companies = CompanyMapper.toDomainListFromEntity(companyEntities)
            Log.d("TwseRepositoryImpl", "twse] Returning remote companies: ${companies.size}")
            return Result.success(companies)
            
        } catch (e: Exception) {
            Log.e("TwseRepositoryImpl", "twse] Failed to fetch companies by industry from remote, trying local database", e)
            
            // 4. 如果遠端請求失敗，嘗試從本地資料庫讀取
            try {
                Log.d("TwseRepositoryImpl", "twse] Starting to get companies by industry from local database")
                val localCompanies = localDataSource.getCompaniesByIndustry(industryCode).first()
                Log.d("TwseRepositoryImpl", "twse] Local companies found: ${localCompanies.size}")
                
                val companies = CompanyMapper.toDomainListFromEntity(localCompanies)
                Log.d("TwseRepositoryImpl", "twse] Returning local companies: ${companies.size}")
                return Result.success(companies)
                
            } catch (localException: Exception) {
                Log.e("TwseRepositoryImpl", "twse] Failed to get companies by industry from both remote and local", localException)
                return Result.failure(localException)
            }
        }
    }
    
    override suspend fun refreshData(): Flow<Result<Unit>> {
        return flow {
            try {
                // 1. 取得遠端資料
                val remoteCompanies = remoteDataSource.fetchCompanies()
                val companyEntities = CompanyMapper.toEntityList(remoteCompanies)
                val industries = calculateIndustriesFromCompanies(remoteCompanies)
                val industryEntities = IndustryMapper.toEntityList(industries)
                
                // 2. 清除舊資料並插入新資料
                localDataSource.clearAllCompanies()
                localDataSource.clearAllIndustries()
                localDataSource.insertCompanies(companyEntities)
                localDataSource.insertIndustries(industryEntities)
                
                emit(Result.success(Unit))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }
    
    /**
     * 從公司資料計算產業分類
     * 
     * @param companies 公司資料列表
     * @return 產業分類列表
     */
    private fun calculateIndustriesFromCompanies(companies: List<com.hm.cursorpracticetwse.data.remote.dto.CompanyDto>): List<Industry> {
        val industryMap = mutableMapOf<String, Int>()

        companies.forEach { company ->
            val industryCode = company.產業別.split(":").firstOrNull() ?: ""
            if (industryCode.isNotBlank()) {
                industryMap[industryCode] = industryMap.getOrDefault(industryCode, 0) + 1
            }
        }

        return industryMap.map { (code, count) ->
            val industryName = getIndustryNameByCode(code)
            
            Industry(
                code = code,
                name = industryName,
                companyCount = count
            )
        }.sortedBy { it.code }
    }
    
    /**
     * 根據產業別代碼取得產業名稱
     */
    private fun getIndustryNameByCode(code: String): String {
        return when (code) {
            "01" -> "水泥工業"
            "02" -> "食品工業"
            "03" -> "塑膠工業"
            "04" -> "紡織纖維"
            "05" -> "電機機械"
            "06" -> "電器電纜"
            "08" -> "玻璃陶瓷"
            "09" -> "造紙工業"
            "10" -> "鋼鐵工業"
            "11" -> "橡膠工業"
            "12" -> "汽車工業"
            "14" -> "建材營造"
            "15" -> "航運業"
            "16" -> "觀光餐旅"
            "17" -> "金融保險"
            "18" -> "貿易百貨"
            "19" -> "綜合"
            "20" -> "其他"
            "21" -> "化學工業"
            "22" -> "生技醫療業"
            "23" -> "油電燃氣業"
            "24" -> "半導體業"
            "25" -> "電腦及週邊設備業"
            "26" -> "光電業"
            "27" -> "通信網路業"
            "28" -> "電子零組件業"
            "29" -> "電子通路業"
            "30" -> "資訊服務業"
            "31" -> "其他電子業"
            "32" -> "文化創意業"
            "33" -> "農業科技業"
            "34" -> "電子商務"
            "35" -> "綠能環保"
            "36" -> "數位雲端"
            "37" -> "運動休閒"
            "38" -> "居家生活"
            "80" -> "管理股票"
            "98" -> "期貨"
            "XX" -> "證券"
            else -> "未知產業"
        }
    }
}
