package com.hm.cursorpracticetwse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hm.cursorpracticetwse.ui.launch.LaunchScreen
import com.hm.cursorpracticetwse.ui.theme.CursorPracticeTWSETheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CursorPracticeTWSETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TwseApp()
                }
            }
        }
    }
}

/**
 * TWSE 應用程式主入口
 * 
 * 目前只顯示 Launch Screen
 * 後續會加入 Navigation 來處理多個畫面
 */
@Composable
fun TwseApp() {
    var showMainScreen by remember { mutableStateOf(false) }
    
    if (showMainScreen) {
        // TODO: 實作主畫面
        MainScreen()
    } else {
        LaunchScreen(
            onNavigateToMain = {
                showMainScreen = true
            }
        )
    }
}

/**
 * 主畫面（暫時實作）
 * 
 * TODO: 後續會實作完整的主畫面
 */
@Composable
private fun MainScreen() {
    // TODO: 實作主畫面內容
    androidx.compose.material3.Text(
        text = "主畫面 - 開發中",
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun TwseAppPreview() {
    CursorPracticeTWSETheme {
        TwseApp()
    }
}