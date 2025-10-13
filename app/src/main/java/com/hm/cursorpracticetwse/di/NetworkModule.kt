package com.hm.cursorpracticetwse.di

import com.hm.cursorpracticetwse.data.remote.TwseApiService
import com.hm.cursorpracticetwse.data.remote.TwseRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Network Module for Hilt Dependency Injection
 * 
 * 提供網路相關的依賴注入配置
 * 包含 OkHttp, Retrofit, API Service 等
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    private const val BASE_URL = "https://openapi.twse.com.tw"
    private const val TIMEOUT_SECONDS = 30L
    
    /**
     * 提供 OkHttpClient
     * 配置連線超時、讀取超時和日誌記錄
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * 提供 Retrofit 實例
     * 配置基礎 URL 和 JSON 轉換器
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * 提供 TWSE API Service
     * 使用 Retrofit 創建 API 介面實例
     */
    @Provides
    @Singleton
    fun provideTwseApiService(retrofit: Retrofit): TwseApiService {
        return retrofit.create(TwseApiService::class.java)
    }
    
    /**
     * 提供 TWSE Remote Data Source
     * 注入 API Service 依賴
     */
    @Provides
    @Singleton
    fun provideTwseRemoteDataSource(apiService: TwseApiService): TwseRemoteDataSource {
        return TwseRemoteDataSource(apiService)
    }
}
