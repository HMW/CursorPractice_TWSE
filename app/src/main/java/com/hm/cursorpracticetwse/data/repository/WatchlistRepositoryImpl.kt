package com.hm.cursorpracticetwse.data.repository

import android.util.Log
import com.hm.cursorpracticetwse.data.local.WatchlistLocalDataSource
import com.hm.cursorpracticetwse.data.mapper.WatchlistMapper
import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.model.WatchlistItem
import com.hm.cursorpracticetwse.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * 追蹤列表 Repository 實現
 * 
 * 實現追蹤列表的資料操作
 */
class WatchlistRepositoryImpl @Inject constructor(
    private val localDataSource: WatchlistLocalDataSource
) : WatchlistRepository {
    
    override suspend fun getWatchlist(): Result<List<WatchlistItem>> {
        Log.d("WatchlistRepositoryImpl", "twse] getWatchlist() called")
        
        return try {
            val entities = localDataSource.getWatchlistItems().first()
            val items = WatchlistMapper.toDomainList(entities)
            Log.d("WatchlistRepositoryImpl", "twse] Retrieved ${items.size} watchlist items")
            Result.success(items)
        } catch (e: Exception) {
            Log.e("WatchlistRepositoryImpl", "twse] Failed to get watchlist", e)
            Result.failure(e)
        }
    }
    
    override suspend fun addToWatchlist(company: Company): Result<Unit> {
        Log.d("WatchlistRepositoryImpl", "twse] addToWatchlist() called for company: ${company.公司代號}")
        
        return try {
            val entity = WatchlistMapper.toEntity(company)
            localDataSource.insertWatchlistItem(entity)
            Log.d("WatchlistRepositoryImpl", "twse] Successfully added company to watchlist: ${company.公司代號}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("WatchlistRepositoryImpl", "twse] Failed to add company to watchlist", e)
            Result.failure(e)
        }
    }
    
    override suspend fun removeFromWatchlist(companyCode: String): Result<Unit> {
        Log.d("WatchlistRepositoryImpl", "twse] removeFromWatchlist() called for company: $companyCode")
        
        return try {
            localDataSource.deleteWatchlistItem(companyCode)
            Log.d("WatchlistRepositoryImpl", "twse] Successfully removed company from watchlist: $companyCode")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("WatchlistRepositoryImpl", "twse] Failed to remove company from watchlist", e)
            Result.failure(e)
        }
    }
    
    override suspend fun isInWatchlist(companyCode: String): Result<Boolean> {
        Log.d("WatchlistRepositoryImpl", "twse] isInWatchlist() called for company: $companyCode")
        
        return try {
            val exists = localDataSource.isWatchlistItemExists(companyCode)
            Log.d("WatchlistRepositoryImpl", "twse] Company $companyCode is in watchlist: $exists")
            Result.success(exists)
        } catch (e: Exception) {
            Log.e("WatchlistRepositoryImpl", "twse] Failed to check if company is in watchlist", e)
            Result.failure(e)
        }
    }
}
