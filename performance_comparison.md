# 效能比較：Data Cache vs Local Data Source

## 測試場景
- Industry Category 頁面載入 1000 家公司資料
- 用戶立即點擊進入 Company List 頁面
- 測量 Company List 頁面載入時間

## 方案 1：Data Cache
```
Industry Category 載入：
1. API 請求：2000ms
2. 存入 Local DB：100ms
3. 存入 Data Cache：1ms
總計：2101ms

Company List 載入：
1. 從 Data Cache 讀取：1ms
2. 顯示資料：0ms
總計：1ms

總載入時間：2102ms
```

## 方案 2：Local Data Source
```
Industry Category 載入：
1. API 請求：2000ms
2. 存入 Local DB：100ms
總計：2100ms

Company List 載入：
1. Room 查詢：50ms
2. Entity 轉換：20ms
3. 顯示資料：0ms
總計：70ms

總載入時間：2170ms
```

## 效能差異
- Data Cache 方案：2102ms
- Local Data Source 方案：2170ms
- 差異：68ms (約 3.2% 提升)

## 記憶體使用
- Data Cache：額外 2-5MB 記憶體
- Local Data Source：無額外記憶體

## 結論
Data Cache 在頻繁切換頁面時提供更好的用戶體驗，
但需要權衡記憶體使用。
