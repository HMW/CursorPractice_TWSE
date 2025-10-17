package com.hm.cursorpracticetwse.ui.company

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.ui.theme.CursorPracticeTWSETheme

/**
 * Company Detail Screen Composable
 * 
 * 公司詳細資料畫面
 * 顯示公司的完整基本資料，包括追蹤功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyDetailScreen(
    company: Company,
    onNavigateBack: () -> Unit,
    viewModel: CompanyDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isWatched by viewModel.isWatched.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    
    // 初始化資料載入
    LaunchedEffect(company) {
        viewModel.loadCompanyDetail(company)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 標題列
        TopAppBar(
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = company.getIndustryName(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "${company.公司代號} ${company.公司簡稱}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { viewModel.toggleWatchlist() }
                ) {
                    Icon(
                        imageVector = if (isWatched) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = if (isWatched) "已追蹤" else "未追蹤",
                        tint = if (isWatched) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        
        // 內容區域
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 基本資料區塊
            InfoCard(
                title = "基本資料",
                content = {
                    InfoRow(
                        label = "公司名稱",
                        value = company.公司名稱,
                        hasLink = company.網址.isNotBlank(),
                        onLinkClick = {
                            if (company.網址.isNotBlank()) {
                                uriHandler.openUri(company.網址)
                            }
                        }
                    )
                    InfoRow(label = "董事長", value = company.董事長)
                    InfoRow(label = "總經理", value = company.總經理)
                    InfoRow(label = "產業類別", value = company.getIndustryName())
                    InfoRow(label = "公司成立日期", value = company.成立日期)
                    if (company.上市日期.isNotBlank()) {
                        InfoRow(label = "上市日期", value = company.上市日期)
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 聯絡資訊區塊
            InfoCard(
                title = "聯絡資訊",
                content = {
                    if (company.總機電話.isNotBlank()) {
                        InfoRow(label = "總機", value = company.總機電話)
                    }
                    InfoRow(label = "統一編號", value = company.營利事業統一編號)
                    InfoRow(label = "地址", value = company.住址)
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 財務資訊區塊
            InfoCard(
                title = "財務資訊",
                content = {
                    InfoRow(
                        label = "實收資本額 (元)",
                        value = formatNumber(company.實收資本額)
                    )
                    InfoRow(
                        label = "普通股每股面額",
                        value = "新台幣 ${formatNumber(company.普通股每股面額)} 元"
                    )
                    InfoRow(
                        label = "已發行普通股數或TDR原股發行股數",
                        value = "${formatNumber(company.已發行普通股數或TDR原股發行股數)} 股 (含私募 ${formatNumber(company.私募股數)} 股)"
                    )
                    if (company.特別股.isNotBlank()) {
                        InfoRow(
                            label = "特別股",
                            value = "${formatNumber(company.特別股)} 股"
                        )
                    }
                }
            )
        }
    }
    
    // 追蹤確認對話框
    val showDialog = when (val currentState = uiState) {
        is CompanyDetailUiState.Success -> currentState.showWatchlistDialog
        else -> false
    }
    if (showDialog) {
        WatchlistDialog(
            company = company,
            isAdding = !isWatched,
            onConfirm = { viewModel.confirmWatchlistToggle() },
            onDismiss = { viewModel.dismissWatchlistDialog() }
        )
    }
}

/**
 * 資訊卡片
 */
@Composable
private fun InfoCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

/**
 * 資訊行
 */
@Composable
private fun InfoRow(
    label: String,
    value: String,
    hasLink: Boolean = false,
    onLinkClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        
        if (hasLink && onLinkClick != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onLinkClick() }
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "公司網站",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 追蹤對話框
 */
@Composable
private fun WatchlistDialog(
    company: Company,
    isAdding: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isAdding) "加入追蹤列表" else "從追蹤列表中移除"
            )
        },
        text = {
            Text(
                text = if (isAdding) 
                    "是否將 ${company.公司代號} ${company.公司簡稱} 加入追蹤列表內?" 
                else 
                    "是否將 ${company.公司代號} ${company.公司簡稱} 從追蹤列表中移除?"
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = if (isAdding) "加入" else "移除"
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

/**
 * 格式化數字，添加千分位逗號
 */
private fun formatNumber(numberString: String): String {
    return try {
        val number = numberString.replace(",", "").toLongOrNull()
        if (number != null) {
            String.format("%,d", number)
        } else {
            numberString
        }
    } catch (e: Exception) {
        numberString
    }
}

/**
 * Preview
 */
@Preview(showBackground = true)
@Composable
private fun CompanyDetailScreenPreview() {
    CursorPracticeTWSETheme {
        CompanyDetailScreen(
            company = Company(
                出表日期 = "2024-01-01",
                公司代號 = "1101",
                公司名稱 = "臺灣水泥股份有限公司",
                公司簡稱 = "台泥",
                外國企業註冊地國 = "",
                產業別 = "01",
                住址 = "台北市中山北路2段113號",
                營利事業統一編號 = "11913502",
                董事長 = "張安平",
                總經理 = "張安平",
                發言人 = "張安平",
                發言人職稱 = "董事長",
                代理發言人 = "",
                總機電話 = "(02)2531-7099",
                成立日期 = "1950/12/29",
                上市日期 = "1962/02/09",
                普通股每股面額 = "10.0000",
                實收資本額 = "73561817420",
                已發行普通股數或TDR原股發行股數 = "7156181742",
                私募股數 = "0",
                特別股 = "200000000",
                編製財務報告類型 = "",
                股票過戶機構 = "",
                過戶電話 = "",
                過戶地址 = "",
                簽證會計師事務所 = "",
                簽證會計師1 = "",
                簽證會計師2 = "",
                英文簡稱 = "",
                英文通訊地址 = "",
                傳真機號碼 = "",
                電子郵件信箱 = "",
                網址 = "https://www.taiwancement.com.tw"
            ),
            onNavigateBack = {}
        )
    }
}
