package com.hm.cursorpracticetwse.data.local

import com.hm.cursorpracticetwse.data.local.dao.WatchlistDao
import com.hm.cursorpracticetwse.data.local.entities.WatchlistItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 追蹤列表本地資料源實現
 * 
 * 實現追蹤列表的本地資料操作
 */
class WatchlistLocalDataSourceImpl @Inject constructor(
    private val watchlistDao: WatchlistDao
) : WatchlistLocalDataSource {
    
    override fun getWatchlistItems(): Flow<List<WatchlistItemEntity>> {
        return watchlistDao.getAllWatchlistItems()
    }
    
    override suspend fun insertWatchlistItem(item: WatchlistItemEntity) {
        watchlistDao.insertWatchlistItem(item)
    }
    
    override suspend fun deleteWatchlistItem(companyCode: String) {
        watchlistDao.deleteWatchlistItem(companyCode)
    }
    
    override suspend fun isWatchlistItemExists(companyCode: String): Boolean {
        return watchlistDao.isWatchlistItemExists(companyCode)
    }
}
