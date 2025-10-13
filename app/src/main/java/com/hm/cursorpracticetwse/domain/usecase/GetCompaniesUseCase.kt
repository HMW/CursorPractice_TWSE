package com.hm.cursorpracticetwse.domain.usecase

import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.repository.TwseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 取得所有公司資料的 Use Case
 * 
 * 封裝取得所有公司資料的業務邏輯
 * 遵循 Clean Architecture 的 Use Case 模式
 */
class GetCompaniesUseCase @Inject constructor(
    private val repository: TwseRepository
) {
    
    /**
     * 執行取得所有公司資料
     * 
     * @return Flow<Result<List<Company>>> 公司列表的結果流
     */
    suspend operator fun invoke(): Flow<Result<List<Company>>> {
        return repository.getCompanies()
    }
}
