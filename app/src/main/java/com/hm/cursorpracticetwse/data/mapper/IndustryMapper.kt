package com.hm.cursorpracticetwse.data.mapper

import com.hm.cursorpracticetwse.data.local.entities.IndustryEntity
import com.hm.cursorpracticetwse.domain.model.Industry

/**
 * 產業資料轉換器
 * 
 * 負責在 Entity 和 Domain Model 之間轉換產業資料
 * Entity ↔ Domain Model
 */
object IndustryMapper {
    
    /**
     * 將 Entity 轉換為 Domain Model
     * 
     * @param entity Database Entity
     * @return Domain Model
     */
    fun toDomain(entity: IndustryEntity): Industry {
        return Industry(
            code = entity.code,
            name = entity.name,
            companyCount = entity.companyCount
        )
    }
    
    /**
     * 將 Domain Model 轉換為 Entity
     * 
     * @param domain Domain Model
     * @return Database Entity
     */
    fun toEntity(domain: Industry): IndustryEntity {
        return IndustryEntity(
            code = domain.code,
            name = domain.name,
            companyCount = domain.companyCount
        )
    }
    
    /**
     * 將 Entity 列表轉換為 Domain Model 列表
     * 
     * @param entityList Entity 列表
     * @return Domain Model 列表
     */
    fun toDomainList(entityList: List<IndustryEntity>): List<Industry> {
        return entityList.map { toDomain(it) }
    }
    
    /**
     * 將 Domain Model 列表轉換為 Entity 列表
     * 
     * @param domainList Domain Model 列表
     * @return Entity 列表
     */
    fun toEntityList(domainList: List<Industry>): List<IndustryEntity> {
        return domainList.map { toEntity(it) }
    }
}
