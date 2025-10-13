# TWSE Android APP 詳細實作計畫

## 架構設計說明

### Repository Interface 和 Models 放在 Domain Layer 的原因

**Repository Interface 放在 Domain Layer：**
1. **依賴反轉原則 (Dependency Inversion Principle)**：Domain Layer 定義介面，Data Layer 實作介面
2. **避免循環依賴**：如果放在 Data Layer，Domain Layer 會依賴 Data Layer，造成循環依賴
3. **測試友善**：Domain Layer 可以輕鬆 mock Repository 介面進行單元測試
4. **業務邏輯獨立**：Domain Layer 不應該知道資料來源的實作細節

**Models 放在 Domain Layer：**
1. **純業務邏輯**：Domain Models 代表業務概念，不依賴任何框架
2. **可重用性**：可以在不同層之間共享，不受技術實作影響
3. **測試獨立**：可以獨立測試業務邏輯，不需要 Android 環境

**正確的依賴方向：**
```
UI Layer → Domain Layer → Data Layer
```

## 核心檔案結構

```
app/src/main/java/com/hm/cursorpracticetwse/
├── di/                          # Hilt 依賴注入
│   ├── AppModule.kt
│   ├── NetworkModule.kt
│   └── DatabaseModule.kt
├── data/
│   ├── remote/
│   │   ├── TwseApiService.kt
│   │   ├── TwseRemoteDataSource.kt
│   │   └── dto/CompanyDto.kt
│   ├── local/
│   │   ├── TwseDatabase.kt
│   │   ├── TwseDao.kt
│   │   ├── TwseLocalDataSource.kt
│   │   └── entities/
│   │       ├── CompanyEntity.kt
│   │       └── IndustryEntity.kt
│   ├── repository/
│   │   └── TwseRepositoryImpl.kt
│   └── mapper/
│       ├── CompanyMapper.kt
│       └── IndustryMapper.kt
├── domain/
│   ├── model/
│   │   ├── Company.kt
│   │   └── Industry.kt
│   ├── repository/
│   │   └── TwseRepository.kt (interface)
│   └── usecase/
│       ├── GetCompaniesUseCase.kt
│       ├── GetIndustriesUseCase.kt
│       └── GetCompaniesByIndustryUseCase.kt
└── ui/
    ├── navigation/
    │   └── NavGraph.kt
    ├── launch/
    │   ├── LaunchScreen.kt
    │   └── LaunchViewModel.kt
    ├── industry/
    │   ├── IndustryListScreen.kt
    │   └── IndustryViewModel.kt
    ├── company/
    │   ├── CompanyListScreen.kt
    │   └── CompanyViewModel.kt
    └── theme/ (已存在)
```

## 詳細實作步驟（含測試）

### 階段 1: 環境設定與依賴配置

**實作內容：**
- 更新 `gradle/libs.versions.toml` 新增 Hilt, Retrofit, Room, Navigation 等依賴
- 更新 `app/build.gradle.kts` 啟用 Hilt, KSP, Serialization
- 在 `AndroidManifest.xml` 添加網路權限和 Hilt Application
- 創建 `TwseApplication.kt` 繼承 Application 並標註 @HiltAndroidApp

**測試驗證：**
- 執行 `./gradlew build` 確認專案編譯成功
- 執行 `./gradlew test` 確認測試環境正常
- 檢查 Hilt 是否正確初始化（Application 啟動無錯誤）

### 階段 2: Domain Layer 實作

**實作內容：**
- 創建 `domain/model/Company.kt` - 公司領域模型（40+ 欄位）
- 創建 `domain/model/Industry.kt` - 產業領域模型
- 創建 `domain/repository/TwseRepository.kt` - Repository 介面
- 創建三個 Use Cases：
  - `GetCompaniesUseCase` - 取得所有公司
  - `GetIndustriesUseCase` - 取得產業分類
  - `GetCompaniesByIndustryUseCase` - 依產業別取得公司

**測試驗證：**
- 創建 `test/java/domain/model/CompanyTest.kt` - 測試 Company 模型
- 創建 `test/java/domain/model/IndustryTest.kt` - 測試 Industry 模型
- 創建 `test/java/domain/usecase/GetCompaniesUseCaseTest.kt` - 測試 Use Case 邏輯
- 執行 `./gradlew test` 確認所有 Domain Layer 測試通過

### 階段 3: Data Layer - Remote 實作

**實作內容：**
- 創建 `data/remote/dto/CompanyDto.kt` - API 回應 DTO
- 創建 `data/remote/TwseApiService.kt` - Retrofit API 介面
- 創建 `data/remote/TwseRemoteDataSource.kt` - 網路資料源
- 創建 `di/NetworkModule.kt` - 提供 Retrofit, OkHttp 等

**測試驗證：**
- 創建 `test/java/data/remote/TwseApiServiceTest.kt` - 使用 MockWebServer 測試 API
- 創建 `test/java/data/remote/TwseRemoteDataSourceTest.kt` - 測試 RemoteDataSource
- 執行 `./gradlew test` 確認 Remote Layer 測試通過
- 手動測試 API 連線是否正常（使用 Postman 或 curl）

### 階段 4: Data Layer - Local 實作

**實作內容：**
- 創建 `data/local/entities/CompanyEntity.kt` - Room Entity
- 創建 `data/local/entities/IndustryEntity.kt` - Room Entity (用於查詢結果)
- 創建 `data/local/TwseDao.kt` - Room DAO 介面
- 創建 `data/local/TwseDatabase.kt` - Room Database
- 創建 `data/local/TwseLocalDataSource.kt` - 本地資料源
- 創建 `di/DatabaseModule.kt` - 提供 Database 和 DAO

**測試驗證：**
- 創建 `androidTest/java/data/local/TwseDaoTest.kt` - 使用 Room 測試資料庫操作
- 創建 `test/java/data/local/TwseLocalDataSourceTest.kt` - 測試 LocalDataSource
- 執行 `./gradlew connectedAndroidTest` 確認資料庫測試通過
- 手動測試資料庫 CRUD 操作

### 階段 5: Data Layer - Repository & Mapper

**實作內容：**
- 創建 `data/mapper/CompanyMapper.kt` - DTO/Entity/Domain 轉換
- 創建 `data/mapper/IndustryMapper.kt` - Entity/Domain 轉換
- 創建 `data/repository/TwseRepositoryImpl.kt` - 實作離線優先策略
- 創建 `di/AppModule.kt` - 提供 Repository 和 Use Cases

**測試驗證：**
- 創建 `test/java/data/mapper/CompanyMapperTest.kt` - 測試資料轉換邏輯
- 創建 `test/java/data/mapper/IndustryMapperTest.kt` - 測試產業轉換邏輯
- 創建 `test/java/data/repository/TwseRepositoryImplTest.kt` - 測試 Repository 實作
- 執行 `./gradlew test` 確認 Repository 和 Mapper 測試通過
- 測試離線優先策略：模擬網路斷線情況

### 階段 6: UI Layer - Launch Screen

**實作內容：**
- 創建 `ui/launch/LaunchViewModel.kt` - 處理啟動邏輯和資料載入
- 創建 `ui/launch/LaunchScreen.kt` - 顯示 Loading 動畫
- 實作 UiState 和資料載入流程

**測試驗證：**
- 創建 `test/java/ui/launch/LaunchViewModelTest.kt` - 測試 ViewModel 邏輯
- 創建 `androidTest/java/ui/launch/LaunchScreenTest.kt` - 測試 UI 元件
- 執行 `./gradlew test` 和 `./gradlew connectedAndroidTest` 確認測試通過
- 手動測試 Launch 畫面載入流程

### 階段 7: UI Layer - Industry Screen

**實作內容：**
- 創建 `ui/industry/IndustryViewModel.kt` - 處理產業列表邏輯
- 創建 `ui/industry/IndustryListScreen.kt` - 顯示 40 個產業類別
- 實作產業列表 UI，包含產業名稱和公司數量

**測試驗證：**
- 創建 `test/java/ui/industry/IndustryViewModelTest.kt` - 測試產業 ViewModel
- 創建 `androidTest/java/ui/industry/IndustryListScreenTest.kt` - 測試產業列表 UI
- 執行測試確認產業列表功能正常
- 手動測試產業列表顯示和點擊功能

### 階段 8: UI Layer - Company Screen

**實作內容：**
- 創建 `ui/company/CompanyViewModel.kt` - 處理公司列表邏輯
- 創建 `ui/company/CompanyListScreen.kt` - 顯示公司列表
- 實作公司列表 UI，顯示公司基本資訊

**測試驗證：**
- 創建 `test/java/ui/company/CompanyViewModelTest.kt` - 測試公司 ViewModel
- 創建 `androidTest/java/ui/company/CompanyListScreenTest.kt` - 測試公司列表 UI
- 執行測試確認公司列表功能正常
- 手動測試公司列表顯示和搜尋功能

### 階段 9: Navigation 整合

**實作內容：**
- 創建 `ui/navigation/NavGraph.kt` - 定義路由和導航圖
- 整合三個畫面：Launch → Industry List → Company List
- 更新 `MainActivity.kt` 設定 Navigation Host

**測試驗證：**
- 創建 `androidTest/java/ui/navigation/NavigationTest.kt` - 測試導航流程
- 執行 `./gradlew connectedAndroidTest` 確認導航測試通過
- 手動測試完整導航流程：Launch → Industry → Company
- 測試返回按鈕和深層連結

### 階段 10: 整合測試與優化

**實作內容：**
- 整合測試完整 APP 流程
- 處理錯誤狀態和 Loading 狀態
- 優化效能和使用者體驗
- 加入錯誤處理和重試機制

**測試驗證：**
- 執行完整 E2E 測試：`./gradlew connectedAndroidTest`
- 測試離線功能：關閉網路後測試 APP 行為
- 測試 UI 流暢度：檢查是否有卡頓或記憶體洩漏
- 效能測試：使用 Android Studio Profiler 檢查效能
- 使用者體驗測試：確認所有功能符合需求

## 測試策略

### 單元測試 (Unit Tests)
- **Domain Layer**: 測試 Use Cases 和 Domain Models
- **Data Layer**: 測試 Repository, Mapper, RemoteDataSource
- **UI Layer**: 測試 ViewModels

### 整合測試 (Integration Tests)
- **Database**: 測試 Room 資料庫操作
- **Network**: 測試 Retrofit API 呼叫
- **Navigation**: 測試畫面導航流程

### UI 測試 (UI Tests)
- **Compose UI**: 測試 Composable 元件
- **E2E**: 測試完整使用者流程

### 測試工具
- **JUnit 5**: 單元測試框架
- **MockK**: Mock 框架
- **MockWebServer**: 模擬網路請求
- **Room Testing**: 資料庫測試
- **Compose Testing**: UI 測試

## 品質保證

### 程式碼品質
- **Kotlin Lint**: 程式碼風格檢查
- **Detekt**: 靜態程式碼分析
- **Code Coverage**: 測試覆蓋率 > 80%

### 效能監控
- **Memory Leaks**: 使用 LeakCanary 檢測記憶體洩漏
- **Performance**: 使用 Android Studio Profiler
- **Network**: 監控 API 呼叫效能

### 使用者體驗
- **Accessibility**: 無障礙功能測試
- **Dark Mode**: 深色模式支援
- **Responsive**: 不同螢幕尺寸適配
