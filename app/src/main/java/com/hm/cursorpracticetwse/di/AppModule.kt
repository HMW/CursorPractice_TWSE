package com.hm.cursorpracticetwse.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * App Module for Hilt Dependency Injection
 * 
 * 這個模組提供應用程式層級的依賴注入
 * 目前是空的，稍後會添加 Repository 和 Use Cases
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    // 稍後會在這裡添加 Repository 和 Use Cases 的提供方法
}
