package com.hm.cursorpracticetwse.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.hm.cursorpracticetwse.domain.model.Company
import com.hm.cursorpracticetwse.domain.model.Industry
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import com.hm.cursorpracticetwse.ui.company.CompanyListScreen
import com.hm.cursorpracticetwse.ui.company.CompanyDetailScreen
import com.hm.cursorpracticetwse.ui.industry.IndustryCategoryScreen
import com.hm.cursorpracticetwse.ui.launch.LaunchScreen
import com.hm.cursorpracticetwse.ui.watchlist.WatchlistScreen

/**
 * TWSE 應用程式導航
 * 
 * 定義應用程式的所有導航路由和導航邏輯
 */
@Composable
fun TwseNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = TwseRoute.Launch.route
    ) {
        // Launch Screen
        composable(TwseRoute.Launch.route) {
            Log.d("TwseNavigation", "twse] Launch Screen composable called")
            LaunchScreen(
                onNavigateToMain = {
                    Log.d("TwseNavigation", "twse] Launch Screen navigation triggered")
                    navController.navigate(TwseRoute.IndustryCategory.route) {
                        popUpTo(TwseRoute.Launch.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        
        // Industry Category Screen
        composable(TwseRoute.IndustryCategory.route) {
            Log.d("TwseNavigation", "twse] Industry Category Screen composable called")
            IndustryCategoryScreen(
                onNavigateToCompanyList = { industry ->
                    Log.d("TwseNavigation", "twse] Industry Category navigation triggered for: ${industry.name}")
                    navController.navigate(
                        TwseRoute.CompanyList.createRoute(industry)
                    )
                },
                onNavigateToWatchlist = {
                    Log.d("TwseNavigation", "twse] Industry Category navigation to watchlist")
                    navController.navigate(TwseRoute.Watchlist.route)
                }
            )
        }
        
        // Watchlist Screen
        composable(TwseRoute.Watchlist.route) {
            Log.d("TwseNavigation", "twse] Watchlist Screen composable called")
            WatchlistScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCompanyDetail = { watchlistItem ->
                    // TODO: 導航到公司詳細頁面
                    // 需要將 WatchlistItem 轉換為 Company 或使用公司代碼查詢
                    Log.d("TwseNavigation", "twse] Watchlist navigation to company detail: ${watchlistItem.companyCode}")
                }
            )
        }
        
        // Company List Screen
        composable("${TwseRoute.CompanyList.route}/{${TwseRoute.CompanyList.industryArg}}") { backStackEntry ->
            val industryCode = backStackEntry.arguments?.getString(TwseRoute.CompanyList.industryArg) ?: "01"
            val industry = Industry(industryCode, "產業分類", 0) // 名稱會在 ViewModel 中更新
            
            CompanyListScreen(
                industry = industry,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCompanyDetail = { company ->
                    Log.d("TwseNavigation", "twse] Company Detail navigation triggered for: ${company.公司代號}")
                    navController.navigate(TwseRoute.CompanyDetail.createRoute(company))
                }
            )
        }
        
        // Company Detail Screen
        composable("${TwseRoute.CompanyDetail.route}/{${TwseRoute.CompanyDetail.companyArg}}") { backStackEntry ->
            backStackEntry
                .arguments
                ?.getString(TwseRoute.CompanyDetail.companyArg)
                ?.let { encodedCompanyJson ->
                    CompanyDetailScreen(
                        company = Gson().fromJson(
                            URLDecoder.decode(
                                encodedCompanyJson,
                                StandardCharsets.UTF_8.toString()
                            ),
                            Company::class.java
                        ),
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
        }
    }
}

/**
 * TWSE 導航路由
 */
sealed class TwseRoute(val route: String) {
    
    /**
     * Launch Screen
     */
    object Launch : TwseRoute("launch")
    
    /**
     * Industry Category Screen
     */
    object IndustryCategory : TwseRoute("industry_category")
    
    /**
     * Watchlist Screen
     */
    object Watchlist : TwseRoute("watchlist")
    
    /**
     * Company List Screen
     */
    object CompanyList : TwseRoute("company_list") {
        const val industryArg = "industry"
        
        fun createRoute(industry: Industry): String {
            return "$route/${industry.code}"
        }
    }
    
    /**
     * Company Detail Screen (未來實作)
     */
    object CompanyDetail : TwseRoute("company_detail") {
        const val companyArg = "company"
        
        fun createRoute(company: Company): String {
            val companyJson = Gson().toJson(company)
            val encodedJson = URLEncoder.encode(companyJson, StandardCharsets.UTF_8.toString())
            return "$route/$encodedJson"
        }
    }
}
