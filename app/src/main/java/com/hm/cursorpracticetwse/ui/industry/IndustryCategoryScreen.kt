package com.hm.cursorpracticetwse.ui.industry

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.cursorpracticetwse.domain.model.Industry
import com.hm.cursorpracticetwse.ui.theme.CursorPracticeTWSETheme

/**
 * Industry Category Screen Composable
 * 
 * 產業分類選擇畫面
 * 顯示所有產業分類的列表，支援搜尋功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndustryCategoryScreen(
    onNavigateToCompanyList: (Industry) -> Unit,
    viewModel: IndustryCategoryViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredIndustries by viewModel.filteredIndustries.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    
    // 初始化資料載入
    LaunchedEffect(Unit) {
        viewModel.loadIndustries()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 標題列
        TopAppBar(
            title = {
                Text(
                    text = "產業分類",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(
                    onClick = { viewModel.refreshData() }
                ) {
                    Text("↻")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
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
                    onRetry = { viewModel.refreshData() },
                    onDismiss = { viewModel.clearError() }
                )
            }
            filteredIndustries.isEmpty() && searchQuery.isNotEmpty() -> {
                EmptySearchContent()
            }
            else -> {
                IndustryListContent(
                    industries = filteredIndustries,
                    onIndustryClick = { industry ->
                        onNavigateToCompanyList(industry)
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
            Text("搜尋產業分類...")
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
                text = "載入產業分類中...",
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
                text = "找不到相關產業分類",
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
 * 產業分類列表內容
 */
@Composable
private fun IndustryListContent(
    industries: List<Industry>,
    onIndustryClick: (Industry) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(industries) { industry ->
            IndustryItem(
                industry = industry,
                onClick = { onIndustryClick(industry) }
            )
        }
    }
}

/**
 * 產業分類項目
 */
@Composable
private fun IndustryItem(
    industry: Industry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = industry.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "代碼: ${industry.code}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            // 公司數量標籤
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "${industry.companyCount} 家",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
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
private fun IndustryCategoryScreenPreview() {
    CursorPracticeTWSETheme {
        IndustryListContent(
            industries = listOf(
                Industry("01", "水泥工業", 5),
                Industry("02", "食品工業", 12),
                Industry("26", "半導體業", 8)
            ),
            onIndustryClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IndustryItemPreview() {
    CursorPracticeTWSETheme {
        IndustryItem(
            industry = Industry("01", "水泥工業", 5),
            onClick = { }
        )
    }
}
