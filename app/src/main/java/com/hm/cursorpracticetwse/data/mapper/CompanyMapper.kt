package com.hm.cursorpracticetwse.data.mapper

import android.util.Log
import com.hm.cursorpracticetwse.data.local.entities.CompanyEntity
import com.hm.cursorpracticetwse.data.remote.dto.CompanyDto
import com.hm.cursorpracticetwse.domain.model.Company

/**
 * 公司資料轉換器
 * 
 * 負責在不同資料層之間轉換公司資料
 * DTO ↔ Entity ↔ Domain Model
 */
object CompanyMapper {
    
    /**
     * 將 DTO 轉換為 Domain Model
     * 
     * @param dto API 回應的 DTO
     * @return Domain Model
     */
    fun toDomain(dto: CompanyDto): Company {
        return Company(
            出表日期 = dto.出表日期,
            公司代號 = dto.公司代號,
            公司名稱 = dto.公司名稱,
            公司簡稱 = dto.公司簡稱,
            外國企業註冊地國 = dto.外國企業註冊地國,
            產業別 = dto.產業別,
            住址 = dto.住址,
            營利事業統一編號 = dto.營利事業統一編號,
            董事長 = dto.董事長,
            總經理 = dto.總經理,
            發言人 = dto.發言人,
            發言人職稱 = dto.發言人職稱 ?: "",
            代理發言人 = dto.代理發言人 ?: "",
            總機電話 = dto.總機電話,
            成立日期 = dto.成立日期,
            上市日期 = dto.上市日期 ?: "",
            普通股每股面額 = dto.普通股每股面額,
            實收資本額 = dto.實收資本額,
            已發行普通股數或TDR原股發行股數 = dto.已發行普通股數或TDR原股發行股數,
            私募股數 = dto.私募股數,
            特別股 = dto.特別股,
            編製財務報告類型 = dto.編製財務報告類型 ?: "",
            股票過戶機構 = dto.股票過戶機構,
            過戶電話 = dto.過戶電話,
            過戶地址 = dto.過戶地址,
            簽證會計師事務所 = dto.簽證會計師事務所,
            簽證會計師1 = dto.簽證會計師1,
            簽證會計師2 = dto.簽證會計師2,
            英文簡稱 = dto.英文簡稱,
            英文通訊地址 = dto.英文通訊地址,
            傳真機號碼 = dto.傳真機號碼,
            電子郵件信箱 = dto.電子郵件信箱,
            網址 = dto.網址
        )
    }
    
    /**
     * 將 DTO 轉換為 Entity
     * 
     * @param dto API 回應的 DTO
     * @return Database Entity
     */
    fun toEntity(dto: CompanyDto): CompanyEntity {
        Log.d("CompanyMapper", "twse] Converting DTO to Entity for company: ${dto.公司代號}")
        Log.d("CompanyMapper", "twse] 編製財務報告類型: ${dto.編製財務報告類型}")
        Log.d("CompanyMapper", "twse] 發言人職稱: ${dto.發言人職稱}")
        Log.d("CompanyMapper", "twse] 代理發言人: ${dto.代理發言人}")
        Log.d("CompanyMapper", "twse] 上市日期: ${dto.上市日期}")
        
        return CompanyEntity(
            公司代號 = dto.公司代號,
            出表日期 = dto.出表日期,
            公司名稱 = dto.公司名稱,
            公司簡稱 = dto.公司簡稱,
            外國企業註冊地國 = dto.外國企業註冊地國,
            產業別 = dto.產業別,
            住址 = dto.住址,
            營利事業統一編號 = dto.營利事業統一編號,
            董事長 = dto.董事長,
            總經理 = dto.總經理,
            發言人 = dto.發言人,
            發言人職稱 = dto.發言人職稱 ?: "",
            代理發言人 = dto.代理發言人 ?: "",
            總機電話 = dto.總機電話,
            成立日期 = dto.成立日期,
            上市日期 = dto.上市日期 ?: "",
            普通股每股面額 = dto.普通股每股面額,
            實收資本額 = dto.實收資本額,
            已發行普通股數或TDR原股發行股數 = dto.已發行普通股數或TDR原股發行股數,
            私募股數 = dto.私募股數,
            特別股 = dto.特別股,
            編製財務報告類型 = dto.編製財務報告類型 ?: "",
            股票過戶機構 = dto.股票過戶機構,
            過戶電話 = dto.過戶電話,
            過戶地址 = dto.過戶地址,
            簽證會計師事務所 = dto.簽證會計師事務所,
            簽證會計師1 = dto.簽證會計師1,
            簽證會計師2 = dto.簽證會計師2,
            英文簡稱 = dto.英文簡稱,
            英文通訊地址 = dto.英文通訊地址,
            傳真機號碼 = dto.傳真機號碼,
            電子郵件信箱 = dto.電子郵件信箱,
            網址 = dto.網址
        )
    }
    
    /**
     * 將 Entity 轉換為 Domain Model
     * 
     * @param entity Database Entity
     * @return Domain Model
     */
    fun toDomain(entity: CompanyEntity): Company {
        return Company(
            出表日期 = entity.出表日期,
            公司代號 = entity.公司代號,
            公司名稱 = entity.公司名稱,
            公司簡稱 = entity.公司簡稱,
            外國企業註冊地國 = entity.外國企業註冊地國,
            產業別 = entity.產業別,
            住址 = entity.住址,
            營利事業統一編號 = entity.營利事業統一編號,
            董事長 = entity.董事長,
            總經理 = entity.總經理,
            發言人 = entity.發言人,
            發言人職稱 = entity.發言人職稱,
            代理發言人 = entity.代理發言人,
            總機電話 = entity.總機電話,
            成立日期 = entity.成立日期,
            上市日期 = entity.上市日期,
            普通股每股面額 = entity.普通股每股面額,
            實收資本額 = entity.實收資本額,
            已發行普通股數或TDR原股發行股數 = entity.已發行普通股數或TDR原股發行股數,
            私募股數 = entity.私募股數,
            特別股 = entity.特別股,
            編製財務報告類型 = entity.編製財務報告類型,
            股票過戶機構 = entity.股票過戶機構,
            過戶電話 = entity.過戶電話,
            過戶地址 = entity.過戶地址,
            簽證會計師事務所 = entity.簽證會計師事務所,
            簽證會計師1 = entity.簽證會計師1,
            簽證會計師2 = entity.簽證會計師2,
            英文簡稱 = entity.英文簡稱,
            英文通訊地址 = entity.英文通訊地址,
            傳真機號碼 = entity.傳真機號碼,
            電子郵件信箱 = entity.電子郵件信箱,
            網址 = entity.網址
        )
    }
    
    /**
     * 將 DTO 列表轉換為 Domain Model 列表
     * 
     * @param dtoList DTO 列表
     * @return Domain Model 列表
     */
    fun toDomainListFromDto(dtoList: List<CompanyDto>): List<Company> {
        return dtoList.map { toDomain(it) }
    }
    
    /**
     * 將 DTO 列表轉換為 Entity 列表
     * 
     * @param dtoList DTO 列表
     * @return Entity 列表
     */
    fun toEntityList(dtoList: List<CompanyDto>): List<CompanyEntity> {
        return dtoList.map { toEntity(it) }
    }
    
    /**
     * 將 Entity 列表轉換為 Domain Model 列表
     * 
     * @param entityList Entity 列表
     * @return Domain Model 列表
     */
    fun toDomainListFromEntity(entityList: List<CompanyEntity>): List<Company> {
        return entityList.map { toDomain(it) }
    }
}
