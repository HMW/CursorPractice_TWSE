package com.hm.cursorpracticetwse.ui.company

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.model.Industry
import com.hm.cursorpracticetwse.ui.theme.CursorPracticeTWSETheme

/**
 * Company List Screen Composable
 * 
 * 公司列表畫面
 * 顯示指定產業分類下的所有公司
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyListScreen(
    industry: Industry,
    onNavigateBack: () -> Unit,
    onNavigateToCompanyDetail: (Company) -> Unit,
    viewModel: CompanyListViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredCompanies by viewModel.filteredCompanies.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    
    // 載入公司資料
    LaunchedEffect(industry) {
        viewModel.loadCompaniesByIndustry(industry)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 標題列
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = industry.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "共 ${industry.companyCount} 家公司",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Text("←")
                }
            },
            actions = {
                IconButton(
                    onClick = { viewModel.refreshData(industry) }
                ) {
                    Text("↻")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        
        // 搜尋列
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        
        // 內容區域
        when {
            isLoading -> {
                LoadingContent()
            }
            errorMessage != null -> {
                ErrorContent(
                    message = errorMessage!!,
                    onRetry = { viewModel.refreshData(industry) },
                    onDismiss = { viewModel.clearError() }
                )
            }
            filteredCompanies.isEmpty() && searchQuery.isNotEmpty() -> {
                EmptySearchContent()
            }
            else -> {
                CompanyListContent(
                    companies = filteredCompanies,
                    onCompanyClick = { company ->
                        onNavigateToCompanyDetail(company)
                    }
                )
            }
        }
    }
}

/**
 * 搜尋列
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = {
            Text("搜尋公司名稱或代號...")
        },
        leadingIcon = {
            Text("🔍")
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

/**
 * 載入中內容
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "載入公司資料中...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * 錯誤內容
 */
@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "載入失敗",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(onClick = onDismiss) {
                    Text("關閉")
                }
                
                Button(onClick = onRetry) {
                    Text("重試")
                }
            }
        }
    }
}

/**
 * 空搜尋結果內容
 */
@Composable
private fun EmptySearchContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "找不到相關公司",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "請嘗試其他關鍵字",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

/**
 * 公司列表內容
 */
@Composable
private fun CompanyListContent(
    companies: List<Company>,
    onCompanyClick: (Company) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(companies) { company ->
            CompanyItem(
                company = company,
                onClick = { onCompanyClick(company) }
            )
        }
    }
}

/**
 * 公司項目
 */
@Composable
private fun CompanyItem(
    company: Company,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 公司名稱和代號
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = company.公司名稱,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "代號: ${company.公司代號}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                // 上市狀態標籤
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                    color = if (company.isListed()) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    } else {
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    }
                ) {
                    Text(
                        text = if (company.isListed()) "已上市" else "未上市",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = if (company.isListed()) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 公司資訊
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "董事長",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = company.董事長,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "總經理",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = company.總經理,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 產業分類
            Text(
                text = "產業分類: ${company.getIndustryName()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            if (company.上市日期.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "上市日期: ${company.上市日期}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

/**
 * Preview
 */
@Preview(showBackground = true)
@Composable
private fun CompanyListScreenPreview() {
    CursorPracticeTWSETheme {
        CompanyListContent(
            companies = listOf(
                createTestCompany("2330", "台積電"),
                createTestCompany("2317", "鴻海"),
                createTestCompany("2454", "聯發科")
            ),
            onCompanyClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CompanyItemPreview() {
    CursorPracticeTWSETheme {
        CompanyItem(
            company = createTestCompany("2330", "台積電"),
            onClick = { }
        )
    }
}

private fun createTestCompany(code: String, name: String): Company {
    return Company(
        出表日期 = "2023-01-01",
        公司代號 = code,
        公司名稱 = name,
        公司簡稱 = name,
        外國企業註冊地國 = "",
        產業別 = "26:半導體業",
        住址 = "台北市",
        營利事業統一編號 = "12345678",
        董事長 = "張三",
        總經理 = "李四",
        發言人 = "王五",
        發言人職稱 = "財務長",
        代理發言人 = "",
        總機電話 = "02-12345678",
        成立日期 = "2020-01-01",
        上市日期 = "2023-01-01",
        普通股每股面額 = "10",
        實收資本額 = "1000000000",
        已發行普通股數或TDR原股發行股數 = "100000000",
        私募股數 = "0",
        特別股 = "0",
        編製財務報告類型 = "1",
        股票過戶機構 = "台灣集中保管結算所",
        過戶電話 = "02-23456789",
        過戶地址 = "台北市",
        簽證會計師事務所 = "勤業眾信",
        簽證會計師1 = "會計師A",
        簽證會計師2 = "會計師B",
        英文簡稱 = "Test Corp",
        英文通訊地址 = "Taipei, Taiwan",
        傳真機號碼 = "02-12345679",
        電子郵件信箱 = "test@example.com",
        網址 = "https://www.example.com"
    )
}
