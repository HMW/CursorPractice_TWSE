package com.hm.cursorpracticetwse.domain.usecase

import com.hm.cursorpracticetwse.domain.repository.WatchlistRepository
import javax.inject.Inject

/**
 * 檢查是否在追蹤列表中 Use Case
 * 
 * 負責檢查指定公司是否在用戶的追蹤列表中
 */
class IsInWatchlistUseCase @Inject constructor(
    private val watchlistRepository: WatchlistRepository
) {
    /**
     * 執行檢查是否在追蹤列表中
     * 
     * @param companyCode 要檢查的公司代碼
     * @return 是否在追蹤列表中的結果
     */
    suspend operator fun invoke(companyCode: String): Result<Boolean> {
        return watchlistRepository.isInWatchlist(companyCode)
    }
}
