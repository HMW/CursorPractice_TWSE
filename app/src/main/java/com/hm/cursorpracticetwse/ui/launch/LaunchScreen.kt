package com.hm.cursorpracticetwse.ui.launch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hm.cursorpracticetwse.R
import com.hm.cursorpracticetwse.ui.theme.CursorPracticeTWSETheme

/**
 * Launch Screen Composable
 * 
 * 應用程式的啟動畫面
 * 顯示載入動畫和應用程式資訊
 */
@Composable
fun LaunchScreen(
    onNavigateToMain: () -> Unit,
    viewModel: LaunchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    
    // 初始化資料載入
    LaunchedEffect(Unit) {
        viewModel.initializeData()
    }
    
    // 處理導航
    LaunchedEffect(uiState) {
        when (uiState) {
            is LaunchUiState.NavigateToMain -> {
                onNavigateToMain()
            }
            else -> { /* 其他狀態不需要導航 */ }
        }
    }
    
    LaunchScreenContent(
        isLoading = isLoading,
        errorMessage = errorMessage,
        onRetry = { viewModel.refreshData() },
        onDismissError = { viewModel.clearError() }
    )
}

/**
 * Launch Screen 內容
 * 
 * @param isLoading 是否正在載入
 * @param errorMessage 錯誤訊息
 * @param onRetry 重試按鈕點擊事件
 * @param onDismissError 關閉錯誤訊息事件
 */
@Composable
private fun LaunchScreenContent(
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onDismissError: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 應用程式圖示
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "TWSE App Logo",
                modifier = Modifier.size(120.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 應用程式標題
            Text(
                text = "TWSE 台股資訊",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 應用程式描述
            Text(
                text = "台灣證券交易所公開資訊查詢",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White.copy(alpha = 0.8f)
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // 載入狀態顯示
            when {
                isLoading -> {
                    LoadingIndicator()
                }
                errorMessage != null -> {
                    ErrorContent(
                        message = errorMessage,
                        onRetry = onRetry,
                        onDismiss = onDismissError
                    )
                }
                else -> {
                    // 初始狀態，顯示歡迎訊息
                    Text(
                        text = "正在準備資料...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.7f)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * 載入指示器
 */
@Composable
private fun LoadingIndicator() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            color = Color.White,
            strokeWidth = 3.dp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "載入中...",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White.copy(alpha = 0.8f)
            ),
            textAlign = TextAlign.Center
        )
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "載入失敗",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White.copy(alpha = 0.8f)
            ),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp
                )
            ) {
                Text("關閉")
            }
            
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("重試")
            }
        }
    }
}

/**
 * Preview
 */
@Preview(showBackground = true)
@Composable
private fun LaunchScreenPreview() {
    CursorPracticeTWSETheme {
        LaunchScreenContent(
            isLoading = true,
            errorMessage = null,
            onRetry = { },
            onDismissError = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LaunchScreenErrorPreview() {
    CursorPracticeTWSETheme {
        LaunchScreenContent(
            isLoading = false,
            errorMessage = "網路連線失敗，請檢查網路設定",
            onRetry = { },
            onDismissError = { }
        )
    }
}
