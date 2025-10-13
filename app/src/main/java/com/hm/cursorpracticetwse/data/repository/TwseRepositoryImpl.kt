package com.hm.cursorpracticetwse.data.repository

import com.hm.cursorpracticetwse.data.local.TwseLocalDataSource
import com.hm.cursorpracticetwse.data.mapper.CompanyMapper
import com.hm.cursorpracticetwse.data.mapper.IndustryMapper
import com.hm.cursorpracticetwse.data.remote.TwseRemoteDataSource
import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.model.Industry
import com.hm.cursorpracticetwse.domain.repository.TwseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
    
    override suspend fun getCompanies(): Flow<Result<List<Company>>> {
        return flow {
            // 1. 先從本地資料庫讀取（快速回應）
            localDataSource.getCompanies()
                .map { entities ->
                    CompanyMapper.toDomainListFromEntity(entities)
                }
                .catch { e ->
                    emit(emptyList<Company>())
                }
                .collect { localCompanies ->
                    emit(Result.success(localCompanies))
                }
            
            // 2. 同時發起遠端 API 請求（更新資料）
            try {
                val remoteCompanies = remoteDataSource.fetchCompanies()
                val companyEntities = CompanyMapper.toEntityList(remoteCompanies)
                
                // 3. 將遠端資料儲存到本地資料庫
                localDataSource.insertCompanies(companyEntities)
                
                // 4. 發送最新的資料
                val updatedCompanies = CompanyMapper.toDomainListFromEntity(companyEntities)
                emit(Result.success(updatedCompanies))
            } catch (e: Exception) {
                // 如果遠端請求失敗，不影響本地資料的顯示
                // 可以選擇性地發送錯誤資訊
                emit(Result.failure(e))
            }
        }
    }
    
    override suspend fun getIndustries(): Flow<Result<List<Industry>>> {
        return flow {
            // 1. 先從本地資料庫讀取
            localDataSource.getIndustries()
                .map { entities ->
                    IndustryMapper.toDomainList(entities)
                }
                .catch { e ->
                    emit(emptyList<Industry>())
                }
                .collect { localIndustries ->
                    emit(Result.success(localIndustries))
                }
            
            // 2. 從公司資料計算產業分類
            try {
                val remoteCompanies = remoteDataSource.fetchCompanies()
                val industries = calculateIndustriesFromCompanies(remoteCompanies)
                val industryEntities = IndustryMapper.toEntityList(industries)
                
                // 3. 更新本地產業分類
                localDataSource.insertIndustries(industryEntities)
                
                // 4. 發送最新的產業分類
                emit(Result.success(industries))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }
    
    override suspend fun getCompaniesByIndustry(industryCode: String): Flow<Result<List<Company>>> {
        return flow {
            // 1. 先從本地資料庫讀取
            localDataSource.getCompaniesByIndustry(industryCode)
                .map { entities ->
                    CompanyMapper.toDomainListFromEntity(entities)
                }
                .catch { e ->
                    emit(emptyList<Company>())
                }
                .collect { localCompanies ->
                    emit(Result.success(localCompanies))
                }
            
            // 2. 同時發起遠端 API 請求
            try {
                val remoteCompanies = remoteDataSource.fetchCompanies()
                val filteredCompanies = remoteCompanies.filter { company ->
                    company.產業別.startsWith(industryCode)
                }
                val companyEntities = CompanyMapper.toEntityList(filteredCompanies)
                
                // 3. 更新本地資料庫
                localDataSource.insertCompanies(companyEntities)
                
                // 4. 發送最新的篩選結果
                val updatedCompanies = CompanyMapper.toDomainListFromEntity(companyEntities)
                emit(Result.success(updatedCompanies))
            } catch (e: Exception) {
                emit(Result.failure(e))
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
            val industryName = companies.firstOrNull { 
                it.產業別.startsWith("$code:")
            }?.產業別?.split(":")?.getOrNull(1)?.trim() ?: "未知產業"
            
            Industry(
                code = code,
                name = industryName,
                companyCount = count
            )
        }.sortedBy { it.code }
    }
}
