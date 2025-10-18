package com.hm.cursorpracticetwse.data.mapper

import com.hm.cursorpracticetwse.data.local.entities.WatchlistItemEntity
import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.model.WatchlistItem
import java.text.SimpleDateFormat
import java.util.*

/**
 * 追蹤列表 Mapper
 * 
 * 負責追蹤列表相關的資料轉換
 */
object WatchlistMapper {
    
    /**
     * 將 Company 轉換為 WatchlistItemEntity
     * 
     * @param company 公司資料
     * @return 追蹤項目實體
     */
    fun toEntity(company: Company): WatchlistItemEntity {
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        
        return WatchlistItemEntity(
            companyCode = company.公司代號,
            companyName = company.公司名稱,
            companyShortName = company.公司簡稱,
            industryCode = company.getIndustryCode(),
            industryName = company.getIndustryName(),
            addedDate = currentDate
        )
    }
    
    /**
     * 將 WatchlistItemEntity 轉換為 WatchlistItem
     * 
     * @param entity 追蹤項目實體
     * @return 追蹤項目領域模型
     */
    fun toDomain(entity: WatchlistItemEntity): WatchlistItem {
        return WatchlistItem(
            companyCode = entity.companyCode,
            companyName = entity.companyName,
            companyShortName = entity.companyShortName,
            industryCode = entity.industryCode,
            industryName = entity.industryName,
            addedDate = entity.addedDate
        )
    }
    
    /**
     * 將 WatchlistItemEntity 列表轉換為 WatchlistItem 列表
     * 
     * @param entities 追蹤項目實體列表
     * @return 追蹤項目領域模型列表
     */
    fun toDomainList(entities: List<WatchlistItemEntity>): List<WatchlistItem> {
        return entities.map { toDomain(it) }
    }
}
