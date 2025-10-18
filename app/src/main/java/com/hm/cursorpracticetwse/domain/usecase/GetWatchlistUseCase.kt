package com.hm.cursorpracticetwse.domain.usecase

import com.hm.cursorpracticetwse.domain.model.WatchlistItem
import com.hm.cursorpracticetwse.domain.repository.WatchlistRepository
import javax.inject.Inject

/**
 * 取得追蹤列表 Use Case
 * 
 * 負責取得用戶的追蹤列表
 */
class GetWatchlistUseCase @Inject constructor(
    private val watchlistRepository: WatchlistRepository
) {
    /**
     * 執行取得追蹤列表
     * 
     * @return 追蹤列表結果
     */
    suspend operator fun invoke(): Result<List<WatchlistItem>> {
        return watchlistRepository.getWatchlist()
    }
}
