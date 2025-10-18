package com.hm.cursorpracticetwse.domain.usecase

import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.repository.WatchlistRepository
import javax.inject.Inject

/**
 * 加入追蹤列表 Use Case
 * 
 * 負責將公司加入用戶的追蹤列表
 */
class AddToWatchlistUseCase @Inject constructor(
    private val watchlistRepository: WatchlistRepository
) {
    /**
     * 執行加入追蹤列表
     * 
     * @param company 要加入的公司
     * @return 操作結果
     */
    suspend operator fun invoke(company: Company): Result<Unit> {
        return watchlistRepository.addToWatchlist(company)
    }
}
