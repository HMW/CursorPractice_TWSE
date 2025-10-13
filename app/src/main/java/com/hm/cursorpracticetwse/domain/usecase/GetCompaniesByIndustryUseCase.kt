package com.hm.cursorpracticetwse.domain.usecase

import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.repository.TwseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 根據產業代碼取得公司列表的 Use Case
 * 
 * 封裝根據產業別查詢公司的業務邏輯
 * 用於顯示特定產業的所有公司
 */
class GetCompaniesByIndustryUseCase @Inject constructor(
    private val repository: TwseRepository
) {
    
    /**
     * 執行根據產業代碼取得公司列表
     * 
     * @param industryCode 產業代碼
     * @return Flow<Result<List<Company>>> 該產業的公司列表
     */
    suspend operator fun invoke(industryCode: String): Flow<Result<List<Company>>> {
        return repository.getCompaniesByIndustry(industryCode)
    }
}
