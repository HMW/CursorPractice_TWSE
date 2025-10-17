package com.hm.cursorpracticetwse.domain.usecase

import com.hm.cursorpracticetwse.domain.model.Industry
import com.hm.cursorpracticetwse.domain.repository.TwseRepository
import javax.inject.Inject

/**
 * 取得產業分類列表的 Use Case
 * 
 * 封裝取得產業分類的業務邏輯
 * 包含產業代碼、名稱和公司數量
 */
class GetIndustriesUseCase @Inject constructor(
    private val repository: TwseRepository
) {
    
    /**
     * 執行取得產業分類列表
     * 
     * @return Flow<Result<List<Industry>>> 產業列表的結果流
     */
    suspend operator fun invoke(): Result<List<Industry>> {
        return repository.getIndustries()
    }
}
