# 簡化的架構設計

## 移除 Data Cache 的理由

### 1. 架構複雜度
- 減少一層抽象
- 降低維護成本
- 更清晰的職責分離

### 2. 效能影響
- Local Data Source 的 Room 查詢已經很快
- 記憶體使用更少
- 避免重複的資料存儲

### 3. 簡化實現
```kotlin
// 簡化前：需要 Data Cache
IndustryCategoryViewModel -> CompanyDataCache -> TwseRepository
CompanyListViewModel -> CompanyDataCache -> TwseRepository

// 簡化後：直接使用 Repository
IndustryCategoryViewModel -> TwseRepository
CompanyListViewModel -> TwseRepository
```

## 建議的簡化方案

### 方案 1：Repository 內部快取
```kotlin
class TwseRepositoryImpl {
    private var cachedCompanies: List<Company>? = null
    private var cachedIndustries: List<Industry>? = null
    
    override suspend fun getCompanies(): Result<List<Company>> {
        if (cachedCompanies != null) {
            return Result.success(cachedCompanies!!)
        }
        // 載入並快取...
    }
}
```

### 方案 2：直接使用 Local Data Source
```kotlin
// ViewModel 直接使用 Local Data Source
class CompanyListViewModel {
    fun loadCompaniesByIndustry(industry: Industry) {
        val companies = localDataSource.getCompaniesByIndustry(industry.code)
        // 處理結果...
    }
}
```

## 結論
您的觀點是正確的，Data Cache 確實增加了不必要的複雜度。
建議移除 Data Cache，直接使用 Local Data Source 或 Repository 內部快取。
