package com.architect.banking.engine.sdui.di

import com.architect.banking.engine.sdui.api.ScreenApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
//import retrofit2.Retrofit
import javax.inject.Singleton


/**
 * Hilt module providing SDUI engine dependencies.
 * Installed in [SingletonComponent] so the service is application-scoped.
 */
@Module
@InstallIn(SingletonComponent::class)
object SduiModule {

//    @Provides
//    @Singleton
//    fun provideScreenApiService(retrofit: Retrofit): ScreenApiService =
//        retrofit.create(ScreenApiService::class.java)

    @Provides
    @Singleton
    fun provideScreenApiService(): ScreenApiService = object : ScreenApiService {}
}
