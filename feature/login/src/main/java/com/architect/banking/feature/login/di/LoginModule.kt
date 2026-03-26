package com.architect.banking.feature.login.di

import com.architect.banking.core.domain.repository.LoginRepository
import com.architect.banking.feature.login.data.LoginRepositoryImpl
import com.architect.banking.feature.login.data.api.LoginApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt module that wires the login feature's network and domain dependencies.
 *
 * - [provideLoginApiService] creates the Retrofit implementation using the shared
 *   [Retrofit] instance from [NetworkModule] — no new Retrofit instance is created.
 * - [bindLoginRepository] binds [LoginRepositoryImpl] to the [LoginRepository] interface
 *   so consumers depend only on the abstraction.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class LoginModule {

    /**
     * Binds [LoginRepositoryImpl] as the [SingletonComponent]-scoped implementation
     * of [LoginRepository].
     */
    @Binds
    @Singleton
    abstract fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository

    companion object {

        /**
         * Creates a [LoginApiService] implementation via Retrofit.
         *
         * @param retrofit The shared [Retrofit] instance provided by [NetworkModule].
         */
        @Provides
        @Singleton
        fun provideLoginApiService(retrofit: Retrofit): LoginApiService =
            retrofit.create(LoginApiService::class.java)
    }
}
