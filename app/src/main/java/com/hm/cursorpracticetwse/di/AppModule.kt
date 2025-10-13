package com.hm.cursorpracticetwse.di

import com.hm.cursorpracticetwse.data.repository.TwseRepositoryImpl
import com.hm.cursorpracticetwse.domain.repository.TwseRepository
import com.hm.cursorpracticetwse.domain.usecase.GetCompaniesByIndustryUseCase
import com.hm.cursorpracticetwse.domain.usecase.GetCompaniesUseCase
import com.hm.cursorpracticetwse.domain.usecase.GetIndustriesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * App Module for Hilt Dependency Injection
 * 
 * 提供應用程式層級的依賴注入
 * 包含 Repository 和 Use Cases
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    
    /**
     * 提供 TWSE Repository
     * 綁定介面和實作
     */
    @Binds
    @Singleton
    abstract fun bindTwseRepository(
        twseRepositoryImpl: TwseRepositoryImpl
    ): TwseRepository
    
    companion object {
        
        /**
         * 提供 GetCompaniesUseCase
         */
        @Provides
        @Singleton
        fun provideGetCompaniesUseCase(
            repository: TwseRepository
        ): GetCompaniesUseCase {
            return GetCompaniesUseCase(repository)
        }
        
        /**
         * 提供 GetIndustriesUseCase
         */
        @Provides
        @Singleton
        fun provideGetIndustriesUseCase(
            repository: TwseRepository
        ): GetIndustriesUseCase {
            return GetIndustriesUseCase(repository)
        }
        
        /**
         * 提供 GetCompaniesByIndustryUseCase
         */
        @Provides
        @Singleton
        fun provideGetCompaniesByIndustryUseCase(
            repository: TwseRepository
        ): GetCompaniesByIndustryUseCase {
            return GetCompaniesByIndustryUseCase(repository)
        }
    }
}
