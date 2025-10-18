package com.hm.cursorpracticetwse.domain.usecase

import com.hm.cursorpracticetwse.domain.repository.WatchlistRepository
import javax.inject.Inject

/**
 * 從追蹤列表移除 Use Case
 * 
 * 負責從用戶的追蹤列表中移除公司
 */
class RemoveFromWatchlistUseCase @Inject constructor(
    private val watchlistRepository: WatchlistRepository
) {
    /**
     * 執行從追蹤列表移除
     * 
     * @param companyCode 要移除的公司代碼
     * @return 操作結果
     */
    suspend operator fun invoke(companyCode: String): Result<Unit> {
        return watchlistRepository.removeFromWatchlist(companyCode)
    }
}
